package com.example.librery_base.exception

import java.lang.RuntimeException

/**
 *description : <p>
 *当服务器响应的头不在200与300之间时，说明请求出现了异常，此时应该将此异常主动抛出。
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 23
 */
class ResponseCodeException(val responseCode:Int):RuntimeException("Http request failed with response code ${responseCode}")