package life.maijiang.community.community.controller;

import life.maijiang.community.community.dto.AccessTokenDTO;
import life.maijiang.community.community.dto.GithubUser;
import life.maijiang.community.community.provider.GithubProvider;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    public  String callback(@RequestParam(name="code") String code,
                            @RequestParam(name = "state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id("f8ab6eb4b048a8260e31");
        accessTokenDTO.setClient_secret("f3be72ca71ff9ec4e9be852fd5711dfb31a06453");
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedrect_uri("http://localhost:8887/callback");
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getuser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
