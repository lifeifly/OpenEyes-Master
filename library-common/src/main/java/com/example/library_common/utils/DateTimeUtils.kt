package com.example.library_common.utils

import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.format.Time
import com.example.library_common.R
import com.example.librery_base.global.GlobalUtils
import com.eyepetizer.android.util.DateUtil
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *description : <p>
 *将毫秒转为00：00：00
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 15
 */
class DateTimeUtils private constructor() {
    companion object {
        /**
         * 2010-12-01 23:15:36
         */
        private const val FORMAT_LONG = "yyyy-MM-dd HH:mm:ss"

        /**
         * 精确到毫秒
         * 2010-12-01 23:15:36.156
         */
        private const val FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS"

        /**
         * 精确到天
         * 2010-12-01
         */
        private const val FORMAT_SHORT = "yyyy-MM-dd"

        /**
         *
         * 2010年12月01日
         */
        private const val FORMAT_LONG_CN = "yyyy年MM月dd日 HH时mm分ss秒"

        /**
         * 2010年12月01日 23时15分36秒
         */
        private const val FORMAT_FULL_CN = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒"

        /**
         * 2010年12月01日 23时15分36秒156毫秒
         */
        private const val FORMAT_SHORT_CN = "yyyy年MM月dd日"

        /**
         * 默认的格式FORMAT_LONG
         */
        private fun getDataPattern(): String {
            return FORMAT_LONG
        }

        /**
         * 根据格式返回现在的时间
         */
        fun getNow(): String = format(Date())

        /**
         * 格式化时间
         * @param date Date
         * @return String
         */
        private fun format(date: Date): String = format(date, getDataPattern())

        /**
         * 格式化时间
         * @param date Date
         * @param pattern String
         * @return String
         */
        private fun format(date: Date, pattern: String): String {
            var returnValue = ""
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.CHINA)
            returnValue = simpleDateFormat.format(date)
            return returnValue
        }

        /**
         * 根据相应格式的时间解析成date
         * @param strDate String
         * @return Date
         */
        private fun parse(strDate: String): Date? = parse(strDate, getDataPattern())

        private fun parse(strDate: String, pattern: String): Date? {
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.CHINA)
            return simpleDateFormat.parse(strDate)
        }

        /**
         * 在日期上增加月 日 时 分秒
         * @param date Date
         * @param n Int
         */
        fun addMonth(date: Date, n: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.MONTH, n)
            return cal.time
        }

        fun addDay(date: Date, n: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DATE, n)
            return cal.time
        }

        /**
         * 获取当前时间对应FORMAT_FULL格式的字符串
         * @return String
         */
        fun getTimeString(): String {
            val simpleDateFormat = SimpleDateFormat(FORMAT_FULL, Locale.CHINA)
            val cal = Calendar.getInstance()
            return simpleDateFormat.format(cal)
        }

        /**
         * 获取年份
         * @param date Date
         * @return String
         */
        fun getYear(date: Date): String {
            return format(date).substring(0, 4)
        }

        /**
         * 按默认格式的字符串距离今天的太难书
         * @return Int
         */
        fun countDays(date: String): Int {
            val t = Calendar.getInstance().time.time
            val c = Calendar.getInstance()
            c.time = parse(date)
            val t1 = c.time.time
            return ((t / 1000 - t1 / 1000) / 3600 / 24).toInt()
        }

        /**
         * 按自定义的格式计算给定时间距离现在的天数
         * @param date String
         * @param pattern String
         * @return Int
         */
        fun countDays(date: String, pattern: String): Int {
            val t = Calendar.getInstance().time.time
            val c = Calendar.getInstance()
            c.time = parse(date, pattern)
            val t1 = c.time.time
            return ((t / 1000 - t1 / 1000) / 3600 / 24).toInt()
        }

        /**
         * 格式化时间 判断是否时今天 昨天
         * @param time String
         * @return String
         */
        fun formatDateTime(time: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            if (TextUtils.isEmpty(time)) return ""
            val date = simpleDateFormat.parse(time)

            val cur = Calendar.getInstance()

            val totady = Calendar.getInstance()

            totady.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            totady.set(Calendar.MONTH, cur.get(Calendar.MONTH))
            totady.set(Calendar.DAY_OF_MONTH, cur.get(Calendar.DAY_OF_MONTH))
            totady.set(Calendar.HOUR_OF_DAY, 0)
            totady.set(Calendar.MINUTE, 0)
            totady.set(Calendar.SECOND, 0)

            val yesterday = Calendar.getInstance()

            yesterday[Calendar.YEAR] = cur.get(Calendar.YEAR)
            yesterday[Calendar.MONTH] = cur.get(Calendar.MONTH)
            yesterday[Calendar.DAY_OF_MONTH] = cur.get(Calendar.DAY_OF_MONTH) - 1
            yesterday[Calendar.HOUR_OF_DAY] = 0
            yesterday[Calendar.MINUTE] = 0
            yesterday[Calendar.SECOND] = 0

            cur.time = date
            if (cur.after(totady)) {
                return "今天" + time.split(" ")[1]
            } else if (cur.before(totady) && cur.after(yesterday)) {
                return "昨天" + time.split(" ")[1]
            } else {
                val index = time.indexOf(" ")
                return time.substring(0, index)
            }
        }

        /**
         * 将UTC-0时区时间字符串转换成用户时区时间的描述.
         *
         * @param strUtcTime UTC-0时区的时间
         * @param strInFmt   时间的输入格式
         * @param strOutFmt  时间的输出格式，若为null则输出格式与输入格式相同
         * @return 用户时区的时间描述.
         * @throws ParseException 时间转换异常
         */
        @Throws(ParseException::class)
        fun getUserZoneString(
            strUtcTime: String?,
            strInFmt: String?, strOutFmt: String?
        ): String? {
            if (StringUtils.isNull(strUtcTime)) {
                throw NullPointerException("参数strDate不能为空")
            } else if (StringUtils.isNull(strInFmt)) {
                throw NullPointerException("参数strInFmt不能为空")
            }
            val lUserMillis = getUserZoneMillis(strUtcTime, strInFmt)
            var strFmt = strInFmt
            if (!StringUtils.isNull(strOutFmt)) {
                strFmt = strOutFmt
            }
            return format(lUserMillis, strFmt)
        }

        /**
         * 格式化时间.
         *
         * @param lMillis  时间参数
         * @param strInFmt 时间格式
         * @return 对应的时间字符串
         */
        fun format(lMillis: Long, strInFmt: String?): String? {
            if (StringUtils.isNull(strInFmt)) {
                throw NullPointerException("参数strInFmt不能为空")
            }
            return DateFormat.format(strInFmt, lMillis) as String
        }

        /**
         * 将UTC-0时区时间字符串转换成用户时区时间距离1970-01-01的毫秒数.
         *
         * @param strUtcTime UTC-0时区的时间字符串
         * @param strInFmt   时间格式
         * @return 用户时区时间距离1970-01-01的毫秒数.
         * @throws ParseException 时间转换异常
         */
        @Throws(ParseException::class)
        fun getUserZoneMillis(
            strUtcTime: String?,
            strInFmt: String?
        ): Long {
            if (StringUtils.isNull(strUtcTime)) {
                throw NullPointerException("参数strUtcTime不能为空")
            } else if (StringUtils.isNull(strInFmt)) {
                throw NullPointerException("参数strInFmt不能为空")
            }
            val lUtcMillis = parseMillis(strUtcTime, strInFmt)
            val time = Time()
            time.setToNow()
            val lOffset = time.gmtoff * DateUtils.SECOND_IN_MILLIS
            return lUtcMillis + lOffset
        }

        /**
         * 转换时间格式，将字符串转换为距离1970-01-01的毫秒数.
         *
         * @param strDate  指定时间的字符串
         * @param strInFmt 时间字符串的格式
         * @return 指定时间字符串距离1970-01-01的毫秒数
         * @throws ParseException 时间转换异常
         */
        @Throws(ParseException::class)
        fun parseMillis(strDate: String?, strInFmt: String?): Long {
            if (StringUtils.isNull(strDate)) {
                throw NullPointerException("参数strDate不能为空")
            } else if (StringUtils.isNull(strInFmt)) {
                throw NullPointerException("参数strInFmt不能为空")
            }
            val sdf = SimpleDateFormat(
                strInFmt,
                Locale.getDefault()
            )
            val date = sdf.parse(strDate)
            return date.time
        }

        fun utc2BeiJingTime(message: String): String? {
            var beiJingTime: String? = message
            if (message.contains("#")) {
                val loginInfo =
                    message.split("#".toRegex()).toTypedArray()
                if (loginInfo != null && loginInfo.size >= 3) {
                    try {
                        val utcTime = loginInfo[1]
                        beiJingTime = getUserZoneString(utcTime, "HH:mm", null)
                        val repaceTimeStr = "#$utcTime#"
                        beiJingTime = message.replace(repaceTimeStr, beiJingTime!!, true)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            }
            return beiJingTime
        }

        /**
         * @param duration 秒钟
         */
        @JvmStatic
        fun format(duration: Long): String {
            var second = ""
            var minute = ""
            var time = ""
            //获取到时间
            val mm = duration / 60 //分
            val ss = duration % 60 //秒
            second = if (ss < 10) {
                "0$ss"
            } else {
                ss.toString()
            }
            minute = if (mm < 10) {
                "0$mm"
            } else {
                mm.toString() //分钟
            }
            time = "$minute:$second"
            return time
        }

        /**
         * @param duration 秒
         */
        @JvmStatic
        fun formatMillSecond(duration: Long): String {
            val minute = 1 * 60
            val hour = 60 * 60
            val day = 24 * 60 * 60

            return when {
                duration < hour -> {
                    String.format("%02d:%02d", duration / minute, duration % 60)
                }
                else -> {
                    String.format(
                        "%02d:%02d:%02d",
                        duration / hour,
                        (duration % hour) / minute,
                        duration % 60
                    )
                }
            }
        }



    }
}