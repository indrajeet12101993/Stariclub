package app.striclub.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_feed.*
import kotlinx.android.synthetic.main.app_bar_user_feed.*
import kotlinx.android.synthetic.main.content_user_feed.*
import kotlinx.android.synthetic.main.nav_header_user_feed.view.*
import app.striclub.Fragment.*
import app.striclub.R
import app.striclub.base.BaseActivity
import app.striclub.base.BaseApplication
import  app.striclub.dataprefence.DataManager
import app.striclub.networkUtils.ApiRequestClient
import app.striclub.pojo.ServerResponseFromUplaodImage
import app.striclub.pojo.UserEditShowInitial.ResponseFromServerInitialEditUser
import app.striclub.pojo.UserEditShowInitial.UserDetail
import com.google.firebase.analytics.FirebaseAnalytics
import android.content.Intent
import android.net.Uri
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater


import kotlinx.android.synthetic.main.rating_dialog.view.*
import kotlinx.android.synthetic.main.sujaho_dialog.view.*


class UserFeedActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var dataManager: DataManager
    var responsefromserverForInitial: MutableList<UserDetail>? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var navigation1: BottomNavigationView
    var headerLayout: View? = null
    var refresh: Boolean = false
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private lateinit var sujao: String
    private var mCompositeDisposable_Update_Profile: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_feed)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setLogo(R.drawable.logo_top)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        //naviagtion drawer
        nav_view.setNavigationItemSelectedListener(this)
        //toolbar.title = getString(R.string.app_title)

        bindDataWithUi()
        if(isNetworkAvailable()) {
            getInitalEditProfile()
        }else{
            showSnackBar("No Internet Connection")
        }
        // find header view in naviagtion drawer
        headerLayout = nav_view.getHeaderView(0) // 0-index header


        headerLayout?.setOnClickListener {
            refresh = true

            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            replaceFragment(UserEditFragment(), getString(R.string.myprofile))


        }
        navigation1 = navigation
        //bottom naviagtion view
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, dataManager.getUserId())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, dataManager.getUserMobile())
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        addHomeFragment()
    }

    override fun onResume() {
        super.onResume()
        if (refresh) {
            getInitalEditProfile()
        }
    }

    private fun bindDataWithUi() {
        if (responsefromserverForInitial != null) {

            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_man)
            requestOptions.error(R.drawable.ic_man)

            if (responsefromserverForInitial!!.get(0).name.isEmpty()) {
                headerLayout!!.tv_nav_user_name.text = "upadte Name!"
            } else {
                headerLayout!!.tv_nav_user_name.text = responsefromserverForInitial!!.get(0).name
            }

            if (responsefromserverForInitial!!.get(0).state.isEmpty()) {
                headerLayout!!.tv_city.text = "plz Update!"
            } else {
                headerLayout!!.tv_city.text = responsefromserverForInitial!!.get(0).statename
            }

            if (responsefromserverForInitial!!.get(0).city.isEmpty()) {
                headerLayout!!.tv_district.text = "plz Update!"
            } else {
                headerLayout!!.tv_district.text = responsefromserverForInitial!!.get(0).cityname
            }




            headerLayout!!.tv_nav_user_mobile.text = responsefromserverForInitial!!.get(0).mobile
            Glide.with(this).load(responsefromserverForInitial!!.get(0).image).apply(requestOptions).into(headerLayout!!.iv_nav_profile)
            dataManager.saveUserName(responsefromserverForInitial!!.get(0).name)
            dataManager.saveUserMobile(responsefromserverForInitial!!.get(0).mobile)
            dataManager.saveUserProfilePic(responsefromserverForInitial!!.get(0).image)
        }
    }

    private fun getInitalEditProfile() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(ApiRequestClient.createREtrofitInstance()
                .getUserInitialEditData(dataManager.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }


    // handle sucess response of api call
    private fun handleResponse(response: ResponseFromServerInitialEditUser) {
        hideDialogLoading()
        responsefromserverForInitial = response.user_detail
        mCompositeDisposable?.clear()
        bindDataWithUi()


    }


    // handle failure response of api call
    private fun handleError(error: Throwable) {
        mCompositeDisposable?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)


    }


    private fun addHomeFragment() {
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        //toolbar.title = getString(R.string.app_title)

        val homeFragment = HomeFragment()
        val data = Bundle()
        data.putString("data", getString(R.string.app_title))
        data.putString("dataForHitServer", "no")
        homeFragment.arguments = data
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.place_holder_for_fragment, homeFragment)
        //  transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun replaceFragment(fragment: Fragment, string: String) {
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
       // toolbar.title = string
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.commit()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                val homeFragment = HomeFragment()
                val data = Bundle()
                data.putString("data", getString(R.string.app_title))
                data.putString("dataForHitServer", "no")
                homeFragment.arguments = data
                replaceFragment(homeFragment, getString(R.string.app_title))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_video -> {

                replaceFragment(ReceipeFragment(), getString(R.string.video))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_smachar -> {
                replaceFragment(BeautyFragment(), getString(R.string.samachar))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_lekh -> {

                replaceFragment(LekhFragment(), getString(R.string.lekh))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            if (supportFragmentManager.findFragmentById(R.id.place_holder_for_fragment) is HomeFragment) {
                super.onBackPressed()
            } else {
                navigation1.selectedItemId = R.id.navigation_home
                val homeFragment = HomeFragment()
                val data = Bundle()
                data.putString("data", getString(R.string.title_home))
                data.putString("dataForHitServer", "no")
                homeFragment.arguments = data
                replaceFragment(homeFragment, getString(R.string.title_home))
            }


        }


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                //toolbar.setTitle(getString(R.string.app_name_title))
                val homeFragment = HomeFragment()
                val data = Bundle()
                data.putString("data", getString(R.string.app_title))
                data.putString("dataForHitServer", "no")
                homeFragment.arguments = data
                navigation1.selectedItemId = R.id.navigation_home
                replaceFragment(homeFragment, getString(R.string.app_title))
            }

            R.id.nav_video -> {
                navigation1.selectedItemId = R.id.navigation_video
                replaceFragment(ReceipeFragment(), getString(R.string.video))
            }
            R.id.nav_samachar -> {
                navigation1.selectedItemId = R.id.navigation_smachar
                replaceFragment(BeautyFragment(), getString(R.string.samachar))
            }
            R.id.nav_lekh -> {
                replaceFragment(LekhFragment(), getString(R.string.lekh))
            }
            R.id.nav_saved_post -> {
                navigation1.selectedItemId = R.id.navigation_home
                replaceFragment(UserSavedFeedListFragemnt(), getString(R.string.savedpost))
            }
            R.id.nav_rating -> {

                ShowRatingDialog()



            }
            R.id.nav_search -> {
                replaceFragment(TrendingFragemnt(), getString(R.string.search))
            }
            R.id.nav_upyog -> {

                launchActivity<TermsAndConditionsActivity>()

            }
            R.id.nav_logout -> {

                dataManager.clear()
                endActivity<MainActivity>()
                finish()

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun ShowRatingDialog() {

        val popDialog = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.rating_dialog, null)
        popDialog.setView(dialogView);
        popDialog.setIcon(android.R.drawable.btn_star_big_on)
        popDialog.setTitle("Plz rate us !")

        popDialog.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->


            dialog.dismiss()
            var rating   =dialogView.rating_bar.progress
            if(rating>4){
                showDhnwad()



            }else{
                sujhaoDialog()
              //  showDhnwadfornot()
            }

        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        popDialog.create()
        popDialog.show()


    }

    fun showDhnwad() {

        val popDialog = AlertDialog.Builder(this)
        popDialog.setTitle("thanks for rating us ! plz rate 5 star on play store!")
        popDialog.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->

            dialog.dismiss()



                val appPackageName = packageName // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }




        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        popDialog.create()
        popDialog.show()


    }

    fun sujhaoDialog() {

        val popDialog = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.sujaho_dialog, null)
        popDialog.setView(dialogView);
        popDialog.setTitle("suugest us what we can do better !")

        popDialog.create()

        // Set a positive button and its click listener on alert dialog
        popDialog.setPositiveButton(android.R.string.ok) { dialog, which ->

            sujao = dialogView.et_sujaho.text.toString()
            if (sujao.isEmpty()) {
                dialogView.et_sujaho.setError("suugest us what we can do bette !")
                dialogView.et_sujaho.requestFocus()

            } else {

                uploadingUserProfileUpdate()

            }


        }



        popDialog.setNegativeButton("Cancel") { dialog, which ->

        }

        popDialog.show()


    }

    private fun uploadingUserProfileUpdate() {


        showDialogLoading()
        mCompositeDisposable_Update_Profile = CompositeDisposable()
        mCompositeDisposable_Update_Profile?.add(ApiRequestClient.createREtrofitInstance()
                .userSujhao(dataManager.getUserId(), "", "", "", sujao,"")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUpdateProfile, this::handleErrorUpdateProfile))
    }


    // handle sucess response of api call
    private fun handleResponseUpdateProfile(response: ServerResponseFromUplaodImage) {
        hideDialogLoading()

        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("thanks for suggestion!")

                .setCancelable(false)

                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->

                })
        val alert = dialogBuilder.create()
        alert.show()
        mCompositeDisposable_Update_Profile?.clear()

    }

    // handle failure response of api call
    private fun handleErrorUpdateProfile(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_Update_Profile?.clear()

    }
    fun showDhnwadfornot() {

        val popDialog = AlertDialog.Builder(this)


        popDialog.setTitle("thanks for rate us!")
        popDialog.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->


            dialog.dismiss()


        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        popDialog.create()
        popDialog.show()


    }
}
