package app.striclub.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import app.striclub.R
import app.striclub.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import android.support.design.widget.AppBarLayout
import android.view.View


class ScrollingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tv_title.visibility= View.INVISIBLE


        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    tv_title.visibility= View.VISIBLE
                    //showOption(R.id.action_info)
                } else if (isShow) {
                    isShow = false
                    tv_title.visibility= View.INVISIBLE
                    //hideOption(R.id.action_info)
                }
            }
        })
    }

//    private fun hideOption(id: Int) {
//        val item = menu.findItem(id)
//        item.setVisible(false)
//    }
//
//    private fun showOption(id: Int) {
//        val item = menu.findItem(id)
//        item.setVisible(true)
//    }
}
