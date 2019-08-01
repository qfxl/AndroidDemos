package com.github.databinding.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 *
 * @ProjectName: AndroidDemos
 * @Package: com.github.databinding.binding
 * @ClassName: BindingAdapters
 * @Description:
 * @Author: 清风徐来
 * @CreateDate: 2019/8/1 9:24
 * @UpdateUser: 清风徐来
 * @UpdateDate: 2019/8/1 9:24
 * @UpdateRemark:
 * @Version: 1.0
 */

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}