package life.maijiang.community.community.service;

import life.maijiang.community.community.dto.PaginationDTO;
import life.maijiang.community.community.dto.QuestionDTO;
import life.maijiang.community.community.exception.CustomizeErrorCode;
import life.maijiang.community.community.exception.CustomizeException;
import life.maijiang.community.community.mapper.QuestionMapper;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.Question;
import life.maijiang.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDTO list(Integer page, Integer size) {
//        创建PaginationDTO对象
        PaginationDTO paginationDTO = new PaginationDTO();
//        查询数据库的全部数据
        Integer totalCount = questionMapper.count();
        Integer totalPage;
        if (totalCount%size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
//        描写分页功能的逻辑，调用setPagination方法
//        判断页码是否为负数或者是0
        if (page<1){
            page = 1;
        }
//        判断页码是否大于总的页数
        if (page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        //        size*(page - 1)
        if (page == 0){
            page = 1;
        }
        Integer offset = size * (page - 1);
//        按页进行查询数据
        List<Question> questions = questionMapper.list(offset,size);
//        创建QuestionDTO类的对象的列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//        遍历查询到的数据
        for (Question question:questions){
//            查询是存在的Creator
           User user =  userMapper.findById(question.getCreator());
//          创建QuestionDTO对象
           QuestionDTO questionDTO = new QuestionDTO();
//           spring提供的一个方法将对应的属性赋值到对应的对象中
           BeanUtils.copyProperties(question,questionDTO);
//           将查询出来的user赋值给questionDTO中的uUser对象
           questionDTO.setUser(user);
//           向questionDTOList列表中添加数据
           questionDTOList.add(questionDTO);
       }
//        将questionDTOList列表赋值给paginationDTO中的questions属性
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //        创建PaginationDTO对象
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalPage;
        if (totalCount%size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
//        描写分页功能的逻辑，调用setPagination方法
//        判断页码是否为负数或者是0
        if (page<1){
            page = 1;
        }
//        判断页码是否大于总的页数
        if (page > totalPage){
            page = totalPage;
        }
        if (page == 0){
            page = 1;
        }
        paginationDTO.setPagination(totalPage,page);
        //        size*(page - 1)
        Integer offset = size * (page - 1);
//        按页进行查询数据
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
//        创建QuestionDTO类的对象的列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//        遍历查询到的数据
        for (Question question:questions){
//            查询是存在的Creator
            User user =  userMapper.findById(question.getCreator());
//          创建QuestionDTO对象
            QuestionDTO questionDTO = new QuestionDTO();
//           spring提供的一个方法将对应的属性赋值到对应的对象中
            BeanUtils.copyProperties(question,questionDTO);
//           将查询出来的user赋值给questionDTO中的uUser对象
            questionDTO.setUser(user);
//           向questionDTOList列表中添加数据
            questionDTOList.add(questionDTO);
        }
//        将questionDTOList列表赋值给paginationDTO中的questions属性
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question =  questionMapper.getById(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else {
            //更新
            questionMapper.update(question);
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id);
        Integer updatequestion =    question.getViewCount();
        questionMapper.incView(updatequestion +1,id);

    }
}
