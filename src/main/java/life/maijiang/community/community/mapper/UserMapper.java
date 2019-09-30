package life.maijiang.community.community.mapper;

import life.maijiang.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,accunt_id,token,gmt_create,gmt_modified,avatar_url)  values (#{name},#{account},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer creator);
    @Select("select * from user where accunt_id = #{account}")
    User findByAccountId(@Param("account") String account);

    @Update("Update user set name = #{name},token = #{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl}")
    void update(User user);
}
