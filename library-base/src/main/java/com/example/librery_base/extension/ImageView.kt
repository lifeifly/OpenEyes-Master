package com.example.librery_base.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Option
import com.bumptech.glide.request.RequestOptions
import com.example.library_base.base.BaseApplication
import com.example.librery_base.R
import com.example.librery_base.global.dp2px
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.net.URL
import kotlin.math.round

/**
 *description : <p>
 *ImageView的常用方法
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 17
 */
/**
 * 加载网络图片
 * @receiver ImageView
 * @param url String
 * @param corner Float 圆角角度
 * @param cornerType CornerType  圆角位置
 */
fun ImageView.load(url: String,corner:Float=0F,cornerType: RoundedCornersTransformation.CornerType=RoundedCornersTransformation.CornerType.ALL){
    if (corner==0F){
        Glide.with(BaseApplication.getInstance()).load(url).into(this)
    }else{
        val option=RequestOptions.bitmapTransform(RoundedCornersTransformation(dp2px(corner),0,cornerType))
            .placeholder(R.drawable.shape_album_loading_bg)
        Glide.with(BaseApplication.getInstance()).load(url).apply(option).into(this)
    }
}

/**
 * 自定义加载图片的参数
 * @receiver ImageView
 * @param url String
 * @param block
 */
fun ImageView.load(url: String,block: RequestOptions.()->RequestOptions){
    Glide.with(BaseApplication.getInstance()).load(url).apply(RequestOptions().block()).into(this)
}