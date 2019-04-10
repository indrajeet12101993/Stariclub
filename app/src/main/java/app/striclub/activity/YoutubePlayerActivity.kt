package app.striclub.activity


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_youtube_player.*
import app.striclub.R
import app.striclub.adapter.CustomYoutubePlayerAdapter
import app.striclub.base.BaseApplication
import app.striclub.callback.InterfaceCustomYoutubeclick
import app.striclub.dataprefence.DataManager
import app.striclub.networkUtils.ApiRequestClient
import app.striclub.pojo.AllPostFeed.Post
import app.striclub.pojo.AllPostFeed.ResponseFromServerAllfeed
import app.striclub.pojo.ResponseFromServerSikayat
import app.striclub.pojo.ResponseFromServerWhatsApp
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import app.striclub.adapter.CustomAdapterSteps
import app.striclub.adapter.CustomIngredientsAdapter
import app.striclub.pojo.stepAndIngriedients.ResponseFromServerIngriendts
import com.bumptech.glide.Glide


class YoutubePlayerActivity : YouTubeBaseActivity(), InterfaceCustomYoutubeclick {
    override fun itemclick( post: Post) {

        button_toggle.visibility=View.GONE
        linear_step.visibility=View.GONE

        youtubeplayer!!.release()
        video= post?.link.toString()
        initial()
        youtube_player.initialize(id, youtubePlayerInit)
        setsocialdata(post)


    }



    private var mCompositeDisposable: CompositeDisposable? = null
    private var mCompositeDisposable_Like: CompositeDisposable? = null
    private var mCompositeDisposable_WhatsApp: CompositeDisposable? = null
    private var mCompositeDisposable_SiKayat: CompositeDisposable? = null
    private var mCompositeDisposable_Facebook: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    val id: String = "AIzaSyAeygVuWu26XAOibEKmW-GTiSlRLT3vDTA"
    var video: String? = null
    var post: Post?=null
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    private var updateLike: Int? = null
    private var post_id_whatsApp: String? = null
    private var callbackManager: CallbackManager? = null
    var fb_share_button: ShareButton? = null

    var shareDialog: ShareDialog? = null
    var youtubeplayer: YouTubePlayer?=null

    // lateinit var youtubePlayerView : YouTubePlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_player)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        fb_share_button = fb_share_button1
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)
        shareDialog?.registerCallback(callbackManager, callback)

        post =  intent.getSerializableExtra("samachardata") as Post
        video= post?.link.toString()
        initial()

        youtube_player.initialize(id, youtubePlayerInit)

        iv_back.setOnClickListener {
            finish()
        }

        setsocialdata(post!!)
        getAllUserFeed(dataManager.getUserId(),post!!.category,post!!.post_id)
        rv_more_videos.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv_ingredients.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv_steps.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rv_ingredients.isNestedScrollingEnabled = false
        rv_steps.isNestedScrollingEnabled = false

        getAllCurrentPost()
    }

    private fun setsocialdata(post: Post) {
        if(post!!.news_content=="2"){
            button_toggle.visibility=View.INVISIBLE
        }
        else{
            button_toggle.visibility=View.VISIBLE
          //  expandableTextView.visibility=View.VISIBLE

          //  linear_step
           // expandableTextView.text=Html.fromHtml(post!!.news_content)
        }
        // or set them separately

        button_toggle.setOnClickListener{

            if(linear_step.visibility==View.VISIBLE){
                button_toggle.setText(R.string.hide_recepies)
                linear_step.visibility=View.GONE
                button_toggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0)
            }else{
                button_toggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0)
                button_toggle.setText(R.string.read_recipies)
                linear_step.visibility=View.VISIBLE
            }

        }

        tv_title.text=post.title
        if(!post.uimage.isEmpty())
        Glide.with(this).load(post.uimage).into(imgView_proPic)
        tv_name.text=post.username

        // like functionality
        tv_total_like.text = post!!.no_like
        if (post!!.Isliked.equals("0")) {
            iv_like_first.visibility = View.VISIBLE
            iv_like_second.visibility = View.INVISIBLE
        } else {
            iv_like_second.visibility = View.VISIBLE
            iv_like_first.visibility = View.INVISIBLE
        }
        updateLike = post!!.no_like.toInt()

        linear_like.setOnClickListener {

            if (iv_like_first.visibility == View.VISIBLE) {
                iv_like_second.visibility = View.VISIBLE
                iv_like_first.visibility = View.INVISIBLE
                val like: Int = updateLike!! + 1
                val Totallike: String = like.toString()
                tv_total_like.text = Totallike
                getAlltotalLike(post!!, "1")
            } else {
                iv_like_second.visibility = View.INVISIBLE
                iv_like_first.visibility = View.VISIBLE

                tv_total_like.text = updateLike.toString()
                getAlltotalLike(post!!, "0")
            }


        }
        //whatsapp share
        tv_total_whattsapp.text = post!!.no_share
        linear_whatsapp.setOnClickListener {


            post_id_whatsApp = post!!.post_id
            val appplaystore: String = "https://play.google.com/store/apps/details?id=in.striclub"
            val sendIntent = Intent()
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    post.title + "\n" + "देखें striclub ऍप पर, अभी फ्री डाउनलोड करे" +
                            "\n" + appplaystore)
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.whatsapp")

            try {
                startActivityForResult(sendIntent, 1);
            } catch (ex: android.content.ActivityNotFoundException) {
                showSnackBar("Whatsapp have not been installed.")

            }

        }
        //comment
        tv_total_cooment.text = post!!.no_comment
        linear_comment.setOnClickListener {

            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("post_id", post!!.post_id)
            startActivity(intent)

        }


        //facebook
        tv_total_facebook.text = post!!.no_fb_share
        linear_facebook.setOnClickListener {
            post_id_whatsApp = post!!.post_id

            val appplaystore: String = "https://play.google.com/store/apps/details?id=in.striclub"
            if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                shareDialog?.registerCallback(callbackManager, callback)

                val linkContent = ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(appplaystore))
                        .setQuote(post!!.headline)
                        .build()
                shareDialog!!.show(linkContent)

            }

        }


    }

    // api call for user registration
    private fun getAllUserFeed(argument: String?, category: String, post_id: String) {

       // showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(ApiRequestClient.createREtrofitInstance()
                .getUserAllHomeFeed4(dataManager.getUserId(),category,post_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }


    // handle sucess response of api call
    private fun handleResponse(responseFromServerAllfeed: ResponseFromServerAllfeed) {
     //   hideDialogLoading()
        if (responseFromServerAllfeed.post.size == 0) {


        } else {
//            mUserAllFeedList?.clear()
//            mUserAllFeedList?.addAll()
//            mALlFeedVideoAdapter?.notifyDataSetChanged()

            val customYoutubePlayerAdapter = CustomYoutubePlayerAdapter(responseFromServerAllfeed.post, this )
            rv_more_videos.adapter = customYoutubePlayerAdapter


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError(error: Throwable) {
        mCompositeDisposable?.clear()
      //  hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    private val callback = object : FacebookCallback<Sharer.Result> {


        override fun onSuccess(result: Sharer.Result) {

            //Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()

            getTotalFacebookLike()
        }

        override fun onCancel() {
//
            //Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()

        }

        override fun onError(error: FacebookException) {
            // Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()


        }
    }
    private fun getTotalFacebookLike() {

        //  showDialogLoading()
        mCompositeDisposable_Facebook = CompositeDisposable()
        mCompositeDisposable_Facebook?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsApp(post_id_whatsApp, "fb")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_facebook, this::handleError_facebook))
    }


    // handle sucess response of api call
    private fun handleResponse_facebook(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {

        val item = responseFromServerWhatsApp.post_id
        val result = responseFromServerWhatsApp.result
        tv_total_facebook.text = result
        mCompositeDisposable_Facebook?.clear()


    }


    // handle failure response of api call
    private fun handleError_facebook(error: Throwable) {
        mCompositeDisposable_Facebook?.clear()

        showSnackBar(error.localizedMessage)


    }



    // api call for user registration
    private fun postSikayat(post_id: String, sikayat: String) {


        mCompositeDisposable_SiKayat = CompositeDisposable()
        mCompositeDisposable_SiKayat?.add(ApiRequestClient.createREtrofitInstance()
                .postSikayat(post_id, sikayat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Sikayat, this::handleError_Sikayat))
    }


    // handle sucess response of api call
    private fun handleResponse_Sikayat(responseFromServerSikayat: ResponseFromServerSikayat) {

        mCompositeDisposable_SiKayat?.clear()


    }


    // handle failure response of api call
    private fun handleError_Sikayat(error: Throwable) {
        mCompositeDisposable_SiKayat?.clear()

        showSnackBar(error.localizedMessage)


    }

    // api call for user registration
    private fun getAlltotalLike(post: Post, like: String) {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()

        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLike(dataManager.getUserId(), post.post_id, like)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Like, this::handleError_like))
    }


    // handle sucess response of api call
    private fun handleResponse_Like(responseFromServerAllfeed: ResponseFromServerAllfeed) {
        //   hideDialogLoading()
        //  mUserAllFeedList?.clear()
        // mUserAllFeedList?.addAll(responseFromServerAllfeed.post)

        //  mALlFeedVideoAdapter?.notifyDataSetChanged()
        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_like(error: Throwable) {


        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }


    private fun initial() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
              //  p1?.cueVideo(video)
                youtubeplayer=p1
                youtubeplayer?.loadVideo(video)
                //  p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                getTotalWhatsAppLike()
                // Toast.makeText(activity, "Got Callback yeppeee...:", Toast.LENGTH_SHORT).show()
            }
        } else {
            //Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTotalWhatsAppLike() {

        //  showDialogLoading()
        mCompositeDisposable_WhatsApp = CompositeDisposable()

        mCompositeDisposable_WhatsApp?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsApp(post_id_whatsApp, "wp")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_whatsApp, this::handleError_whatsApp))
    }


    // handle sucess response of api call
    private fun handleResponse_whatsApp(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {

        val item = responseFromServerWhatsApp.post_id
        val result = responseFromServerWhatsApp.result
        tv_total_whattsapp.text = result
        mCompositeDisposable_WhatsApp?.clear()


    }


    // handle failure response of api call
    private fun handleError_whatsApp(error: Throwable) {
        mCompositeDisposable_WhatsApp?.clear()

        showSnackBar(error.localizedMessage)


    }

    fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(findViewById<View>(android.R.id.content),
                message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView
                .findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.show()
    }
    private fun getAllCurrentPost() {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()
        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getCurrentPost(dataManager.getUserId(), post!!.post_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Cuurent_post, this::handleError_current_post))
    }


    // handle sucess response of api call
    private fun handleResponse_Cuurent_post(response: ResponseFromServerIngriendts) {
        mCompositeDisposable_Like?.clear()


        if (response.ingredients.size > 0) {
            val adapter = CustomIngredientsAdapter(response.ingredients)

            //now adding the adapter to recyclerview
            rv_ingredients.adapter = adapter
        }
        if (response.steps.size > 0) {
            val adapter = CustomAdapterSteps(response.steps)

            //now adding the adapter to recyclerview
            rv_steps.adapter = adapter
        }

    }


    // handle failure response of api call
    private fun handleError_current_post(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }



}
