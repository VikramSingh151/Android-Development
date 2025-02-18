package com.example.paymentgateway

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultWithDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Checkout.preload(applicationContext)
        val co = Checkout()

        co.setKeyID("rzp_test_dfjnFPbS1Vycqg")

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            initpayment()
        }
    }

    private fun initpayment() {
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Neutrino")
            options.put("description","Software Development Company")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#FF1515");
            options.put("currency","INR");
            //options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","abc7566210@gmail.com")
            prefill.put("contact","1234567890")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment success ", Toast.LENGTH_SHORT) .show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error :  ${p1} ", Toast.LENGTH_SHORT) .show()
    }
}