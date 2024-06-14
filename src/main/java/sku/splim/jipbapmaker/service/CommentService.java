package sku.splim.jipbapmaker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Comment;
import sku.splim.jipbapmaker.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> recipeComment(Long id) {
        return commentRepository.findAllByRecipeIdOrderByModifyDate(id);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void update(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setContent(content);
        comment.setUpdatedFlag(true);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid comment Id:" + id);
        }
        commentRepository.deleteById(id);
    }

    public long getCommentCountByRecipeId(Long recipeId) {
        return commentRepository.countByRecipeId(recipeId);
    }
}
