package life.maijiang.community.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String account;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
