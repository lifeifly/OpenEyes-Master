package com.example.librery_base.router

/**
 *description : <p>
 *存放activity的路径,用ARouter跳转页面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 23
 */
class RouterActivityPath {
    /**
     * main组件
     */
    class Main {
        companion object {
            //主页面
            const val PAGER_MAIN = "/main/Main"
        }
    }

    /**
     * 视频播放组件
     * @property VIDEO String
     * @property PAGER_VIDEO String
     */
    class Video {
        companion object {
            //视频播放页面
            const val PAGER_VIDEO = "/video/Video"
        }
    }

    class User {
        companion object {


            //关注页面
            const val PAGER_ATTENTION = "/user/Attention"
        }
    }

    class WebView {
        companion object {



            //关注页面
            const val PAGER_WEBVIEW = "/webview/WebView"
        }
    }

    class Login{
        companion object{
            const val PAGER_LOGIN="/login/Login"
        }
    }

    class Detail{
        companion object{
            //视频详情
            const val PAGER_DETAIL="/detail/Detail"
        }
    }

    class Ugc{
        companion object{
            //ugc
            const val PAGER_USCDETAIL="/ugc/UGCDetail"
        }
    }
}