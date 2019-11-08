package life.maijiang.community.community.service;

import life.maijiang.community.community.dto.PaginationDTO;
import life.maijiang.community.community.dto.QuestionDTO;
import life.maijiang.community.community.dto.QuestionQueryDTO;
import life.maijiang.community.community.exception.CustomizeErrorCode;
import life.maijiang.community.community.exception.CustomizeException;
import life.maijiang.community.community.mapper.QuestionExtMapper;
import life.maijiang.community.community.mapper.QuestionMapper;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.Question;
import life.maijiang.community.community.model.QuestionExample;
import life.maijiang.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(String search, Integer page, Integer size) {
//        String[] tags = new String[0];
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            List<String> stringList = Arrays.stream(tags).filter(tag -> StringUtils.isNotBlank(tag)).collect(Collectors.toList());
            search = stringList.stream().collect(Collectors.joining("|"));
        }else if (search == ""){
            search = null;
        }
//        创建PaginationDTO对象
        PaginationDTO paginationDTO = new PaginationDTO();
//        查询数据库的全部数据
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
//        描写分页功能的逻辑，调用setPagination方法
//        判断页码是否为负数或者是0
        if (page < 1) {
            page = 1;
        }
//        判断页码是否大于总的页数
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        //        size*(page - 1)
        if (page == 0) {
            page = 1;
        }
        Integer offset = size * (page - 1);
//        按页进行查询数据
        //List<Question> questions = questionMapper.list(offset, size);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
//        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
//        创建QuestionDTO类的对象的列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//        遍历查询到的数据
        for (Question question : questions) {
//            查询是存在的Creator
            User user = userMapper.selectByPrimaryKey(question.getCreator());
//          创建QuestionDTO对象
            QuestionDTO questionDTO = new QuestionDTO();
//           spring提供的一个方法将对应的属性赋值到对应的对象中
            BeanUtils.copyProperties(question, questionDTO);
//           将查询出来的user赋值给questionDTO中的uUser对象
            questionDTO.setUser(user);
//           向questionDTOList列表中添加数据
            questionDTOList.add(questionDTO);
        }
//        将questionDTOList列表赋值给paginationDTO中的questions属性
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        //        创建PaginationDTO对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //自己写的分页
        //Integer totalCount = questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
//        描写分页功能的逻辑，调用setPagination方法
//        判断页码是否为负数或者是0
        if (page < 1) {
            page = 1;
        }
//        判断页码是否大于总的页数
        if (page > totalPage) {
            page = totalPage;
        }
        if (page == 0) {
            page = 1;
        }
        paginationDTO.setPagination(totalPage, page);
        //        size*(page - 1)
        Integer offset = size * (page - 1);
//        按页进行查询数据
        //List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
//        创建QuestionDTO类的对象的列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();
//        遍历查询到的数据
        for (Question question : questions) {
//            查询是存在的Creator
            User user = userMapper.selectByPrimaryKey(question.getCreator());
//          创建QuestionDTO对象
            QuestionDTO questionDTO = new QuestionDTO();
//           spring提供的一个方法将对应的属性赋值到对应的对象中
            BeanUtils.copyProperties(question, questionDTO);
//           将查询出来的user赋值给questionDTO中的uUser对象
            questionDTO.setUser(user);
//           向questionDTOList列表中添加数据
            questionDTOList.add(questionDTO);
        }
//        将questionDTOList列表赋值给paginationDTO中的questions属性
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);


//        Question question = questionMapper.getById(id);
//        Integer updatequestion = question.getViewCount();
//        questionMapper.incView(updatequestion + 1, id);

    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        List<String> stringList = Arrays.stream(tags).filter(t -> !StringUtils.isBlank(t)).collect(Collectors.toList());
        String regexpTag = stringList.stream().collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
//        System.out.println(questions);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
