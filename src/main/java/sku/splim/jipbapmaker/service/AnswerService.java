package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.repository.AnswerRepository;

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

    public void update(Answer answer) {
        answerRepository.save(answer);
        logService.updateQnA(answer);
    }

    @Transactional
    public void delete(Answer answer) {
        Question question = answer.getQuestion();
        answerRepository.delete(answer);
        question.setStatus(false);
        logService.deleteQnA(answer);
    }
}
