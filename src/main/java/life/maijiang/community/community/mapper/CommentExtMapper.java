package life.maijiang.community.community.mapper;

import life.maijiang.community.community.model.Comment;

public interface CommentExtMapper {

    int incCommentCount(Comment record);
}