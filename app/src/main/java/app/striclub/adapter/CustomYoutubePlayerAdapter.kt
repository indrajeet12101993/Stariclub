package app.striclub.adapter

import app.striclub.R
import app.striclub.callback.InterfaceCustomYoutubeclick
import app.striclub.constants.AppConstants
import app.striclub.pojo.AllPostFeed.Post
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import kotlinx.android.synthetic.main.custom_youtube_layout.view.*

class CustomYoutubePlayerAdapter(private val cityList: MutableList<Post>, private val listner: InterfaceCustomYoutubeclick) : RecyclerView.Adapter<CustomYoutubePlayerAdapter.ViewHolder>() {
    //this method is returning the view for each item in the list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomYoutubePlayerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_youtube_layout, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomYoutubePlayerAdapter.ViewHolder, position: Int) {
        holder.bindItems(cityList[position],listner)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cityList.size

    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var readyForLoadingYoutubeThumbnail: Boolean = true
        fun bindItems(result: Post, listner: InterfaceCustomYoutubeclick) {
            if(readyForLoadingYoutubeThumbnail) {
                readyForLoadingYoutubeThumbnail= false
               itemView.video_thumbnail_image_view.initialize(AppConstants.KEY, object : YouTubeThumbnailView.OnInitializedListener {
                    override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView, youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                        //when initialization is sucess, set the video id to thumbnail to load
                        youTubeThumbnailLoader.setVideo(result.link)

                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                            override fun onThumbnailLoaded(youTubeThumbnailView: YouTubeThumbnailView, s: String) {
                                //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                                youTubeThumbnailLoader.release()
                            }

                            override fun onThumbnailError(youTubeThumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                                //print or show error when thumbnail load failed

                            }
                        })
                        readyForLoadingYoutubeThumbnail = true;
                    }

                    override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView, youTubeInitializationResult: YouTubeInitializationResult) {
                        //print or show error when initialization failed

                        readyForLoadingYoutubeThumbnail = true;
                    }
                })
            }


            itemView. tv_video_titile.text=result.title
            itemView .tv_video_by.text="By"+" "+result.username
           // itemView. tv_video_description.text= Html.fromHtml(result!!.news_content)
            itemView.setOnClickListener {
                listner.itemclick(result)

            }

        }
    }
}