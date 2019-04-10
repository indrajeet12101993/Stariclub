package app.striclub.activity

import app.striclub.R
import app.striclub.base.BaseActivity
import app.striclub.base.BaseApplication
import app.striclub.dataprefence.DataManager
import app.striclub.networkUtils.ApiRequestClient
import app.striclub.pojo.AllPostFeed.Post
import app.striclub.pojo.ResponseFromServerSikayat
import app.striclub.pojo.ResponseFromServerWhatsApp
import app.striclub.pojo.listBlog.ResponseFromServerListBlog
import app.striclub.utils.TimeUtils
import app.striclub.utils.UtilityFiles
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import app.striclub.adapter.CustomAdapterSteps
import app.striclub.adapter.CustomIngredientsAdapter
import app.striclub.pojo.stepAndIngriedients.ResponseFromServerIngriendts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_lekh_read_more.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.radiobutton_dialog.*

class HomeLekhReadMoreActivity : BaseActivity() {

    var post: Post? = null
    private var updateLike: Int? = null
    private var mCompositeDisposable_Like: CompositeDisposable? = null
    private var mCompositeDisposable_WhatsApp: CompositeDisposable? = null
    private var mCompositeDisposable_SiKayat: CompositeDisposable? = null
    private var mCompositeDisposable_Facebook: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private var post_id_whatsApp: String? = null
    var iscallForCommentUpdate: Boolean = false
    private var callbackManager: CallbackManager? = null
    var shareDialog: ShareDialog? = null
    var like: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_lekh_read_more)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tv_title1.visibility = View.INVISIBLE
        tv_by1.visibility = View.INVISIBLE

        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true

                    tv_title1.visibility = View.VISIBLE
                    tv_by1.visibility = View.VISIBLE

                } else if (isShow) {
                    isShow = false
                    tv_title1.visibility = View.INVISIBLE
                    tv_by1.visibility = View.INVISIBLE
                    //hideOption(R.id.action_info)
                }
            }
        })
        post = intent.getSerializableExtra("samachardata") as Post

        rv_ingredients.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv_steps.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rv_ingredients.isNestedScrollingEnabled = false
        rv_steps.isNestedScrollingEnabled = false



        callbackManager = CallbackManager.Factory.create()
        Glide.with(this).load(post!!.news_image).into(iv_lekh_view)

        tv_title.text = post!!.title
        tv_title1.text = post!!.title


        // click on linar like
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


            if (post!!.Isliked.equals("1")) {
                updateLike = post!!.no_like.toInt()
                if (iv_like_second.visibility == View.VISIBLE) {
                    iv_like_second.visibility = View.INVISIBLE
                    iv_like_first.visibility = View.VISIBLE
                    like = updateLike!! - 1
                    val Totallike: String = like.toString()
                    tv_total_like.text = Totallike
                    getAlltotalLike(post!!, "0")
                    return@setOnClickListener
                }
                if (iv_like_first.visibility == View.VISIBLE) {
                    iv_like_first.visibility = View.INVISIBLE
                    iv_like_second.visibility = View.VISIBLE
                    val like1: Int = like!! + 1
                    val Totallike: String = like1.toString()
                    tv_total_like.text = Totallike
                    getAlltotalLike(post!!, "1")
                    return@setOnClickListener

                }
            }
            if (post!!.Isliked.equals("0")) {
                updateLike = post!!.no_like.toInt()
                if (iv_like_first.visibility == View.VISIBLE) {
                    iv_like_second.visibility = View.VISIBLE
                    iv_like_first.visibility = View.INVISIBLE
                    like = updateLike!! + 1
                    val Totallike: String = like.toString()
                    tv_total_like.text = Totallike
                    getAlltotalLike(post!!, "1")
                    return@setOnClickListener
                } else {
                    iv_like_second.visibility = View.INVISIBLE
                    iv_like_first.visibility = View.VISIBLE
                    //val updateLike  =post.no_like.toInt()-1
                    //  val like: Int = updateLike
                    //  val Totallike: String = like.toString()
                    val like1: Int = like!! - 1
                    val Totallike: String = like1.toString()
                    tv_total_like.text = updateLike.toString()
                    getAlltotalLike(post!!, "0")
                    return@setOnClickListener
                }
            }


        }

        //whatsapp share
        tv_total_whattsapp.text = post!!.no_share
        linear_whatsapp.setOnClickListener {

            tv_total_whattsapp.text = (post!!.no_share.toInt() + 1).toString()
            post_id_whatsApp = post!!.blog_id
            val appplaystore: String = "https://play.google.com/store/apps/details?id=in.krishkam"
            Glide.with(this).asBitmap()
                    .load(post!!.image)
                    .into(object : SimpleTarget<Bitmap>(100, 100) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val sendIntent = Intent()
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.type = "image/*"
                            sendIntent.putExtra(Intent.EXTRA_STREAM, UtilityFiles.getLocalBitmapUri(resource, applicationContext))
                            sendIntent.putExtra(Intent.EXTRA_TEXT, post!!.title + "\n" + "देखें कृषकं ऍप पर, अभी फ्री डाउनलोड करे" +
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

        //comment
        tv_total_cooment.text = post!!.no_comment
        linear_comment.setOnClickListener {

            iscallForCommentUpdate = true
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("post_id", post!!.blog_id)
            startActivity(intent)


        }


        //facebook
        tv_total_facebook.text = post!!.no_fb_share
        linear_facebook.setOnClickListener {
            tv_total_facebook.text = (post!!.no_fb_share.toInt() + 1).toString()
            shareDialog = ShareDialog(this)
            val appplaystore: String = "https://play.google.com/store/apps/details?id=in.krishkam"
            post_id_whatsApp = post!!.blog_id

            if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                shareDialog?.registerCallback(callbackManager, callback)

                val linkContent = ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(appplaystore))
                        .build()
                shareDialog!!.show(linkContent)

            }


        }
        getAllCurrentPost()

    }

    private val callback = object : FacebookCallback<Sharer.Result> {


        override fun onSuccess(result: Sharer.Result) {

            // Toast.makeText(this@lekhReadMoreActivity, "success", Toast.LENGTH_SHORT).show()

            getTotalFacebookLike()
        }

        override fun onCancel() {

            //   Toast.makeText(this@lekhReadMoreActivity, "cancel", Toast.LENGTH_SHORT).show()

        }

        override fun onError(error: FacebookException) {
            // Toast.makeText(this@lekhReadMoreActivity, "error", Toast.LENGTH_SHORT).show()


        }
    }

    private fun getTotalFacebookLike() {

        //  showDialogLoading()
        mCompositeDisposable_Facebook = CompositeDisposable()
        mCompositeDisposable_Facebook?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForlekh(post_id_whatsApp, "fb")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_facebook, this::handleError_facebook))
    }


    // handle sucess response of api call
    private fun handleResponse_facebook(responseFromServerWhatsApp: ResponseFromServerWhatsApp) {


        val result = responseFromServerWhatsApp.result
        tv_total_facebook.text = result
        mCompositeDisposable_Facebook?.clear()


    }


    // handle failure response of api call
    private fun handleError_facebook(error: Throwable) {
        mCompositeDisposable_Facebook?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }




    // api call for user registration
    private fun postSikayat(post_id: String, sikayat: String) {

        showDialogLoading()
        mCompositeDisposable_SiKayat = CompositeDisposable()
        mCompositeDisposable_SiKayat?.add(ApiRequestClient.createREtrofitInstance()
                .postSikayat(post_id, sikayat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Sikayat, this::handleError_Sikayat))
    }


    // handle sucess response of api call
    private fun handleResponse_Sikayat(responseFromServerSikayat: ResponseFromServerSikayat) {
        hideDialogLoading()
        mCompositeDisposable_SiKayat?.clear()


    }


    // handle failure response of api call
    private fun handleError_Sikayat(error: Throwable) {
        mCompositeDisposable_SiKayat?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }


    private fun getAlltotalLike(post: Post, like: String) {


        //  showDialogLoading()
        mCompositeDisposable_Like = CompositeDisposable()

        mCompositeDisposable_Like?.add(ApiRequestClient.createREtrofitInstance()
                .getTotalLikeForLekh(dataManager.getUserId(), post.blog_id, like)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Like, this::handleError_like))
    }


    // handle sucess response of api call
    private fun handleResponse_Like(responseFromServerAllfeed: ResponseFromServerListBlog) {

        mCompositeDisposable_Like?.clear()


    }


    // handle failure response of api call
    private fun handleError_like(error: Throwable) {


        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Like?.clear()


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

    // api call for user registration
    private fun getTotalWhatsAppLike() {

        //  showDialogLoading()
        mCompositeDisposable_WhatsApp = CompositeDisposable()

        mCompositeDisposable_WhatsApp?.add(ApiRequestClient.createREtrofitInstance()
                .getsharecountWhatsAppForlekh(post_id_whatsApp, "wp")
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
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
