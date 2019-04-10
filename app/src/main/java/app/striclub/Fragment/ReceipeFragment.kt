package app.striclub.Fragment


import app.striclub.R
import app.striclub.activity.CommentsActivity
import app.striclub.activity.HomeLekhReadMoreActivity
import app.striclub.activity.YoutubePlayerActivity
import app.striclub.adapter.CustomAdapterForCity
import app.striclub.adapter.CustomAdapterForHomeReacycler
import app.striclub.adapter.CustomAdapterForReceipes
import app.striclub.adapter.CustomAdapterForState
import app.striclub.base.BaseApplication
import app.striclub.base.BaseFragment
import app.striclub.callback.*
import app.striclub.dataprefence.DataManager
import app.striclub.networkUtils.ApiRequestClient
import app.striclub.pojo.*
import app.striclub.pojo.AllPostFeed.Post
import app.striclub.pojo.AllPostFeed.ResponseFromServerAllfeed
import app.striclub.pojo.listBlog.ResponseFromServerListBlog
import app.striclub.pojo.listnews.ServerFromResponseListNews
import app.striclub.utils.UtilityFiles
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_video.view.*
import kotlinx.android.synthetic.main.profile_dialog.view.*
import kotlinx.android.synthetic.main.receipes_catogory.*
import java.io.Serializable


class ReceipeFragment : BaseFragment(), InterfaceSelectCatogory,InterFaceAllfeedVideoSelectListner, IFragmentManager, SwipeRefreshLayout.OnRefreshListener, InterfaceStateSelectListner, InterfaceCitySelectListner {
    override fun selectRecepies(string: String) {


        buffer!!.append(string)
        buffer!!.append(",")

    }
    // item click when state is selected
    override fun onItemClickState(result: app.striclub.pojo.state.Result) {


        dialog_State.dismiss()
        state_id = result.id
        dialogView!!.et_state.setText(result.name)
        dialogView!!.et_state.requestFocus()

    }

    // item click when city is selected
    override fun onItemClickState(result: app.striclub.pojo.city.Result) {

        dialog_city.dismiss()
        city_name = result.name
        city_id = result.id
        dialogView!!.et_district.setText(result.name)
        dialogView!!.et_district.requestFocus()


    }
    override fun onRefresh() {
        if (dataForHitServer.equals("yes")) {

            getAllUserFeedForSwipe(getArgument)
        } else {
            getAllUserFeedForSwipe("")
        }
    }

    override fun userPostSelectReadMore(post: Post) {


        if (post.post_type.equals("1")) {

            val intent = Intent(activity, YoutubePlayerActivity::class.java)
            intent.putExtra("samachardata", post as Serializable)
            startActivity(intent)
        }
        if (post.post_type.equals("3")) {

            val intent = Intent(activity, YoutubePlayerActivity::class.java)
            intent.putExtra("samachardata", post as Serializable)
            startActivity(intent)
        }

        if (post.post_type.equals("0")) {


            val intent = Intent(activity, HomeLekhReadMoreActivity::class.java)
            intent.putExtra("samachardata", post as Serializable)
            startActivity(intent)
        }
        if (post.post_type.equals("2")) {


            val intent = Intent(activity, HomeLekhReadMoreActivity::class.java)
            intent.putExtra("samachardata", post as Serializable)
            startActivity(intent)
        }

    }

    override fun getSupportFragmentManager(): FragmentManager {
        return fragmentManager!!
    }

    override fun getSupportFragment(): Fragment {
        return this

    }

    override fun userPostSelectSavedPost(post: Post, position: Int, type: String) {

        postToAddSavedFeed(post.user_id, post.post_id, type)

    }

    override fun userPostSelectfacebook(post: Post, position: Int) {


        if (post.post_type.equals("0")) {
            post_id_whatsApp = post.post_id
            post_id_Position = position
            shareFaceBook(post.title)
        }
        if (post.post_type.equals("1")) {
            post_id_whatsApp = post.post_id
            post_id_Position = position

            shareFaceBook(post.title)
        }
        if (post.post_type.equals("2")) {
            iscallForNewsCount_facebook = true
            post_id_whatsApp = post.post_id
            post_id_Position = position
            id_Count_whatsApp = post.news_id
            shareFaceBook(post.title)
        }
        if (post.post_type.equals("3")) {
            iscallForLekhCount_facebook = true
            post_id_whatsApp = post.post_id
            post_id_Position = position
            id_Count_whatsApp = post.blog_id
            shareFaceBook(post.title)
        }


    }

    fun shareFaceBook(title:String) {

        val appplaystore: String = "https://play.google.com/store/apps/details?id=app.striclub"
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            shareDialog?.registerCallback(callbackManager, callback)

            val linkContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(appplaystore))
                    .setQuote(title)
                    .build()
            shareDialog!!.show(linkContent)

        }

    }


    override fun userPostSelectSikayat(post: Post) {

    }

    override fun userPostSelectComment(post: Post, position: Int) {
        iscallForCommentUpdate = true
        val intent = Intent(activity, CommentsActivity::class.java)
        intent.putExtra("post_id", post.post_id)
        startActivity(intent)


    }


    override fun userPostSelectWhatsApp(post: Post, position: Int) {

        val appplaystore: String = "https://play.google.com/store/apps/details?id=app.striclub"

        if (post.post_type.equals("0")) {
            post_id_whatsApp = post.post_id
            post_id_Position = position

            if (post.post_image.isNullOrEmpty()) {
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
            } else {
                Glide.with(activity!!).asBitmap()
                        .load(post.post_image)
                        .into(object : SimpleTarget<Bitmap>(100, 100) {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val sendIntent = Intent()
                                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                sendIntent.action = Intent.ACTION_SEND
                                sendIntent.type = "image/*"
                                sendIntent.putExtra(Intent.EXTRA_STREAM, UtilityFiles.getLocalBitmapUri(resource, activity!!))
                                sendIntent.putExtra(Intent.EXTRA_TEXT, post.title + "\n" + "देखें striclub ऍप पर, अभी फ्री डाउनलोड करे" +
                                        "\n" + appplaystore)
                                sendIntent.setPackage("com.whatsapp")

                                try {
                                    startActivityForResult(sendIntent, 1);
                                } catch (ex: android.content.ActivityNotFoundException) {
                                    showSnackBar("Whatsapp have not been installed.")

                                }
                            }


                        })

            }

        } else if (post.post_type.equals("1")) {
            post_id_whatsApp = post.post_id
            post_id_Position = position
            val sendIntent = Intent()
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, post.title + "\n" + "देखें striclub ऍप पर, अभी फ्री डाउनलोड करे" +
                    "\n" + appplaystore)
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.whatsapp")

            try {
                startActivityForResult(sendIntent, 1);
            } catch (ex: android.content.ActivityNotFoundException) {
                showSnackBar("Whatsapp have not been installed.")

            }
        } else if (post.post_type.equals("2")) {
            iscallForNewsCount = true
            post_id_whatsApp = post.post_id
            post_id_Position = position
            id_Count_whatsApp = post.news_id
            Glide.with(activity!!).asBitmap()
                    .load(post.news_image)
                    .into(object : SimpleTarget<Bitmap>(100, 100) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val sendIntent = Intent()
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.type = "image/*"
                            sendIntent.putExtra(Intent.EXTRA_STREAM, UtilityFiles.getLocalBitmapUri(resource, activity!!))
                            sendIntent.putExtra(Intent.EXTRA_TEXT, post.title + "\n" + "देखें striclub ऍप पर, अभी फ्री डाउनलोड करे" +
                                    "\n" + appplaystore)
                            sendIntent.setPackage("com.whatsapp")

                            try {
                                startActivityForResult(sendIntent, 1);
                            } catch (ex: android.content.ActivityNotFoundException) {
                                showSnackBar("Whatsapp have not been installed.")

                            }
                        }


                    })
        } else if (post.post_type.equals("3")) {
            iscallForLekhCount = true
            post_id_whatsApp = post.post_id
            post_id_Position = position
            id_Count_whatsApp = post.blog_id
            Glide.with(activity!!).asBitmap()
                    .load(post.image)
                    .into(object : SimpleTarget<Bitmap>(100, 100) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val sendIntent = Intent()
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.type = "image/*"
                            sendIntent.putExtra(Intent.EXTRA_STREAM, UtilityFiles.getLocalBitmapUri(resource, activity!!))
                            sendIntent.putExtra(Intent.EXTRA_TEXT, post.title + "\n" + "देखें striclub ऍप पर, अभी फ्री डाउनलोड करे" +
                                    "\n" + appplaystore)
                            sendIntent.setPackage("com.whatsapp")

                            try {
                                startActivityForResult(sendIntent, 1);
                            } catch (ex: android.content.ActivityNotFoundException) {
                                showSnackBar("Whatsapp have not been installed.")

                            }
                        }


                    })
        }


    }


    override fun userPostSelectPostLike(post: Post, like: String) {
        if (post.post_type.equals("2")) {
            noOfLike = like
            id_Like = post.news_id
            getAlltotalLike_ForNews(post, like)
        } else if (post.post_type.equals("3")) {
            noOfLike = like
            id_Like = post.blog_id
            getAlltotalLike_ForLekh(post, like)
        } else {
            getAlltotalLike(post, like)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {


                getTotalWhatsAppLike()

            }
        } else {

        }
    }

    private val callback = object : FacebookCallback<Sharer.Result> {


        override fun onSuccess(result: Sharer.Result) {
            //   Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()

            getTotalFacebookLike()
        }

        override fun onCancel() {
//
            //Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()

        }

        override fun onError(error: FacebookException) {
            // Toast.makeText(activity, "cancelExpec", Toast.LENGTH_SHORT).show()


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

        if(iscallForNewsCount_facebook){
            getTotalFacebookLikeForSamachar()

        }
        if(iscallForLekhCount_facebook){
            getTotalFacebookLikeForLekh()

        }
        mCompositeDisposable_Facebook?.clear()


    }


    // handle failure response of api call
    private fun handleError_facebook(error: Throwable) {
        mCompositeDisposable_Facebook?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    private fun getTotalFacebookLikeForLekh() {

        //  showDialogLoading()
        mCompositeDisposable_Facebook = CompositeDisposable()
        mCompositeDisposable_Facebook?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForlekh(id_Count_whatsApp, "fb")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_facebook_Lekh, this::handleError_facebook_Lekh))
    }


    // handle sucess response of api call
    private fun handleResponse_facebook_Lekh(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {


        mCompositeDisposable_Facebook?.clear()


    }


    // handle failure response of api call
    private fun handleError_facebook_Lekh(error: Throwable) {


        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Facebook?.clear()

    }

    private fun getTotalFacebookLikeForSamachar() {

        //  showDialogLoading()
        mCompositeDisposable_Facebook = CompositeDisposable()
        mCompositeDisposable_Facebook?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForSamachar(id_Count_whatsApp, "fb")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_facebook_for_smaachar, this::handleError_facebook_For_smachar))
    }


    // handle sucess response of api call
    private fun handleResponse_facebook_for_smaachar(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {


        mCompositeDisposable_Facebook?.clear()


    }


    // handle failure response of api call
    private fun handleError_facebook_For_smachar(error: Throwable) {

        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Facebook?.clear()

    }


    private fun postToAddSavedFeed(user_id: String, post_id: String, type: String) {

        // showDialogLoading()
        mCompositeDisposable_Saved_Feed = CompositeDisposable()
        mCompositeDisposable_Saved_Feed?.add(ApiRequestClient.createREtrofitInstance()
                .postSavedPostFeed(dataManager.getUserId(), post_id, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_feed, this::handleError_svedFedd))
    }


    // handle sucess response of api call
    private fun handleResponse_feed(responseFromSerVerAddPostList: ResponseFromSerVerAddPostList) {

        mCompositeDisposable_Saved_Feed?.clear()


    }


    // handle failure response of api call
    private fun handleError_svedFedd(error: Throwable) {
        hideDialogLoading()
        mCompositeDisposable_Saved_Feed?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    private var mCompositeDisposable: CompositeDisposable? = null
    private var mCompositeDisposable_Like: CompositeDisposable? = null
    private var mCompositeDisposable_WhatsApp: CompositeDisposable? = null
    private var mCompositeDisposable_SiKayat: CompositeDisposable? = null
    private var mCompositeDisposable_Facebook: CompositeDisposable? = null
    private var mCompositeDisposable_Saved_Feed: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private var mUserAllFeedList: MutableList<Post>? = null
    private var mALlFeedVideoAdapter: CustomAdapterForHomeReacycler? = null
    private var post_id_whatsApp: String? = null
    private var post_id_Position: Int? = null
    private var id_Like: String? = null
    private var id_Count_whatsApp: String? = null
    private var noOfLike: String? = null
    private var callbackManager: CallbackManager? = null
    var fb_share_button: ShareButton? = null
    var shareDialog: ShareDialog? = null
    var iscallForCommentUpdate: Boolean = false
    var iscallForNewsCount: Boolean = false
    var iscallForLekhCount: Boolean = false
    var iscallForNewsCount_facebook: Boolean = false
    var iscallForLekhCount_facebook: Boolean = false
    var getArgument: String? = null
    var dataForHitServer: String? = null
    lateinit var tv_no_saved_post: TextView
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var sujao: String
    var dialogView: View? = null
    private var mAndroidStateList: MutableList<app.striclub.pojo.state.Result>? = null
    private lateinit var dialog_city: Dialog
    private lateinit var dialog_State: Dialog
    private var mCompositeDisposable_city: CompositeDisposable? = null
    private var mCompositeDisposable_state: CompositeDisposable? = null
    private var mStateAdapter: CustomAdapterForState? = null
    private var mCityAdapter: CustomAdapterForCity? = null
    private lateinit var city_name: String
    private var mCompositeDisposable_Update_Profile: CompositeDisposable? = null
    private lateinit var state_id: String
    private lateinit var city_id: String
    var dialog_recepies: Dialog? = null
    var width: Double? = null
    var height: Double? = null
    var buffer:StringBuffer?=null

    var win: Window? = null
    var dialogView1:View?=null
    var fab:TextView?=null
    var mRecepiesList:ArrayList<CheckFordataDialog> ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        FacebookSdk.sdkInitialize(activity!!.applicationContext)
        val view: View = inflater.inflate(R.layout.fragment_video, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mUserAllFeedList = ArrayList<Post>()
        //crating an arraylist to store users using the data class user

        view.rv_user_feed_list.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        tv_no_saved_post  =view.tv_no_saved_post
        //creating our adapter
        mALlFeedVideoAdapter = CustomAdapterForHomeReacycler(mUserAllFeedList!!, this, this)
        buffer= StringBuffer()
        mRecepiesList= ArrayList<CheckFordataDialog>()
        //now adding the adapter to recyclerview
        view.rv_user_feed_list.adapter = mALlFeedVideoAdapter
        swipeRefreshLayout = view.swipe_refresh_layout
        swipeRefreshLayout!!.setOnRefreshListener(this)
        dialog_recepies = Dialog(activity!!)
        dialogView1 = View.inflate(activity, R.layout.receipes_catogory, null)
        dialog_recepies!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_recepies!!.setCanceledOnTouchOutside(true)
        dialog_recepies!!.setContentView(dialogView1!!)
        val wm: WindowManager = activity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm!!.getDefaultDisplay()
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        width = metrics.widthPixels * .6
        height = metrics.heightPixels * .8
        win = dialog_recepies!!.getWindow()
        fb_share_button = view.fb_share_button
        fb_share_button?.fragment = this
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)
        view.fab.visibility=View.VISIBLE
        fab=view.fab
        if(isNetworkAvailable()){
            getAllUserFeed("")
        } else{
            showSnackBar("No Internet Connection")
        }

        mRecepiesList!!.add(CheckFordataDialog("Veg",false))
        mRecepiesList!!.add(CheckFordataDialog("Non Veg",false))
        mRecepiesList!!.add(CheckFordataDialog("Egg",false))
        mRecepiesList!!.add(CheckFordataDialog("Time",false))
        mRecepiesList!!.add(CheckFordataDialog("BreakFast",false))
        mRecepiesList!!.add(CheckFordataDialog("Lunch",false))
        mRecepiesList!!.add(CheckFordataDialog("Dinner",false))
        mRecepiesList!!.add(CheckFordataDialog("Cusines",false))
        mRecepiesList!!.add(CheckFordataDialog("Turkish",false))
        mRecepiesList!!.add(CheckFordataDialog("Mexican",false))
        mRecepiesList!!.add(CheckFordataDialog("Italian",false))
        mRecepiesList!!.add(CheckFordataDialog("Japanese",false))
        mRecepiesList!!.add(CheckFordataDialog("Indian",false))
        mRecepiesList!!.add(CheckFordataDialog("Thai",false))
        mRecepiesList!!.add(CheckFordataDialog("French",false))
        mRecepiesList!!.add(CheckFordataDialog("Spanish",false))

        view.fab.setOnClickListener {

            showDiag()
        }

        return view
    }
    private fun showDiag() {




        dialog_recepies!!.recycler_select.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        val adapterRecepies = CustomAdapterForReceipes(mRecepiesList!!, this)
        dialog_recepies!!.recycler_select.adapter = adapterRecepies


        dialog_recepies!!.fab_catogory.setOnClickListener {
            dialog_recepies!!.dismiss()
            //revealShow(dialogView1!!, false, null)
            val toString = buffer.toString()

            if(!toString.isEmpty()){

                getAllUserFeed(buffer.toString())
            }





        }
        dialog_recepies!!.setOnShowListener {
            revealShow(dialogView1!!, true, null)
            }


        dialog_recepies!!.show()
        win!!.setGravity(Gravity.END)
        win!!.setLayout(width!!.toInt(), height!!.toInt())
        dialog_recepies!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun revealShow(dialogView: View, b: Boolean, dialog: Dialog?) {

        val view = dialogView.findViewById<View>(R.id.dialog_anim)

        val w = view.width
        val h = view.height

        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()

        val cx = (fab!!.getX() + fab!!.getWidth() / 2).toInt()
        val cy = fab!!.getY().toInt() + fab!!.getHeight() + 56


        if (b) {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, endRadius.toFloat())


            //view.visibility = View.VISIBLE
            revealAnimator.duration = 300
            revealAnimator.start()

        } else {

            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius.toFloat(), 0f)


            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    dialog!!.dismiss()
                    view.visibility = View.INVISIBLE

                }
            })
            anim.duration = 700
            anim.start()
        }

    }




    override fun onResume() {
        super.onResume()
        if (iscallForCommentUpdate) {
            getAllUserFeed("")
        }

    }



    // api call for user registration
    private fun getAllUserFeed(argument: String?) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(ApiRequestClient.createREtrofitInstance()
                .getUserAllHomeFeed(dataManager.getUserId(), argument)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }


    // handle sucess response of api call
    private fun handleResponse(responseFromServerAllfeed: ResponseFromServerAllfeed) {
        hideDialogLoading()
        if(responseFromServerAllfeed.post.size==0){

            tv_no_saved_post.text=getString(R.string.noanypost)
            tv_no_saved_post.visibility=View.VISIBLE
        }else{
            mUserAllFeedList?.clear()
            mUserAllFeedList?.addAll(responseFromServerAllfeed.post)
            mALlFeedVideoAdapter?.notifyDataSetChanged()


        }
        mCompositeDisposable?.clear()



    }


    // handle failure response of api call
    private fun handleError(error: Throwable) {
        mCompositeDisposable?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    private fun getAllUserFeedForSwipe(argument: String?) {

        swipeRefreshLayout!!.isRefreshing = true
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(ApiRequestClient.createREtrofitInstance()
                .getUserAllHomeFeed(dataManager.getUserId(), argument)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseForSwipe, this::handleErrorForSwipe))
    }


    // handle sucess response of api call
    private fun handleResponseForSwipe(responseFromServerAllfeed: ResponseFromServerAllfeed) {
        swipeRefreshLayout!!.isRefreshing = false

        if(responseFromServerAllfeed.post.size==0){

            tv_no_saved_post.text=getString(R.string.noanypost)
            tv_no_saved_post.visibility=View.VISIBLE
        }else{
            mUserAllFeedList?.clear()
            mUserAllFeedList?.addAll(responseFromServerAllfeed.post)
            mALlFeedVideoAdapter?.notifyDataSetChanged()


        }
        mCompositeDisposable?.clear()



    }


    // handle failure response of api call
    private fun handleErrorForSwipe(error: Throwable) {
        mCompositeDisposable?.clear()
        swipeRefreshLayout!!.isRefreshing = false

        showSnackBar(error.localizedMessage)


    }
    // api call for user registration
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


        if (iscallForNewsCount) {
            getTotalWhatsAppLikeForSamchar()


        }
        if (iscallForLekhCount) {
            getAlltotalLikeForLekh()

        }
        mCompositeDisposable_WhatsApp?.clear()


    }


    // handle failure response of api call
    private fun handleError_whatsApp(error: Throwable) {
        mCompositeDisposable_WhatsApp?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    // api call for user registration
    private fun getTotalWhatsAppLikeForSamchar() {

        //  showDialogLoading()
        mCompositeDisposable_WhatsApp = CompositeDisposable()

        mCompositeDisposable_WhatsApp?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForSamachar(id_Count_whatsApp, "wp")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_whatsAppForSamachar, this::handleError_whatsApp_ForSmachar))
    }


    // handle sucess response of api call
    private fun handleResponse_whatsAppForSamachar(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {


        mCompositeDisposable_WhatsApp?.clear()


    }


    // handle failure response of api call
    private fun handleError_whatsApp_ForSmachar(error: Throwable) {
        mCompositeDisposable_WhatsApp?.clear()

        showSnackBar(error.localizedMessage)


    }

    // api call for user registration
    private fun getAlltotalLikeForLekh() {

        //  showDialogLoading()
        //  showDialogLoading()
        mCompositeDisposable_WhatsApp = CompositeDisposable()

        mCompositeDisposable_WhatsApp?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForlekh(id_Count_whatsApp, "wp")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Like_Lekh, this::handleError_like_Lekh))
    }


    // handle sucess response of api call
    private fun handleResponse_Like_Lekh(responseFromServerAllfeed: ResponseFromServerWhatsApp) {

        mCompositeDisposable_WhatsApp?.clear()


    }


    // handle failure response of api call
    private fun handleError_like_Lekh(error: Throwable) {


        showSnackBar(error.localizedMessage)
        mCompositeDisposable_WhatsApp?.clear()


    }


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
        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_like(error: Throwable) {


        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }

    private fun getAlltotalLike_ForNews(post: Post, like: String) {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()
        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLike(dataManager.getUserId(), post.post_id, like)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_LikeForNews, this::handleError_likeForNews))
    }


    // handle sucess response of api call
    private fun handleResponse_LikeForNews(responseFromServerAllfeed: ResponseFromServerAllfeed) {
        getAlltotalLike_ForNews1()

        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_likeForNews(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }

    private fun getAlltotalLike_ForNews1() {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()
        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLikeForSamchar(dataManager.getUserId(), id_Like, noOfLike)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_LikeForNews1, this::handleError_likeForNews1))
    }


    // handle sucess response of api call
    private fun handleResponse_LikeForNews1(responseFromServerAllfeed: ServerFromResponseListNews) {


        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_likeForNews1(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }

    private fun getAlltotalLike_ForLekh(post: Post, like: String) {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()
        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLike(dataManager.getUserId(), post.post_id, like)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_LikeForLekh, this::handleError_likeForLekh))
    }


    // handle sucess response of api call
    private fun handleResponse_LikeForLekh(responseFromServerAllfeed: ResponseFromServerAllfeed) {
        getAlltotalLike_ForLekh1()

        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_likeForLekh(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }

    private fun getAlltotalLike_ForLekh1() {

        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()
        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLikeForLekh(dataManager.getUserId(), id_Like, noOfLike)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_LikeForLekh1, this::handleError_likeForLekh1))
    }


    // handle sucess response of api call
    private fun handleResponse_LikeForLekh1(responseFromServerAllfeed: ResponseFromServerListBlog) {


        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_likeForLekh1(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


    }

    override fun userPostSelectVideoId(post: Post) {
        val intent = Intent(activity, YoutubePlayerActivity::class.java)
        intent.putExtra("youtubeVideoId", post as Serializable)
        startActivity(intent)
    }

}
