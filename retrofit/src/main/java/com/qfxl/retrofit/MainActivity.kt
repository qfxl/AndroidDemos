package com.qfxl.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    private val mRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://gank.io")
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_request.setOnClickListener {
            val api = mRetrofit.create(API::class.java)
            val mCall = api.getCategory()
            mCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                }
            })
        }
    }

    interface API {
        @GET("/api/xiandu/categories")
        fun getCategory(): Call<ResponseBody>
    }
}


fun String.log() {
    Log.i("qfxl", this)
}