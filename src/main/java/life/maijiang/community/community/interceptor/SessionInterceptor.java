package life.maijiang.community.community.interceptor;

import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.User;
import life.maijiang.community.community.model.UserExample;
import life.maijiang.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //        获取Cookie中的值
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
//                    获取cookie里面的token值
                    String token = cookie.getValue();
//                    在数据库中查询是否有cookie里面的token
                    //使用的是mybatis-generator的方法
                    UserExample userexample = new UserExample();
                    userexample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users =  userMapper.selectByExample(userexample);
                    //使用的是自己的mapper
//                    User user = userMapper.findByToken(token);
                    if (users.size() != 0) {
//                        在session中设置session值
                        request.getSession().setAttribute("user", users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
