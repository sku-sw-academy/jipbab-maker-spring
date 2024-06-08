package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.repository.AnswerRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private LogService logService;

    public void save(Answer answer) {
        Question question = answer.getQuestion();
        answerRepository.save(answer);
        question.setStatus(true);
        logService.QnA(answer);
    }

    @Transactional
    public void delete(Answer answer) {
        Question question = answer.getQuestion();
        answerRepository.delete(answer);
        question.setStatus(false);
        logService.deleteQnA(answer);
    }

    public Answer findById(Long id) {
        return answerRepository.findById(id).orElse(null);
    }

    public List<Answer> getAnswers(long id){
        return answerRepository.findAllByAdminIdOrderByModifyDateDesc(id);
    }

    public Answer getQuestionAnswer(long id){
        return answerRepository.findByQuestionId(id);
    }

    public boolean updateAnswer(Long id, String content) {
        try {
            Optional<Answer> answerOptional = answerRepository.findById(id);
            if (answerOptional.isPresent()) {
                Answer answer = answerOptional.get();
                answer.setContent(content);
                answerRepository.save(answer);
                logService.updateQnA(answer);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
