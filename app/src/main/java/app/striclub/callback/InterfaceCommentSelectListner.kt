package app.striclub.callback

import app.striclub.pojo.AllCommentList.CommentDetail

interface InterfaceCommentSelectListner {

    fun postDelete(commentDetail: CommentDetail, s: String)
}