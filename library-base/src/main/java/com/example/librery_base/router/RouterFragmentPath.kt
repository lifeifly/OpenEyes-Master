package com.example.librery_base.router

/**
 *description : <p>
 *AROUTER路径统一注册
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 23
 */
class RouterFragmentPath {
    //首页组件
    class Home {
        companion object {


            //首页
           const val PAGER_HOME = "/home/Home"
        }
    }

    //社区组件
    class Community {
        companion object {
            //社区页
          const  val PAGER_COMMUNITY =  "/community/Community"
        }
    }

    //更多组件
    class More {
        companion object {
            //更多页
            const val PAGER_MORE ="/more/More"
        }
    }

    //个人组件
    class User {
        companion object {
            //个人页
           const val PAGER_USER =  "/user/User"
        }
    }

    //搜索组件
    class Search{
        companion object{
            const val PAGER_SEARCH="/search/Search"
        }
    }
}