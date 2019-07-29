package com.github.fingerprint

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mBiometricPrompt by lazy {
        BiometricPrompt.Builder(this)
            .setTitle("验证指纹")
            .setDescription("请先验证您的指纹")
            .setNegativeButton("取消", mainExecutor, DialogInterface.OnClickListener { dialog, which -> toast("取消了指纹验证")})
            .build()
    }

    private val mCancellationSignal by lazy {
        CancellationSignal().also {
            it.setOnCancelListener {

            }
        }
    }

    private val mAuthenticationCallback by lazy {
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                toast("指纹验证失败 $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                toast("指纹验证成功")
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                toast("指纹验证失败，请 $helpString")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_verify_fingerprint.setOnClickListener {
            mBiometricPrompt.authenticate(mCancellationSignal, mainExecutor, mAuthenticationCallback)
        }
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}