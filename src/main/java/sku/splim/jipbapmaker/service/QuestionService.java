package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public long deleteQuestionById(long id) {
        // id에 해당하는 질문을 삭제하고 삭제된 항목의 수를 반환
        return questionRepository.deleteQuestionById(id);
    }
}
