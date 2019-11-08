package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.CommentDTO;
import life.maijiang.community.community.dto.QuestionDTO;
import life.maijiang.community.community.enums.CommentTypeEnum;
import life.maijiang.community.community.service.CommentService;
import life.maijiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {
        //获取到当前的questionDTO
        QuestionDTO questionDTO = questionService.getById(id);
        //根据tag字段查询相关的问题
        List<QuestionDTO> relateQuestions =questionService.selectRelated(questionDTO);
        //获取当前问题的评论
        List<CommentDTO> comments  = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //更新阅读数
        questionService.incView(id);
        model.addAttribute("relateQuestions",relateQuestions);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        return "question";
    }
}
