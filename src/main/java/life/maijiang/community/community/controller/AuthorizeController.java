package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.AccessTokenDTO;
import life.maijiang.community.community.dto.GithubUser;
import life.maijiang.community.community.provider.GithubProvider;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @RequestMapping("/callback")
    public  String callback(@RequestParam(name="code") String code,
                            @RequestParam(name = "state") String state,
                            HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id("f8ab6eb4b048a8260e31");
        accessTokenDTO.setClient_secret("f3be72ca71ff9ec4e9be852fd5711dfb31a06453");
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedrect_uri("http://localhost:8887/callback");
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getuser(accessToken);
//        System.out.println(user.getName());
        if(user != null){
//            登录成功 写cookie和session
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
//            登录失败
            System.out.println("登录失败");
            return "rediret:/";
        }
    }
}
