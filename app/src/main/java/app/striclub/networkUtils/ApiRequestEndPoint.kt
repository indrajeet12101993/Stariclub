package app.striclub.networkUtils

import io.reactivex.Observable
import app.striclub.pojo.otp.ResponseFromServerOtpVerify
import okhttp3.RequestBody
import retrofit2.http.*
import app.striclub.pojo.city.ResponseFromServerCity
import app.striclub.pojo.state.ResponseFromServerStateList
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import app.striclub.pojo.*
import app.striclub.pojo.AllCommentList.ResponseFromServerAllCommentList
import app.striclub.pojo.AllPostFeed.ResponseFromServerAllfeed
import app.striclub.pojo.UserEditShowInitial.ResponseFromServerInitialEditUser
import app.striclub.pojo.hashTagTrending.ResponseFromServerHashTag
import app.striclub.pojo.listBlog.ResponseFromServerListBlog
import app.striclub.pojo.listnews.ServerFromResponseListNews
import app.striclub.pojo.searchsuggestion.ResponseFromServerSearchSuggestions
import app.striclub.pojo.stepAndIngriedients.ResponseFromServerIngriendts
import app.striclub.pojo.termsandconditions.ResponseFromServerTermsAndConditions


interface ApiRequestEndPoint {

    @FormUrlEncoded
    @POST("Api/login")
    fun postServerUserPhoneNumber(@Field("phone") phone: String): Observable<ResponseFromSerevrPhoneNumber>

    @FormUrlEncoded
    @POST("Api/otpcheck")
    fun postServerUserOtpVerify(@Field("otp") otp: String): Observable<ResponseFromServerOtpVerify>

    @FormUrlEncoded
    @POST("Api/user")
    fun postServerUserDetailsWithCity(@Field("user_id") user_id: String?,
                                      @Field("name") name: String,
                                      @Field("state") state: String,
                                      @Field("city") city: String,
                                      @Field("village") village: String): Observable<ResponseFromServerForUserRegistartion>


    @Multipart
    @POST("Api/profile")
    fun uploadImage(@Part image: MultipartBody.Part, @Part("user_id") user_id: RequestBody): Observable<ServerResponseFromUplaodImage>

    @FormUrlEncoded
    @POST("Api/removeprofile")
    fun removeImage(@Field("user_id") user_id: String?): Observable<ServerResponseFromUplaodImage>

    @FormUrlEncoded
    @POST("Api/updateuser")
    fun uploadUpadteUserProfile(@Field("user_id") user_id: String?,
                                @Field("name") name: String,
                                @Field("state") state: String,
                                @Field("city") city: String,
                                @Field("village") village: String): Observable<ServerResponseFromUplaodImage>

    @FormUrlEncoded
    @POST("Api/sujhaode")
    fun userSujhao(@Field("user_id") user_id: String?,
                                @Field("name") name: String,
                                @Field("state") state: String,
                                @Field("city") city: String,
                                @Field("comment") comment: String,
                                @Field("village") village: String): Observable<ServerResponseFromUplaodImage>

//    @GET("login.php?")
//    fun getLogin(@Query("service") service: String, @Query("user_name")
//    user_name: String, @Query("pwd") pwd: String): Observable<ResponseFromServer>


    @FormUrlEncoded
    @POST("Api/city")
    fun getCityList(@Field("state_id") state_id: String): Observable<ResponseFromServerCity>

    @FormUrlEncoded
    @POST("Api/listvideo")
    fun getUserAllPostFeed(@Field("user_id") user_id: String?,
                           @Field("post_id") post_id: String?): Observable<ResponseFromServerAllfeed>

    @FormUrlEncoded
    @POST("Api/News")
    fun getUserAllNewsFeed(@Field("user_id") user_id: String?): Observable<ServerFromResponseListNews>

    @FormUrlEncoded
    @POST("Api/recipepost")
    fun getUserAllHomeFeed(@Field("user_id") user_id: String?, @Field("filter") name: String?): Observable<ResponseFromServerAllfeed>
    @FormUrlEncoded
    @POST("Api/listpost")
    fun getUserAllHomeFeed2(@Field("user_id") user_id: String?): Observable<ResponseFromServerAllfeed>

    @FormUrlEncoded
    @POST("Api/relatedpost")
    fun getUserAllHomeFeed4(@Field("user_id") user_id: String?, @Field("filter") name: String?, @Field("post_id") post_id: String?): Observable<ResponseFromServerAllfeed>
    @FormUrlEncoded
    @POST("Api/beautypost")
    fun getUserAllHomeFeed1(@Field("user_id") user_id: String?): Observable<ResponseFromServerAllfeed>

    @FormUrlEncoded
    @POST("Api/Blog")
    fun getUserAllBlogFeed(@Field("user_id") user_id: String?,
                           @Field("blog_id") blog_id: String?): Observable<ResponseFromServerListBlog>


    @FormUrlEncoded
    @POST("Api/savepost")
    fun getUserAllSavedPostFeed(@Field("user_id") user_id: String?): Observable<ResponseFromServerAllfeed>

    @FormUrlEncoded
    @POST("Api/edituser")
    fun getUserInitialEditData(@Field("user_id") user_id: String?): Observable<ResponseFromServerInitialEditUser>

    @FormUrlEncoded
    @POST("Api/sharecount")
    fun getsharecountWhatsApp(@Field("post_id") post_id: String?, @Field("type") type: String?): Observable<ResponseFromServerWhatsApp>

    @FormUrlEncoded
    @POST("Api/blogsharecount")
    fun getsharecountWhatsAppForlekh(@Field("blog_id") post_id: String?, @Field("type") type: String?): Observable<ResponseFromServerWhatsApp>
    @FormUrlEncoded
    @POST("Api/Newsharecount")
    fun getsharecountWhatsAppForSamachar(@Field("news_id") post_id: String?, @Field("type") type: String?): Observable<ResponseFromServerWhatsApp>

    @FormUrlEncoded
    @POST("Api/comment_post")
    fun getToatalComment(@Field("post_id") post_id: String?): Observable<ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/news_comment_post")
    fun getToatalCommentForNews(@Field("news_id") post_id: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/blog_comment_post")
    fun getToatalCommentForLekh(@Field("blog_id") post_id: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/suggestions")
    fun getSearchSuggestions(@Field("name") name: String?): Observable<ResponseFromServerSearchSuggestions>

    @FormUrlEncoded
    @POST("Api/Deletecomment")
    fun getDeleteComment(@Field("comment_id") comment_id: String?,
                         @Field("post_id") post_id: String?): Observable<ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/news_Deletecomment")
    fun getDeleteCommentForNews(@Field("comment_id") comment_id: String?,
                         @Field("news_id") post_id: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/blog_Deletecomment")
    fun getDeleteCommentForLekh(@Field("comment_id") comment_id: String?,
                                @Field("blog_id") blog_id: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>

    @FormUrlEncoded
    @POST("Api/post_likes")
    fun getTotalLike(@Field("user_id") user_id: String?, @Field("post_id") post_id: String?,
                     @Field("value") value: String?): Observable<ResponseFromServerAllfeed>

    @FormUrlEncoded
    @POST("Api/blogpost_likes")
    fun getTotalLikeForLekh(@Field("user_id") user_id: String?, @Field("blog_id") blog_id: String?,
                            @Field("value") value: String?): Observable<ResponseFromServerListBlog>
    @FormUrlEncoded
    @POST("Api/currentpost")
    fun getCurrentPost(@Field("user_id") user_id: String?,
                            @Field("post_id") value: String?): Observable<ResponseFromServerIngriendts>

    @FormUrlEncoded
    @POST("Api/newspost_likes")
    fun getTotalLikeForSamchar(@Field("user_id") user_id: String?, @Field("news_id") blog_id: String?,
                               @Field("value") value: String?): Observable<ServerFromResponseListNews>


    @FormUrlEncoded
    @POST("Api/add_comment")
    fun postComment(@Field("post_id") post_id: String?, @Field("user_id") user_id: String?,
                    @Field("comment") comment: String?): Observable<ResponseFromServerAllCommentList>
    @FormUrlEncoded
    @POST("Api/news_add_comment")
    fun postCommentForSmachar(@Field("news_id") post_id: String?, @Field("user_id") user_id: String?,
                    @Field("comment") comment: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>

    @FormUrlEncoded
    @POST("Api/blog_add_comment")
    fun postCommentForLekh(@Field("blog_id") post_id: String?, @Field("user_id") user_id: String?,
                              @Field("comment") comment: String?): Observable<app.striclub.pojo.allCommentListForNews.ResponseFromServerAllCommentList>

    @GET("Api/state")
    fun getStateList(): Observable<ResponseFromServerStateList>

    @GET("Api/Terms")
    fun getTerms(): Observable<ResponseFromServerTermsAndConditions>

    @GET("Api/Trending")
    fun getHashTagList(): Observable<ResponseFromServerHashTag>

    @GET("Api/hashtag")
    fun getTrendingList(): Observable<ResponseFromServerHashTag>


    @FormUrlEncoded
    @POST("Api/Report")
    fun postSikayat(@Field("post_id") post_id: String?,
                    @Field("comment") comment: String?): Observable<ResponseFromServerSikayat>


    @FormUrlEncoded
    @POST("Api/Addsavedpost")
    fun postSavedPostFeed(@Field("user_id") user_id: String?,
                          @Field("post_id") post_id: String?, @Field("type") type: String): Observable<ResponseFromSerVerAddPostList>
}

