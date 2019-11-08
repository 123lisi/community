package life.maijiang.community.community.cache;

import life.maijiang.community.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","node"));
        tagDTOS.add(program);
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring-boot","springMVC","SSH","Django"));
        tagDTOS.add(framework);
        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux","Nginx","Docker"));
        tagDTOS.add(server);
        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("Mysql","Redis","mongodb","sql"));
        tagDTOS.add(db);
        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","vim","idea"));
        tagDTOS.add(tool);
        return  tagDTOS;
    }
    public static String isValid(String tags){
        String[] split = StringUtils.split(tags, ',');
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
