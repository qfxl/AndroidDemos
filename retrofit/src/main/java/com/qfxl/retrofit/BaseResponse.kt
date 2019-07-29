package com.qfxl.retrofit

/**
 *
 * @ProjectName: AndroidDemos
 * @Package: com.qfxl.retrofit
 * @ClassName: BaseResponse
 * @Description:
 * @Author: 清风徐来
 * @CreateDate: 2019/7/11 14:31
 * @UpdateUser: 清风徐来
 * @UpdateDate: 2019/7/11 14:31
 * @UpdateRemark:
 * @Version: 1.0
 */
data class BaseResponse<T>(val error: Boolean, val results: T)