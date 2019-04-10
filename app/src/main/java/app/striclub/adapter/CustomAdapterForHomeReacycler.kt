package app.striclub.adapter

import android.support.v7.widget.RecyclerView
import android.text.Html

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide

import app.striclub.R
import app.striclub.callback.IFragmentManager
import app.striclub.callback.InterFaceAllfeedVideoSelectListner
import app.striclub.holder.VideoViewHolder
import app.striclub.holder.ViewHolderLekh
import app.striclub.holder.ViewHolderSaAMchar
import app.striclub.pojo.AllPostFeed.Post
import app.striclub.utils.TimeUtils
import kotlinx.android.synthetic.main.custom_layout_home_fragment_recyclerview.view.*



class CustomAdapterForHomeReacycler(val userFeedList: MutableList<Post>, private val listner: InterFaceAllfeedVideoSelectListner, var fragmentManager: IFragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val RECIPIEIMAGE: Int = 120
    private val RECIPIEVIDEO: Int = 121
    private val BEAUTYIMAGE: Int = 122
    private val BEAUTYVIDEO: Int = 123

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView: View
        if (viewType == RECIPIEIMAGE) {

            itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_home_fragment_recyclerview, parent, false)
            return ViewHolderIMage(itemView)
        }
        else  if (viewType == RECIPIEVIDEO) {

            itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_video_fragment_list, parent, false)

            return VideoViewHolder(itemView)
        }

        else  if (viewType == BEAUTYIMAGE) {

       //    itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_samachar_fragemnt_recycler, parent, false)
//            return ViewHolderSaAMchar(itemView)

            itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_home_fragment_recyclerview, parent, false)
            return ViewHolderIMage(itemView)
        }
        else{

//            itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_lekh_fragment_recycler, parent, false)
//            return ViewHolderLekh(itemView)

            itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_video_fragment_list, parent, false)

            return VideoViewHolder(itemView)
        }
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
     //   holder.bindItems(userFeedList[position], listner, holder.adapterPosition)


        if (holder is ViewHolderIMage) {

            holder.bindItemsForImage(userFeedList[position], listner,holder.adapterPosition)
        }

        if (holder is ViewHolderSaAMchar) {
            holder.bindItems(userFeedList[position], listner,holder.adapterPosition)
        }
        if (holder is ViewHolderLekh) {
            holder.bindItems(userFeedList[position], listner,holder.adapterPosition)
        }


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userFeedList.size

    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder is VideoViewHolder) {
            val position = holder.adapterPosition
            val video:Post = userFeedList.get(position)
            video.binder.bind(holder, fragmentManager,listner)
        }

    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is VideoViewHolder) {
            val position = holder.adapterPosition
            if(position>0){
                val video = userFeedList.get(position)
                video.binder.unBind(holder, fragmentManager,listner)
            }


        }


    }




    override fun getItemViewType(position: Int): Int {


        //getting message object of current position
        val post = userFeedList.get(position)


        return if (post.post_type.equals("0")) {
            RECIPIEIMAGE
        } else if (post.post_type.equals("1")) {
            RECIPIEVIDEO
        } else if (post.post_type.equals("2")) {
            BEAUTYIMAGE
        } else {
            BEAUTYVIDEO
        }

    }

    class ViewHolderIMage(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var updateLike: Int? = null
        var like: Int? = null

        fun bindItemsForImage(post: Post, listner: InterFaceAllfeedVideoSelectListner, adapterPosition: Int) {


            val server_time_timestamp = TimeUtils.getServerTimeStamp(post.created)
            val relativeTime = TimeUtils.getTimeAgo(server_time_timestamp)
            itemView.tv_time.text = relativeTime

            if(!post.uimage.isEmpty())
            Glide.with(itemView.context).load(post.uimage).into(itemView.imgView_proPic)

            itemView.tv_district.text = post.city
            itemView.tv_city.text = post.state

            if(post.username.isEmpty()){
                itemView.textViewUsername.text  = "Striclub member"
            }else{
                itemView.textViewUsername.text = post.username
            }

            itemView.tv_headline.text = post.title


            //itemView.tv_content.visibility = View.VISIBLE
            itemView.tv_content.text = post.title
            itemView.tv_content_long.text = Html.fromHtml(post.news_content)
//            itemView.tv_content.setOnClickListener {
//
//                if (itemView.tv_content.visibility == View.VISIBLE) {
//                    itemView.tv_content_long.visibility = View.VISIBLE
//                    itemView.tv_content_long.text = Html.fromHtml(post.news_content)
//                    itemView.tv_content.visibility = View.INVISIBLE
//                } else {
//                    itemView.tv_content_long.visibility = View.INVISIBLE
//                    itemView.tv_content.text = Html.fromHtml(post.news_content)
//                    itemView.tv_content.visibility = View.VISIBLE
//                }
//
//            }



            // click on linar like
            itemView.tv_total_like.text = post.no_like
            if (post.Isliked.equals("0")) {
                itemView.iv_like_first.visibility = View.VISIBLE
                itemView.iv_like_second.visibility = View.INVISIBLE
            } else {
                itemView.iv_like_second.visibility = View.VISIBLE
                itemView.iv_like_first.visibility = View.INVISIBLE
            }
            updateLike = post.no_like.toInt()

            itemView.linear_like.setOnClickListener {
                if (post.Isliked.equals("1")) {
                    updateLike = post.no_like.toInt()
                    if (itemView.iv_like_second.visibility == View.VISIBLE) {
                        itemView.iv_like_second.visibility = View.INVISIBLE
                        itemView.iv_like_first.visibility = View.VISIBLE
                        like = updateLike!! - 1
                        val Totallike: String = like.toString()
                        itemView.tv_total_like.text = Totallike
                        listner.userPostSelectPostLike(post, "0")
                        return@setOnClickListener
                    }
                    if (itemView.iv_like_first.visibility == View.VISIBLE) {
                        itemView.iv_like_first.visibility = View.INVISIBLE
                        itemView.iv_like_second.visibility = View.VISIBLE
                        val like1: Int = like!! + 1
                        val Totallike: String = like1.toString()
                        itemView.tv_total_like.text = Totallike
                        listner.userPostSelectPostLike(post, "1")
                        return@setOnClickListener

                    }
                }
                if (post.Isliked.equals("0")) {
                    updateLike = post.no_like.toInt()
                    if (itemView.iv_like_first.visibility == View.VISIBLE) {
                        itemView.iv_like_second.visibility = View.VISIBLE
                        itemView.iv_like_first.visibility = View.INVISIBLE
                        like = updateLike!! + 1
                        val Totallike: String = like.toString()
                        itemView.tv_total_like.text = Totallike
                        listner.userPostSelectPostLike(post, "1")
                        return@setOnClickListener
                    } else {
                        itemView.iv_like_second.visibility = View.INVISIBLE
                        itemView.iv_like_first.visibility = View.VISIBLE
                        //val updateLike  =post.no_like.toInt()-1
                        //  val like: Int = updateLike
                        //  val Totallike: String = like.toString()
                        val like1: Int = like!! - 1
                        val Totallike: String = like1.toString()
                        itemView.tv_total_like.text = updateLike.toString()
                        listner.userPostSelectPostLike(post, "0")
                        return@setOnClickListener
                    }
                }


            }
            //whatsapp share
            itemView.tv_total_whattsapp.text = post.no_share
            itemView.linear_whatsapp.setOnClickListener {
                itemView.tv_total_whattsapp.text = (post.no_share.toInt()+1).toString()
                listner.userPostSelectWhatsApp(post, adapterPosition)

            }
            //comment
            itemView.tv_total_cooment.text = post.no_comment
            itemView.linear_comment.setOnClickListener {

                listner.userPostSelectComment(post, adapterPosition)

            }
            // shikayat
            itemView.tv_shikayat.setOnClickListener {

                listner.userPostSelectSikayat(post)
            }
            //facebook
            itemView.tv_total_facebook.text = post.no_fb_share
            itemView.linear_facebook.setOnClickListener {

                itemView.tv_total_facebook.text = (post.no_fb_share.toInt()+1).toString()
                listner.userPostSelectfacebook(post, adapterPosition)
            }

            // save post
            if (post.IsSavedpost.equals("0")) {
                // when post is not save
                itemView.iv_saved_post.visibility = View.VISIBLE
                itemView.iv_saved_post_done.visibility = View.INVISIBLE
                itemView.tv_user_save.text = itemView.context.getString(R.string.savepaost)
                Glide.with(itemView.context).load(R.drawable.bookmark_icon).into(itemView.iv_saved_post)


            } else {
                // when post is saved
                itemView.iv_saved_post.visibility = View.INVISIBLE
                itemView.iv_saved_post_done.visibility = View.VISIBLE
                itemView.tv_user_save.text = itemView.context.getString(R.string.savedpaost)
                Glide.with(itemView.context).load(R.drawable.saved_icon).into(itemView.iv_saved_post_done)
            }



            itemView.tv_source.text = post.source
                Glide.with(itemView.context).load(post.news_image).into(itemView.iv_post_image)






            itemView.linear_saved_post.setOnClickListener {
                // when post is saved for if and else user don?t save the post
                if (itemView.iv_saved_post.visibility == View.VISIBLE) {
                    itemView.iv_saved_post.visibility = View.INVISIBLE
                    itemView.iv_saved_post_done.visibility = View.VISIBLE
                    itemView.tv_user_save.text = itemView.context.getString(R.string.savedpaost)
                    Glide.with(itemView.context).load(R.drawable.saved_icon).into(itemView.iv_saved_post_done)
                    listner.userPostSelectSavedPost(post, adapterPosition, "save")

                } else {
                    itemView.iv_saved_post.visibility = View.VISIBLE
                    itemView.tv_user_save.text = itemView.context.getString(R.string.savepaost)
                    itemView.iv_saved_post_done.visibility = View.INVISIBLE
                    Glide.with(itemView.context).load(R.drawable.bookmark_icon).into(itemView.iv_saved_post)
                    listner.userPostSelectSavedPost(post, adapterPosition, "remove")
                }

            }

            itemView.readmore.setOnClickListener {
                listner.userPostSelectReadMore(post)
            }
            itemView.setOnClickListener {
                listner.userPostSelectReadMore(post)
            }








        }


    }

}

