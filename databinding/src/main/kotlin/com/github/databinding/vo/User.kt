package com.github.databinding.vo

import androidx.databinding.ObservableField

/**
 *
 * @ProjectName: AndroidDemos
 * @Package: com.github.databinding.vo
 * @ClassName: User
 * @Description:
 * @Author: 清风徐来
 * @CreateDate: 2019/7/31 15:12
 * @UpdateUser: 清风徐来
 * @UpdateDate: 2019/7/31 15:12
 * @UpdateRemark:
 * @Version: 1.0
 */
class User(name: String, age: Int) {

    var nameHolder: ObservableField<String>? = null
    var ageHolder: ObservableField<Int>? = null

    init {
        this.nameHolder = ObservableField(name)
        this.ageHolder = ObservableField(age)
    }

}