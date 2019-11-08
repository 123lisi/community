package life.maijiang.community.community.service;

import life.maijiang.community.community.dto.NotificationDTO;
import life.maijiang.community.community.dto.PaginationDTO;
import life.maijiang.community.community.dto.QuestionDTO;
import life.maijiang.community.community.enums.NotificationStatusEnum;
import life.maijiang.community.community.enums.NotificationTypeEnum;
import life.maijiang.community.community.exception.CustomizeErrorCode;
import life.maijiang.community.community.exception.CustomizeException;
import life.maijiang.community.community.mapper.NotificationMapper;
import life.maijiang.community.community.mapper.UserMapper;
import life.maijiang.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        //        创建PaginationDTO对象
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //自己写的分页
        //Integer totalCount = questionMapper.countByUserId(userId);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
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
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        return paginationDTO;

    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
