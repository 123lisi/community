/**
 *点击发送ajax，提交评论内容
 **/
function post() {
    var question_id = $("#question_id").val();
    var textarea = $("#textarea").val();
    comment2target(question_id, 1, textarea);
}

// console.log(question_id,textarea);
function comment2target(targetId, type, content) {
    if (!textarea) {
        alert("不能回复空内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (data) {
            if (data.code == 200) {
                // $("#comment_section").hide();
                window.location.reload();
            } else {
                if (data.code == 2003) {
                    var isAccepted = confirm(data.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=f8ab6eb4b048a8260e31&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "isclose");
                    }
                } else {
                    alert(data.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 张展开二级评论
 **/
function collapseComment(e) {
    var id = e.getAttribute("data-id");
    // console.log(id)
    var comments = $("#comment-" + id);
    //获取二级评论的状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass('in');
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass('in');
            e.setAttribute('data-collapse', 'in');
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaBodyElement = $("<div/>", {
                        class: "media-body"
                    }).append($('<h5/>',{
                        class: "media-heading",
                        "html": comment.user.name
                    }))
                        .append($("<div/>", {
                            "html":comment.content
                        })).append($("<div/>", {
                            class: "menu"
                        }).append($("<span/>", {
                            class: "pull-right",
                            "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                        })));
                    var avatarElement = $("<img/>",{
                        class: "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    });
                    var mediaLeftElement = $("<div/>", {
                        class: "media-left"
                    }).append(avatarElement);
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                        // html: comment.content
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass('in');
                e.setAttribute('data-collapse', 'in');
                e.classList.add("active");
            });
        }

    }
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $('#tag').val();
    if (previous.split(',').indexOf(value) == -1) {
        if (previous){
            $('#tag').val(previous + "," +value);
        } else {
            $('#tag').val(value);
        }
    }

}
function showSelectTag() {
        $('#select-tag').show();
}