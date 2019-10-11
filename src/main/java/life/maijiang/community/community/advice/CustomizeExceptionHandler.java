package life.maijiang.community.community.advice;


import life.maijiang.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model) {
        if (ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else {
            model.addAttribute("message","您好您访问的页面不存在或者移交到其他页面上了");
        }

        return new ModelAndView("error");
    }


}
