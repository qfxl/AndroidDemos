package com.github.databinding

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

/**
 *
 * @ProjectName: AndroidDemos
 * @Package: com.github.databinding
 * @ClassName: Extension
 * @Description:
 * @Author: 清风徐来
 * @CreateDate: 2019/7/31 17:28
 * @UpdateUser: 清风徐来
 * @UpdateDate: 2019/7/31 17:28
 * @UpdateRemark:
 * @Version: 1.0
 */

fun <T> Context.navigate(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}

inline fun <T> delay(delayTime: Long, crossinline block: () -> T) =
    Handler(Looper.getMainLooper()).postDelayed({
        block()
    }, delayTime)