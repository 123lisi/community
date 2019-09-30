package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.PaginationDTO;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.User;
import life.maijiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          Model model){
       User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redict:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","我的最新回复");
        }
//        System.out.println("user.getId()"+user.getId());
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
//        System.out.println(paginationDTO);
        model.addAttribute("pagination",paginationDTO );
        return "profile";
    }
}
