package app.striclub.callback

import app.striclub.pojo.listnews.Result

interface InterfaceAllNewsFeedSelectListner {

    fun userPostSelectReadMore(post: Result)
    fun userPostSelectPostLike(post: Result, like: String)
    fun userPostSelectWhatsApp(post:Result,position:Int)
    fun userPostSelectComment(post:Result,position:Int)
    fun userPostSelectSikayat(post:Result)
    fun userPostSelectfacebook(post: Result,position: Int)
    fun userPostSelectSavedPost(post: Result,position: Int,type:String)
}