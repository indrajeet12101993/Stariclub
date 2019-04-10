package app.striclub.activity

import app.striclub.R


import android.os.Bundle


import kotlinx.android.synthetic.main.activity_main.*
import app.striclub.base.BaseActivity
import app.striclub.utils.Validation
import android.content.Intent



class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // click listner of otp
        btn_otp_prapt.setOnClickListener {

            val phone_number: String = et_phone_number.text.toString()

            if (Validation.isEmptyField(phone_number)) {
                et_phone_number.error = getString(R.string.emptynumber)

                return@setOnClickListener


            }
            if (!Validation.isValidPhoneNumber(phone_number)) {
                et_phone_number.error = "please input valid 10 digit number"
                return@setOnClickListener

            }
            //  hideKeyboard(et_phone_number)

            if (isNetworkAvailable()) {
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra("Username", phone_number)
                startActivity(intent)
                finish()
            } else {
                showSnackBar("\n" +
                        "No internet connection")
            }


        }
    }


}
