package com.github.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.github.databinding.databinding.ActivitySplashBinding
import com.github.databinding.vo.User
import kotlin.random.Random

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        val user = User("qfxl", 28)
        binding.mUser = user

        delay(3000) {
            user.nameHolder?.set("Hello World")
        }
        binding.clickListener = View.OnClickListener {
            navigate(MainActivity::class.java)
        }
    }
}
