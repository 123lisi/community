package life.maijiang.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请先登录后重试"),
    SYS_ERROR(2004,"当前操作需要重试"),
    TYPE_PARAM_WEING(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在"),
    COMMENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009,"消息已经是不存在了"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    ;
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code,String message ) {
        this.message = message;
        this.code = code;
    }

}
