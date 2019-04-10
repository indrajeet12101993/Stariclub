package app.striclub.callback

import app.striclub.pojo.allCommentListForNews.CommentDetail

interface InterfaceCommentForNewsSelectListner {
    fun postDelete(commentDetail: CommentDetail, s: String)
}