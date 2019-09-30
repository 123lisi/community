package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.PaginationDTO;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
    //    使用User的接口
    @Autowired
    private UserMapper userMapper;
    @Autowired
//    使用Question的接口
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {

//        创建PaginationDTO的对象并赋值为questionService中list的返回值
        PaginationDTO pagination = questionService.list(page, size);
//        使用spring中的Model类 设置传到页面上的值
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
