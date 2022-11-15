package com.example.library_common.utils

import android.content.Context
import android.text.TextUtils
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 16
 */
class StringUtils private constructor(){
    companion object{
        /**
         * 判断字符串是否为空或空字符
         *
         * @param strSource 源字符串
         * @return true表示为空，false表示不为空
         */
        fun isNull(strSource: String?): Boolean {
            return strSource == null || "" == strSource.trim { it <= ' ' }
        }

        /**
         * is null or its length is 0 or it is made by space
         *
         *
         * <pre>
         * isBlank(null) = true;
         * isBlank(&quot;&quot;) = true;
         * isBlank(&quot;  &quot;) = true;
         * isBlank(&quot;a&quot;) = false;
         * isBlank(&quot;a &quot;) = false;
         * isBlank(&quot; a&quot;) = false;
         * isBlank(&quot;a b&quot;) = false;
        </pre> *
         *
         * @param str
         * @return if string is null or its size is 0 or it is made by space, return true, else return false.
         */
        fun isBlank(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.length == 0
        }

        /**
         * 获取字符串长度的安全方法
         * get length of CharSequence
         *
         *
         * <pre>
         * length(null) = 0;
         * length(\"\") = 0;
         * length(\"abc\") = 3;
        </pre> *
         *
         * @param str
         * @return if str is null or empty, return 0, else return [CharSequence.length].
         */
        fun size(str: CharSequence?): Int {
            return str?.length ?: 0
        }

        /**
         * null Object to empty string
         *
         *
         * <pre>
         * nullStrToEmpty(null) = &quot;&quot;;
         * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
         * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
        </pre> *
         *
         * @param str
         * @return
         */
        fun nullStrToEmpty(str: Any?): String? {
            return if (str == null) "" else if (str is String) str else str.toString()
        }

        /**
         * 转换首字母为大写
         * capitalize first letter
         *
         *
         * <pre>
         * capitalizeFirstLetter(null)     =   null;
         * capitalizeFirstLetter("")       =   "";
         * capitalizeFirstLetter("2ab")    =   "2ab"
         * capitalizeFirstLetter("a")      =   "A"
         * capitalizeFirstLetter("ab")     =   "Ab"
         * capitalizeFirstLetter("Abc")    =   "Abc"
        </pre> *
         *
         * @param str
         * @return
         */
        fun capitalizeFirstLetter(str: String): String? {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val c = str[0]
            return if (!Character.isLetter(c) || Character.isUpperCase(c)) str else Character.toUpperCase(
                c
            ).toString() + str.substring(1)
        }

        /**
         * 是否是Utf-8字符串
         * encoded in utf-8
         *
         *
         * <pre>
         * utf8Encode(null)        =   null
         * utf8Encode("")          =   "";
         * utf8Encode("aa")        =   "aa";
         * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
        </pre> *
         *
         * @param str
         * @return
         * @throws UnsupportedEncodingException if an error occurs
         */
        fun utf8Encode(str: String): String? {
            return if (!TextUtils.isEmpty(str) && str.toByteArray().size != str.length) {
                try {
                    URLEncoder.encode(str, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException("UnsupportedEncodingException occurred. ", e)
                }
            } else str
        }

        /**
         * 使用utf-8转码
         * encoded in utf-8, if exception, return defultReturn
         *
         * @param str
         * @param defultReturn
         * @return
         */
        fun utf8Encode(str: String, defultReturn: String?): String? {
            return if (!TextUtils.isEmpty(str) && str.toByteArray().size != str.length) {
                try {
                    URLEncoder.encode(str, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    defultReturn
                }
            } else str
        }

        /**
         * 获取<a></a>标签内的内容
         * get innerHtml from href
         *
         *
         * <pre>
         * getHrefInnerHtml(null)                                  = ""
         * getHrefInnerHtml("")                                    = ""
         * getHrefInnerHtml("mp3")                                 = "mp3";
         * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
         * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
         * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
         * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
         * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
         * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
         * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
         * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
         * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
        </pre> *
         *
         * @param href
         * @return
         *  * if href is null, return ""
         *  * if not match regx, return source
         *  * return the last string that match regx
         *
         */
        fun getHrefInnerHtml(href: String?): String? {
            if (TextUtils.isEmpty(href)) {
                return ""
            }
            val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
            val hrefPattern =
                Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE)
            val hrefMatcher = hrefPattern.matcher(href)
            return if (hrefMatcher.matches()) {
                hrefMatcher.group(1)
            } else href
        }

        /**
         * Html文本格式转码
         * process special char in html
         *
         *
         * <pre>
         * htmlEscapeCharsToString(null) = null;
         * htmlEscapeCharsToString("") = "";
         * htmlEscapeCharsToString("mp3") = "mp3";
         * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
         * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
         * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
         * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
         * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
        </pre> *
         *
         * @param source
         * @return
         */
        fun htmlEscapeCharsToString(source: String): String? {
            return if (TextUtils.isEmpty(source)) source else source.replace(
                "&lt;".toRegex(),
                "<"
            ).replace("&gt;".toRegex(), ">")
                .replace("&amp;".toRegex(), "&").replace("&quot;".toRegex(), "\"")
        }

        /**
         * 全角字符 转为为半角字符
         * transform full width char to half width char
         *
         *
         * <pre>
         * fullWidthToHalfWidth(null) = null;
         * fullWidthToHalfWidth("") = "";
         * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
         * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
        </pre> *
         *
         * @param s
         * @return
         */
        fun fullWidthToHalfWidth(s: String): String? {
            if (TextUtils.isEmpty(s)) {
                return s
            }
            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i] == 12288.toChar()) {
                    source[i] = ' '
                    // } else if (source[i] == 12290) {
                    // source[i] = '.';
                } else if (source[i] >= 65281.toChar() && source[i] <= 65374.toChar()) {
                    source[i] = (source[i] - 65248)
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }

        /**
         * 将半角字符转换为全角字符
         * transform half width char to full width char
         *
         *
         * <pre>
         * halfWidthToFullWidth(null) = null;
         * halfWidthToFullWidth("") = "";
         * halfWidthToFullWidth(" ") = new String(new char[] {12288});
         * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
        </pre> *
         *
         * @param s
         * @return
         */
        fun halfWidthToFullWidth(s: String): String? {
            if (TextUtils.isEmpty(s)) {
                return s
            }
            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i] == ' ') {
                    source[i] = 12288.toChar()
                    // } else if (source[i] == '.') {
                    // source[i] = (char)12290;
                } else if (source[i] >= 33.toChar() && source[i] <= 126.toChar()) {
                    source[i] = (source[i] + 65248)
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }

        private const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
        private const val DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
        private val dateFormater: ThreadLocal<SimpleDateFormat?> =
            object : ThreadLocal<SimpleDateFormat?>() {
                override fun initialValue(): SimpleDateFormat {
                    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                }
            }

        private val dateFormater2: ThreadLocal<SimpleDateFormat?> =
            object : ThreadLocal<SimpleDateFormat?>() {
                override fun initialValue(): SimpleDateFormat {
                    return SimpleDateFormat("yyyy-MM-dd")
                }
            }

        /**
         * 判断字符串是否为空
         *
         * @param str null、“ ”、“null”都返回true
         * @return boolean
         */
        fun isNullString(str: String?): Boolean {
            return null == str || isBlank(str.trim { it <= ' ' }) || "null" == str.trim { it <= ' ' }
                .toLowerCase()
        }

        fun isEmpty(cs: CharSequence?): Boolean {
            return cs == null || cs.length == 0
        }

        fun isBlank(cs: CharSequence?): Boolean {
            var strLen: Int=0
            if (cs == null || cs.length.also { strLen = it } == 0) {
                return true
            }
            for (i in 0 until strLen) {
                if (Character.isWhitespace(cs[i]) == false) {
                    return false
                }
            }
            return true
        }

        /**
         * 格式化字符串 如果为空，返回“”
         *
         * @param str
         * @return String
         */
        fun formatString(str: String?): String? {
            return if (isNullString(str)) {
                ""
            } else {
                str
            }
        }

        /**
         * 获得文件名称
         *
         * @param path
         * @return String
         */
        fun getFileName(path: String): String? {
            if (isNullString(path)) return null
            val bingindex = path.lastIndexOf("/")
            return path.substring(bingindex + 1, path.length)
        }

        /**
         * 获得文件名称前缀
         *
         * @param path
         * @return String
         */
        fun getFileNamePrefix(path: String): String? {
            if (isNullString(path)) return null
            val bingindex = path.lastIndexOf("/")
            val endindex = path.lastIndexOf(".")
            return path.substring(bingindex + 1, endindex)
        }

        private val numericPattern =
            Pattern.compile("^[0-9\\-]+$")

        /**
         * 判断字符串是否是数字
         *
         * @param src
         * @return boolean
         */
        fun isNumeric(src: String?): Boolean {
            var return_value = false
            if (src != null && src.length > 0) {
                val m = numericPattern.matcher(src)
                if (m.find()) {
                    return_value = true
                }
            }
            return return_value
        }

        /**
         * 自动命名文件,命名文件格式如：IP地址+时间戳+三位随机数 .doc
         *
         * @param ip       ip地址
         * @param fileName 文件名
         * @return String
         */
        fun getIPTimeRandName(ip: String?, fileName: String): String? {
            val buf = StringBuilder()
            if (ip != null) {
                val str = ip.split("\\.".toRegex()).toTypedArray()
                for (aStr in str) {
                    buf.append(addZero(aStr, 3))
                }
            } // 加上IP地址
            buf.append(getTimeStamp()) // 加上日期
            val random = Random()
            for (i in 0..2) {
                buf.append(random.nextInt(10)) // 取三个随机数追加到StringBuffer
            }
            buf.append(".").append(getFileExt(fileName)) // 加上扩展名
            return buf.toString()
        }

        /**
         * 自动命名文件,命名文件格式如：时间戳+三位随机数 .doc
         *
         * @param fileName
         * @return String
         */
        fun getTmeRandName(fileName: String): String? {
            return getIPTimeRandName(null, fileName)
        }

        fun addZero(str: String?, len: Int): String? {
            val s = StringBuilder()
            s.append(str)
            while (s.length < len) {
                s.insert(0, "0")
            }
            return s.toString()
        }

        /**
         * 获得时间戳 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
         *
         * @return String
         */
        fun getTimeStamp(): String? {
            val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS")
            return sdf.format(Date())
        }

        /**
         * 获得文件扩展名
         *
         * @param filename
         * @return String
         */
        fun getFileExt(filename: String): String? {
            val i = filename.lastIndexOf(".") // 返回最后一个点的位置
            return filename.substring(i + 1)
        }

        /**
         * 将url进行utf-8编码
         *
         * @param url
         * @return String
         */
        fun encodeURL(url: String?): String? {
            return try {
                URLEncoder.encode(url, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        }

        /**
         * 将url进行utf-8解码
         *
         * @param url
         * @return String
         */
        fun decodeURL(url: String?): String? {
            return try {
                URLDecoder.decode(url, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        }

        /**
         * 格式化日期字符串 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
         *
         * @param date
         * @param pattern
         * @return String
         */
        fun formatDate(date: Date?, pattern: String?): String? {
            val format = SimpleDateFormat(pattern)
            return format.format(date)
        }

        /**
         * 格式化日期字符串 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
         */
        fun formatDate(date: Date?): String? {
            return formatDate(date, DEFAULT_DATE_PATTERN)
        }

        /**
         * 获取当前时间 也可以用 ：commons-lang.rar 下的：DateFormatUtils类 更为简单
         *
         * @return String
         */
        fun getDate(): String? {
            return formatDate(Date(), DEFAULT_DATE_PATTERN)
        }

        /**
         * 获取当前时间
         *
         * @return String
         */
        fun getDateTime(): String? {
            return formatDate(Date(), DEFAULT_DATETIME_PATTERN)
        }

        /**
         * 格式化日期时间字符串
         */
        fun formatDateTime(date: Date?): String? {
            return formatDate(date, DEFAULT_DATETIME_PATTERN)
        }

        /**
         * 格式化json格式日期
         *
         * @param date
         * @return String
         */
        fun formatJsonDateTime(date: JSONObject): String? {
            var result: Date? = null
            try {
                result = Date(
                    date.getInt("year"), date.getInt("month"),
                    date.getInt("date"), date.getInt("hours"),
                    date.getInt("minutes"), date.getInt("seconds")
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result?.let { formatDateTime(it) } ?: ""
        }

        /**
         * 将字符串集合 变为以 separator 分割的字符串
         *
         * @param array     字符串集合
         * @param separator 分隔符
         * @return String
         */
        fun join(
            array: ArrayList<String?>?,
            separator: String?
        ): String? {
            val result = StringBuilder()
            if (array != null && array.size > 0) {
                for (str in array) {
                    result.append(str)
                    result.append(separator)
                }
                result.delete(result.length - 1, result.length)
            }
            return result.toString()
        }

        /**
         * 压缩字符串
         *
         * @param str
         * @return String
         * @throws IOException
         */
        @Throws(IOException::class)
        fun compress(str: String?): String? {
            if (str == null || str.length == 0) {
                return str
            }
            val out = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(out)
            gzip.write(str.toByteArray())
            gzip.close()
            return out.toString("ISO-8859-1")
        }

        /**
         * 解压缩字符串
         *
         * @param str
         * @return String
         * @throws IOException
         */
        @Throws(IOException::class)
        fun uncompress(str: String?): String? {
            if (str == null || str.length == 0) {
                return str
            }
            val out = ByteArrayOutputStream()
            val `in` = ByteArrayInputStream(
                str.toByteArray(charset("ISO-8859-1"))
            )
            val gunzip = GZIPInputStream(`in`)
            val buffer = ByteArray(256)
            var n: Int
            while (gunzip.read(buffer).also { n = it } >= 0) {
                out.write(buffer, 0, n)
            }
            return out.toString("UTF-8")
        }

        /**
         * **description :** 去除特殊字符或将所有中文标号替换为英文标号
         *
         * @param input
         * @return String
         */
        fun stringFilter(input: String?): String? {
            var input = input ?: return null
            input = input.replace("【".toRegex(), "[").replace("】".toRegex(), "]")
                .replace("！".toRegex(), "!").replace("：".toRegex(), ":") // 替换中文标号
            val regEx = "[『』]" // 清除掉特殊字符
            val p = Pattern.compile(regEx)
            val m = p.matcher(input)
            return m.replaceAll("").trim { it <= ' ' }
        }

        /**
         * **description :** 半角字符转全角字符
         *
         * @param input
         * @return String
         */
        fun ToDBC(input: String?): String? {
            if (input == null) return null
            val c = input.toCharArray()
            for (i in c.indices) {
                if (c[i] == 12288.toChar()) {
                    c[i] = 32.toChar()
                    continue
                }
                if (c[i] > 65280.toChar() && c[i] < 65375.toChar()) c[i] =
                    (c[i] - 65248)
            }
            return String(c)
        }

        /**
         * 判断字符串"oldString"是否为null
         *
         * @param oldString 需要判断的字符串
         * @return String 如果"oldString"为null返回空值"",否则返回"oldString"
         */
        fun getString(oldString: String?): String? {
            return if (oldString == null || "null" == oldString) {
                ""
            } else {
                oldString.trim { it <= ' ' }
            }
        }

        /**
         * 将一实数转换成字符串并返回
         *
         * @param d 实数
         * @return String
         */
        fun getString(d: Double): String? {
            return d.toString()
        }

        /**
         * 得到文件的后缀名(扩展名)
         *
         * @param name
         * @return String 后缀名
         */
        @Throws(Exception::class)
        fun getAfterPrefix(name: String): String? {
            return name.substring(name.lastIndexOf(".") + 1, name.length)
        }

        /**
         * 分割字符串
         *
         * @param values 要分割的内容
         * @param limit  分隔符 例：以“,”分割
         * @return String[] 返回数组，没有返回null
         */
        fun spilctMoreSelect(
            values: String,
            limit: String
        ): Array<String?>? {
            return if (isNullOrEmpty(values)) {
                null
            } else values.trim { it <= ' ' }.split(limit.toRegex()).toTypedArray()
        }

        /**
         * 将字符串数组转化为字符串
         *
         * @param needvalue
         * @return String 返回字符串，否则返回null
         */
        fun arr2Str(needvalue: Array<String>?): String? {
            var str = ""
            return if (needvalue != null) {
                val len = needvalue.size
                for (i in 0 until len) {
                    str += if (i == len - 1) {
                        needvalue[i]
                    } else {
                        needvalue[i] + ","
                    }
                }
                str
            } else {
                null
            }
        }

        fun arr2int(arr: Array<String>?): Int {
            return if (arr != null && arr.size > 0) {
                arr[1].toInt()
            } else -1
        }

        /**
         * 判断字符串是否为空或空符串。
         *
         * @param str 要判断的字符串。
         * @return String 返回判断的结果。如果指定的字符串为空或空符串，则返回true；否则返回false。
         */
        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.length == 0
        }

        /**
         * 去掉字符串两端的空白字符。因为String类里边的trim()方法不能出现null.trim()的情况，因此这里重新写一个工具方法。
         *
         * @param str 要去掉空白的字符串。
         * @return String 返回去掉空白后的字符串。如果字符串为null，则返回null；否则返回str.trim()。 *
         */
        fun trim(str: String?): String? {
            return str?.trim { it <= ' ' }
        }

        /**
         * 更具配置的string.xml 里的id，得到内容
         *
         * @param context
         * @param id
         * @return String
         */
        fun getValueById(context: Context, id: Int): String? {
            return context.resources.getString(id)
        }

        /**
         * 用于文中强制换行的处理
         *
         * @param oldstr
         * @return String
         */
        fun replaceStr(oldstr: String): String? {
            var oldstr = oldstr
            oldstr = oldstr.replace("\n".toRegex(), "<br>") // 替换换行
            oldstr = oldstr.replace("\r\n".toRegex(), "<br>") // 替换回车换行
            oldstr = oldstr.replace(" ".toRegex(), "&nbsp;" + " ") // 替换空格
            return oldstr
        }

        /**
         * 判断是否是数字
         *
         * @param c
         * @return boolean
         */
        fun isNum(c: Char): Boolean {
            return c.toInt() >= 48 && c.toInt() <= 57
        }

        /**
         * 获得题号 例如：2.本文选自哪篇文章？ 提取题号中的数字 2
         *
         * @param content
         * @return int
         */
        fun getThemeNum(content: String): Int {
            var tnum = -1
            if (isNullOrEmpty(content)) return tnum
            val a = content.indexOf(".")
            if (a > 0) {
                val num = content.substring(0, a)
                tnum = try {
                    num.toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    return tnum
                }
            }
            return tnum
        }

        // 添加自己的字符串操作

        // 添加自己的字符串操作
        fun dealDigitalFlags(str: String?): String? {
            var result: String? = ""
            if (str == null || str.length < 0) {
                return null
            } else {
                val len = str.length
                for (i in 0 until len) {
                    var tmp = str.substring(i, i + 1)
                    if (tmp == "+" || tmp == "*" || tmp == "=") {
                        tmp = " $tmp "
                    }
                    result += tmp
                }
            }
            return result
        }

        /**
         * 截取序号 例如：01026---->26
         *
         * @param oldnum
         * @return String
         */
        fun detailNum(oldnum: String): String? {
            if (isNullOrEmpty(oldnum)) return oldnum
            val newnum = oldnum.toInt()
            return "$newnum."
        }

        @Throws(Exception::class)
        fun getStoreArr(arr: Array<String>): Array<String> {
            var temp: String
            for (i in arr.indices) {
                for (j in arr.size - 1 downTo i + 1) {
                    val a = arr[i].toInt()
                    val b = arr[j].toInt()
                    if (a > b) {
                        temp = arr[i]
                        arr[i] = arr[j]
                        arr[j] = temp
                    }
                }
            }
            return arr
        }

        /**
         * 给数字字符串排序 如：3，1，2 --->1，2，3
         *
         * @param str
         * @return String
         * @throws Exception
         */
        fun resetStoreNum(str: String?): String? {
            var value = ""
            try {
                if (str == null || str.length < 1) return value
                val results = str.split(",".toRegex()).toTypedArray()
                val newarr = getStoreArr(results)
                for (aNewarr in newarr) {
                    value += "$aNewarr,"
                }
                value = value.substring(0, value.length - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }

        /**
         * 判断数组中是否包含某个值
         *
         * @param srcValue
         * @param values
         * @return boolean
         */
        fun arrIsValue(
            srcValue: String,
            values: Array<String>?
        ): Boolean {
            if (values == null) {
                return false
            }
            for (value in values) {
                if (value == srcValue) {
                    return true
                }
            }
            return false
        }

        /**
         * 获得"."之后的所有内容
         *
         * @param content 原字符串
         * @return String
         */
        fun DeleteOriNumber(content: String): String? {
            return if (content.trim { it <= ' ' }.length > 1) {
                val index = content.indexOf(".")
                content.substring(index + 1, content.length)
            } else {
                content
            }
        }

        /**
         * GBK编码
         *
         * @param content
         * @return String
         */
        fun convertToGBK(content: String): String? {
            var content = content
            if (!isEmpty(content)) {
                try {
                    content = String(content.toByteArray(), Charset.forName("GBK"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
            return content
        }

        private fun trimSpaces(IP: String): String { // 去掉IP字符串前后所有的空格
            var IP = IP
            while (IP.startsWith(" ")) {
                IP = IP.substring(1, IP.length).trim { it <= ' ' }
            }
            while (IP.endsWith(" ")) {
                IP = IP.substring(0, IP.length - 1).trim { it <= ' ' }
            }
            return IP
        }

        /**
         * 判断是否是一个IP
         *
         * @param IP
         * @return boolean
         */
        fun isIp(IP: String): Boolean {
            var IP = IP
            var b = false
            IP = trimSpaces(IP)
            if (IP.matches(Regex("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))) {
                val s = IP.split("\\.".toRegex()).toTypedArray()
                if (s[0].toInt() < 255) if (s[1].toInt() < 255) if (s[2]
                        .toInt() < 255
                ) if (s[3].toInt() < 255) b = true
            }
            return b
        }

        /**
         * 将字符串转位日期类型
         *
         * @param sdate
         * @return Date
         */
        fun toDate(sdate: String?): Date? {
            return try {
                dateFormater.get()?.parse(sdate)
            } catch (e: ParseException) {
                null
            }
        }

        /**
         * 方法: distanceSize
         * 描述: 计算距离
         *
         * @param distance 距离数 单位千米
         * @return String  转换后的距离
         */
        fun distanceSize(distance: Double): String? {
            if (distance < 1.0) return (distance * 1000).toInt().toString() + "m"
            var dd = "0"
            try {
                val fnum = DecimalFormat("##0.00")
                dd = fnum.format(distance)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dd + "km"
        }

        /**
         * 方法: replaceResult
         * 描述: 替换结果字符串
         *
         * @param content
         * @return String    返回类型
         */
        fun replaceResult(content: String): String? {
            var content = content
            if (!isEmpty(content)) content =
                content.replace("\\", "").replace("\"{", "{").replace("}\"", "}")
            return content
        }

        /**
         * 方法: checkPhone
         * 描述: 提取电话号码
         *
         * @param content
         * @return ArrayList<String>    返回类型
        </String> */
        fun checkPhone(content: String?): ArrayList<String>? {
            val list = ArrayList<String>()
            if (isEmpty(content)) return list
            val p =
                Pattern.compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}")
            val m = p.matcher(content)
            while (m.find()) {
                list.add(m.group())
            }
            return list
        }

        /**
         *
         * 描述:保留一位小数
         *
         * @param value
         * @return 设定文件
         */
        fun parseStr(value: String): String? {
            if (isNullString(value)) return "0.0"
            val df = DecimalFormat("######0.0")
            val mvalue = value.toDouble()
            return df.format(mvalue)
        }

        fun parseStr2(value: String): String? {
            if (isNullString(value)) return "--"
            val df = DecimalFormat("######0.0")
            val mvalue = value.toDouble()
            val mStr = df.format(mvalue)
            return if (mStr == "0" || mStr == "0.0") {
                "--"
            } else mStr
        }

        fun parseStr(value: Double): String? {
            if (value == 0.0) return "0.0"
            val df = DecimalFormat("######0.0")
            return df.format(value.toString().toDouble())
        }

        /**
         * 处理自动换行问题
         *
         * @param input 字符串
         * @return 设定文件
         */
        fun ToWrap(input: String): String? {
            val c = input.toCharArray()
            for (i in c.indices) {
                if (c[i] == 12288.toChar()) {
                    c[i] = 32.toChar()
                    continue
                }
                if (c[i] > 65280.toChar() && c[i] < 65375.toChar()) c[i] =
                    (c[i] - 65248)
            }
            return String(c)
        }

        /**
         * 时间显示转换
         *
         * @param duration   时间区间 0-59
         * @param isShowZero 小于10是否显示0 如：09
         * @return
         */
        fun durationShow(duration: Int, isShowZero: Boolean): String? {
            val showStr: String
            showStr = if (isShowZero) {
                if (duration < 10) {
                    "0$duration"
                } else {
                    duration.toString()
                }
            } else {
                duration.toString()
            }
            return showStr
        }

        fun fromTimeString(s: String): Long {
            var s = s
            if (s.lastIndexOf(".") != -1) {
                s = s.substring(0, s.lastIndexOf("."))
            }
            val split = s.split(":".toRegex()).toTypedArray()
            return if (split.size == 3) {
                split[0].toLong() * 3600L + split[1]
                    .toLong() * 60L + split[2].toLong()
            } else if (split.size == 2) {
                split[0].toLong() * 60L + split[0].toLong()
            } else {
                throw IllegalArgumentException("Can\'t parse time string: $s")
            }
        }

        fun toTimeString(seconds: Long): String? {
            var seconds = seconds
            seconds = seconds / 1000
            val hours = seconds / 3600L
            val remainder = seconds % 3600L
            val minutes = remainder / 60L
            val secs = remainder % 60L
            return if (hours == 0L) {
                (if (minutes < 10L) "0" else "") + minutes + ":" + (if (secs < 10L) "0" else "") + secs
            } else (if (hours < 10L) "0" else "") + hours + ":" + (if (minutes < 10L) "0" else "") + minutes + ":" + (if (secs < 10L) "0" else "") + secs
        }

        /**
         * 是否含有表情符
         * false 为含有表情符
         */
        fun checkFace(checkString: String): Boolean {
            val reg = "^([a-z]|[A-Z]|[0-9]|[\u0000-\u00FF]|[\u2000-\uFFFF]){1,}$"
            val pattern = Pattern.compile(reg)
            val matcher =
                pattern.matcher(checkString.replace(" ".toRegex(), ""))
            return matcher.matches()
        }

    }
}