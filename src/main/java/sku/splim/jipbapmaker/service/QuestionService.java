package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.repository.QuestionRepository;
import java.util.List;

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

    public List<Question> findAllByUserIdByModifyDateDesc(long userid) {
        return questionRepository.findAllByUserIdOrderByModifyDateDesc(userid);
    }

    @Transactional
    public long deleteQuestionById(long id) {
        Question question = questionRepository.findById(id).orElse(null);



        return questionRepository.deleteQuestionById(id);
    }

    public List<Question> findAllByStatusFalse(){
        return questionRepository.findAllByStatusFalse();
    }

    public Question findById(long id) {
        return questionRepository.findById(id).orElse(null);
    }
}
