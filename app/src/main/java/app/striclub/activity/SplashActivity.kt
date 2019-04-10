package app.striclub.activity


import android.os.Bundle
import android.os.Handler

import app.striclub.R
import app.striclub.base.BaseActivity
import app.striclub.base.BaseApplication
import app.striclub.constants.AppConstants.SPLASH_DELAY
import app.striclub.dataprefence.DataManager


class SplashActivity : BaseActivity() {
    private var mDelayHandler: Handler? = null
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if(!dataManager.getLoggedIn()){

                launchActivity<MainActivity>()
                finish()
                return@Runnable

            }
            else{

                launchActivity<UserFeedActivity>()
                finish()
            }









        }
    }

    override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onBackPressed()
    }
}
