package com.example.librery_base.eventbus

/**
 *description : <p>
 *用于eventbus发送事件的顶级事件
 *</p>
 *
 *@author : flyli
 *@since :2021/5/30 11
 */
open class MsgEvent

/**
 * EventBus通知刷新界面消息的事件类
 * @property activityClass Class<*>?
 * @constructor
 */
open class RefreshEvent(var tab: Int = 0) : MsgEvent()

/**
 * EventBus用于通知Tab切换界面的事件类
 * @property activityClass Class<*>?
 * @constructor
 */
open class SwitchPagesEvent(var tab:Int=0) : MsgEvent()

interface Navigation {
    interface Home {
        companion object {
            const val HOME=10
            const val HOME_NOMINATE = 1
            const val HOME_DISCOVERY = 2
            const val HOME_DAILY = 3
        }
    }

    interface Community {
        companion object {
            const val COMMUNITY=11
            const val COMMUNITY_RECOMMEND = 4
            const val COMMUNITY_ATTENTION = 5
        }
    }

    interface Notification {
        companion object {
            const val NOTIFICATION=12
            const val NOTIFICATION_PUSH = 6
            const val NOTIFICATION_INTERACT = 7
            const val NOTIFICATION_PRIVATE = 8
        }
    }

    interface Mine {
        companion object {
            const val MINE = 9
        }
    }
}