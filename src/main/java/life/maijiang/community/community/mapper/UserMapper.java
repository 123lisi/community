package life.maijiang.community.community.mapper;

import life.maijiang.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface  UserMapper {
    @Insert("insert into user (name,accunt_id,token,gmt_create,gmt_modified,avatar_url)  values (#{name},#{account},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
