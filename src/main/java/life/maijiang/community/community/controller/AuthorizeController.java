package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.AccessTokenDTO;
import life.maijiang.community.community.dto.GithubUser;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.User;
import life.maijiang.community.community.provider.GithubProvider;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/callback")
    public  String callback(@RequestParam(name="code") String code,
                            @RequestParam(name = "state") String state,
                            HttpServletRequest request,
                            HttpServletResponse respons){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id("f8ab6eb4b048a8260e31");
        accessTokenDTO.setClient_secret("f3be72ca71ff9ec4e9be852fd5711dfb31a06453");
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedrect_uri("http://localhost:8887/callback");
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getuser(accessToken);
        System.out.println(githubUser.getAvatarUrl());
        if(githubUser != null){

            User user = new User();
            user.setName(githubUser.getName());
            user.setAccount(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userMapper.insert(user);
//            把token写入cookie里面用于验证
            respons.addCookie(new Cookie("token",token));
            //            登录成功 写cookie和session
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else{
//            登录失败
            System.out.println("登录失败");
            return "rediret:/";
        }
    }
}
