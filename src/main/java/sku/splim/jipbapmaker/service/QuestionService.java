package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.repository.QuestionRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public void save(Question question) {
        questionRepository.save(question);
    }

    public List<Question> findAllByOrderByModifyDateDesc() {
        return questionRepository.findAllByOrderByModifyDateDesc();
    }

    public List<Question> findAllByUserIdOrderByCreateDateDesc(long userid) {
        return questionRepository.findAllByUserIdAndDeletedAtIsNullOrderByCreateDateDesc(userid);
    }

    public boolean softDeleteQuestionById(long id) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setDeletedAt(new Timestamp(System.currentTimeMillis()));
            questionRepository.save(question);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public long deleteQuestionById(long id) {
        Question question = questionRepository.findById(id).orElse(null);



        return questionRepository.deleteQuestionById(id);
    }

    public List<Question> findAllByStatusFalse(){
        return questionRepository.findAllByStatusFalseOrderByDeletedAt();
    }

    public Question findById(long id) {
        return questionRepository.findById(id).orElse(null);
    }
}
