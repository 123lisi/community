package life.maijiang.community.community.dto;

import com.alibaba.fastjson.JSONObject;
import life.maijiang.community.community.exception.CustomizeErrorCode;
import life.maijiang.community.community.exception.CustomizeException;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@Data

public class ResultDTO<T> {
    private Integer code;
    private T data ;
    private String message;
    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }
    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }
    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
