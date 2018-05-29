package com.treasure.lovetravel.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类，包含大量通用方法
 *
 * @author Alex Huang
 * @email huangyu407@qq.com
 * @date 2014-9-24
 */
public class StringUtils {

    /**
     * 检查字符串是否为空(空字符串不算空)
     * <p>
     * <pre>
     * StringUtil.isEmpty(null) = true
     * StringUtil.isEmpty("") = true
     * StringUtil.isEmpty(" ") = false
     * StringUtil.isEmpty("bob") = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 检查字符串是否不为空(空字符串不算空)
     * <p>
     * <pre>
     * StringUtil.isNotEmpty(null) = false
     * StringUtil.isNotEmpty("") = false
     * StringUtil.isNotEmpty(" ") = true
     * StringUtil.isNotEmpty("bob") = true
     * StringUtil.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * 检查字符串是否为空(空字符串也算空) StringUtil.isBlank(null) = true StringUtil.isBlank("")
     * = true StringUtil.isBlank(" ") = true StringUtil.isBlank("bob") = false
     * StringUtil.isBlank("  bob  ") = false </pre>
     *
     * @param str
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否为空(空字符串也算空)
     * <p>
     * <pre>
     * StringUtil.isBlank(null) = true
     * 	StringUtil.isBlank("") = true
     * StringUtil.isBlank(" ") = true
     * StringUtil.isBlank("bob") = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return true;
        }
        if ("null".equals(str)){
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否不为空(空字符串也算空)
     * <p>
     * <pre>
     * StringUtil.isNotBlank(null) = false
     * StringUtil.isNotBlank("") = false
     * StringUtil.isNotBlank(" ") = false
     * StringUtil.isNotBlank("bob") = true
     * StringUtil.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(CharSequence str) {
        return !StringUtils.isBlank(str);
    }

    /**
     * 检查字符串是否不为空(空字符串也算空)
     * <p>
     * <pre>
     * StringUtil.isNotBlank(null) = false
     * StringUtil.isNotBlank("") = false
     * StringUtil.isNotBlank(" ") = false
     * StringUtil.isNotBlank("bob") = true
     * StringUtil.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    /**
     * 检查字符串是否全部有字母组成.
     * <p>
     * <pre>
     * StringUtil.isAlpha(null)   = false
     * StringUtil.isAlpha("")     = true
     * StringUtil.isAlpha("  ")   = false
     * StringUtil.isAlpha("abc")  = true
     * StringUtil.isAlpha("ab2c") = false
     * StringUtil.isAlpha("ab-c") = false
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        for (int i = str.length(); i > 0; i--) {
            if (Character.isLetter(str.charAt(i - 1)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否有数字[0~9]组成
     * <p>
     * <pre>
     * StringUtil.isDigits(&quot;&quot;) = false;
     * StringUtil.isDigits(null) = false;
     * StringUtil.isDigits(&quot;123&quot;) = true;
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isDigits(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否一个合法的数字
     * <p>
     * <pre>
     * StringUtil.isNumeric(&quot;&quot;) = false;
     * StringUtil.isNumeric(null) = false;
     * StringUtil.isNumeric(&quot;123&quot;) = true;
     * StringUtil.isNumeric(&quot;123f&quot;) = true;
     * StringUtil.isNumeric(&quot;123l&quot;) = true;
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str))
            return false;
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, validate it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another
        // digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't
        // pass
        return !allowSigns && foundDigit;
    }

    /**
     * 判断字符否是中文字符
     *
     * @param c
     * @return boolean
     */
    public static boolean isChineseChar(char c) {
        return (c >= '\u4E00' && c <= '\u9FA5');
    }

    /**
     * 计算字符串出现次数
     * <p>
     * <pre>
     * StringUtil.countMatches(null, *)       = 0
     * StringUtil.countMatches("", *)         = 0
     * StringUtil.countMatches("abba", null)  = 0
     * StringUtil.countMatches("abba", "")    = 0
     * StringUtil.countMatches("abba", "a")   = 2
     * StringUtil.countMatches("abba", "ab")  = 1
     * StringUtil.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str
     * @param sub
     * @return
     */
    public static int count(String str, String sub) {
        return count(str, sub, 0);
    }

    /**
     * 计算字符串出现次数
     *
     * @param source
     * @param sub
     * @param start  start index
     * @return
     */
    public static int count(String source, String sub, int start) {
        int count = 0;
        int j = start;
        int sublen = sub.length();
        if (sublen == 0) {
            return 0;
        }
        while (true) {
            int i = source.indexOf(sub, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + sublen;
        }
        return count;
    }

    /**
     * 计算字符串出现次数
     *
     * @param source
     * @param c
     * @return
     */
    public static int count(String source, char c) {
        return count(source, c, 0);
    }

    /**
     * 计算字符串出现次数
     *
     * @param source
     * @param c
     * @param start
     * @return
     */
    public static int count(String source, char c, int start) {
        int count = 0;
        int j = start;
        while (true) {
            int i = source.indexOf(c, j);
            if (i == -1) {
                break;
            }
            count++;
            j = i + 1;
        }
        return count;
    }

    /**
     * 往左截断指定字符
     * <p>
     * <pre>
     * StringUtil.substringBefore(null, *)      = null
     * StringUtil.substringBefore("", *)        = ""
     * StringUtil.substringBefore("abc", "a")   = ""
     * StringUtil.substringBefore("abcba", "b") = "a"
     * StringUtil.substringBefore("abc", "c")   = "ab"
     * StringUtil.substringBefore("abc", "d")   = "abc"
     * StringUtil.substringBefore("abc", "")    = ""
     * StringUtil.substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 往右截断指定字符
     * <p>
     * <pre>
     * StringUtil.substringAfter(null, *)      = null
     * StringUtil.substringAfter("", *)        = ""
     * StringUtil.substringAfter(*, null)      = ""
     * StringUtil.substringAfter("abc", "a")   = "bc"
     * StringUtil.substringAfter("abcba", "b") = "cba"
     * StringUtil.substringAfter("abc", "c")   = ""
     * StringUtil.substringAfter("abc", "d")   = ""
     * StringUtil.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 截取中间字符串
     * <p>
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 拼接给定的字符串集合为一个长字符串
     *
     * @param glue 连接符
     * @param wrap 外围包裹字符串
     * @param coll 字符串集合
     * @return
     */
    public final static String join(String glue, String wrap,
                                    Collection<String> coll) {
        StringBuilder sb = new StringBuilder();
        if (null == coll || coll.isEmpty())
            return sb.toString();
        boolean hasWrap = isNotEmpty(wrap);
        Iterator<String> it = coll.iterator();
        if (hasWrap) {
            sb.append(wrap).append(it.next()).append(wrap);
            while (it.hasNext())
                sb.append(glue).append(wrap).append(it.next()).append(wrap);

        } else {
            sb.append(it.next());
            while (it.hasNext())
                sb.append(glue).append(it.next());
        }
        return sb.toString();
    }

    /**
     * 只替换一次relpace
     * <p>
     * <pre>
     * StringUtil.replaceOnce(null, *, *)        = null
     * StringUtil.replaceOnce("", *, *)          = ""
     * StringUtil.replaceOnce("any", null, *)    = "any"
     * StringUtil.replaceOnce("any", *, null)    = "any"
     * StringUtil.replaceOnce("any", "", *)      = "any"
     * StringUtil.replaceOnce("aba", "a", null)  = "aba"
     * StringUtil.replaceOnce("aba", "a", "")    = "ba"
     * StringUtil.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @param text
     * @param searchString
     * @param replacement
     * @return
     */
    public static String replaceOnce(String text, String searchString,
                                     String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * <pre>
     * StringUtil.replace(null, *, *, *)         = null
     * StringUtil.replace("", *, *, *)           = ""
     * StringUtil.replace("any", null, *, *)     = "any"
     * StringUtil.replace("any", *, null, *)     = "any"
     * StringUtil.replace("any", "", *, *)       = "any"
     * StringUtil.replace("any", *, *, 0)        = "any"
     * StringUtil.replace("abaa", "a", null, -1) = "abaa"
     * StringUtil.replace("abaa", "a", "", -1)   = "b"
     * StringUtil.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtil.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtil.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtil.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text
     * @param searchString
     * @param replacement
     * @param max
     * @return
     */
    public static String replace(String text, String searchString,
                                 String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null
                || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 重复一段字符串并拼接起来
     * <p>
     * <pre>
     * StringUtil.repeat(null, 2) = null
     * StringUtil.repeat("", 0)   = ""
     * StringUtil.repeat("", 2)   = ""
     * StringUtil.repeat("a", 3)  = "aaa"
     * StringUtil.repeat("ab", 2) = "abab"
     * StringUtil.repeat("a", -2) = ""
     * </pre>
     *
     * @param source
     * @param count
     * @return
     */
    public static String repeat(String source, int count) {
        StringBuilder result = new StringBuilder(count * source.length());
        while (count > 0) {
            result.append(source);
            count--;
        }
        return result.toString();
    }

    /**
     * 重复一段字符串并拼接起来
     *
     * @param c
     * @param count
     * @return
     */
    public static String repeat(char c, int count) {
        StringBuilder result = new StringBuilder(count);
        while (count > 0) {
            result.append(c);
            count--;
        }
        return result.toString();
    }

    /**
     * 把一个英文字符串的第一个字符变成大写字母
     * <p>
     * <pre>
     * StringUtil.capitalize(null)  = null
     * StringUtil.capitalize("")    = ""
     * StringUtil.capitalize("cat") = "Cat"
     * StringUtil.capitalize("cAt") = "CAt"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * 把一个英文字符串的第一个字符变成小写字母
     * <p>
     * <pre>
     * StringUtil.uncapitalize(null)  = null
     * StringUtil.uncapitalize("")    = ""
     * StringUtil.uncapitalize("Cat") = "cat"
     * StringUtil.uncapitalize("CAT") = "cAT"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * 检查一个字符串指定前缀开始,忽略大小写
     * <p>
     * <pre>
     * StringUtil.startsWithIgnoreCase(null, null)      = true
     * StringUtil.startsWithIgnoreCase(null, "abcdef")  = false
     * StringUtil.startsWithIgnoreCase("abc", null)     = false
     * StringUtil.startsWithIgnoreCase("abc", "abcdef") = true
     * StringUtil.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * 检查一个字符串指定前缀开始
     *
     * @param str
     * @param prefix
     * @param ignoreCase
     * @return
     */
    private static boolean startsWith(String str, String prefix,
                                      boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        return prefix.length() <= str.length() && str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    /**
     * 检查一个字符串指定字符串结尾,忽略大小写
     * <p>
     * <pre>
     * StringUtil.endsWithIgnoreCase(null, null)      = true
     * StringUtil.endsWithIgnoreCase(null, "abcdef")  = false
     * StringUtil.endsWithIgnoreCase("def", null)     = false
     * StringUtil.endsWithIgnoreCase("def", "abcdef") = true
     * StringUtil.endsWithIgnoreCase("def", "ABCDEF") = false
     * </pre>
     *
     * @param str
     * @param suffix
     * @return
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * 检查一个字符串指定字符串结尾
     *
     * @param str
     * @param suffix
     * @param ignoreCase
     * @return
     */
    private static boolean endsWith(String str, String suffix,
                                    boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0,
                suffix.length());
    }

    /**
     * 把null的字符串转换为"", 否则原样返回
     *
     * @param str
     */
    public static String nullToEmpty(String str) {
        if (null == str) {
            return "";
        }
        return str;
    }

    /**
     * 作为分隔方便的方法来返回集合
     *
     * @param coll
     * @param delim
     */
    public static String toDelimitedString(Collection<String> coll, String delim) {
        return toDelimitedString(coll, delim, "", "");
    }

    /**
     * 作为分隔方便的方法来返回集合
     *
     * @param coll
     * @param delim
     * @param prefix
     * @param suffix
     */
    public static String toDelimitedString(Collection<String> coll,
                                           String delim, String prefix, String suffix) {
        if (coll == null || coll.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * 删除空字符串
     * <p>
     * <pre>
     * StringUtil.deleteWhitespace(null)         = null
     * StringUtil.deleteWhitespace("")           = ""
     * StringUtil.deleteWhitespace("abc")        = "abc"
     * StringUtil.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    /**
     * Collection转成数组
     *
     * @param collection
     * @return
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Enumeration转成数组
     *
     * @param enumeration
     * @return
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }

    public static boolean isAnyEmpty(String... array) {
        for (String str : array) {
            if (isEmpty(str))
                return true;
        }
        return false;
    }
    public static boolean isAlphanumeric(String str, int minInclude,
                                         int maxInclude) {
        Pattern pattern = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{" + minInclude
                        + "," + maxInclude + "}$");
        return pattern.matcher(str).matches();
    }

    public static boolean matchPwd(String str) {
        Pattern pattern = Pattern.compile("^[0-9A-Za-z]{6,20}$");
        return pattern.matcher(str).matches();
    }

    private static final Pattern PHONE_PATTERN = Pattern
            .compile("^(1[0-9][0-9])\\d{8}$");

    private static final Pattern PHONE_PATTERN2 = Pattern
            .compile("(1[0-9][0-9])\\d{8}");
    private static final Pattern IDCARD = Pattern
            .compile("(\\d{15}$)|(\\d{18}$)|(\\d{17}(\\d|X|x)$)");
    public static String getPhoneNum(String phoneStr) {
        if (StringUtils.isBlank(phoneStr))
            return null;

        Matcher matcher = PHONE_PATTERN2.matcher(phoneStr);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static boolean isPhone(String phoneStr) {
        return PHONE_PATTERN.matcher(phoneStr).matches();
    }

    public static String encode(String str) {
        if (str == null)
            return null;

        try {
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String appendUrlParams(String url, Map<String, String> params) {
        if (url == null)
            return null;

        if (params == null || params.size() == 0)
            return url;

        if (url.indexOf("?") == -1)
            url += "?";

        int i = 0;
        for (Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                if (i > 0) {
                    url += "&";
                }

                url += StringUtils.encode(key) + "="
                        + StringUtils.encode(value);
                i++;
            }
        }
        return url;
    }

    /**
     * 是否字母
     *
     * @param c
     * @return
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param String s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int letterLength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 根据字母长度截取，并补省略符号...
     *
     * @param s
     * @param length
     * @return
     */
    public static String ellipsisByLetterLength(String s, int length) {
        return ellipsisByLetterLength(s, length, "...");
    }
/**
 *
 * 校验身份证
 */
public static boolean isCardNo(String s) {
    return IDCARD.matcher(s).matches();
}


    /**
     * 根据字母长度截取，并补省略符号
     *
     * @param s
     * @param length
     * @return
     */
    public static String ellipsisByLetterLength(String s, int length,
                                                String ellipsis) {
        if (s == null)
            return s;

        if (letterLength(s) <= length) {
            return s;
        }

        char[] arr = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (!isLetter(arr[i])) {
                k = k + 2;
            } else {
                k++;
            }
            if (k >= length)
                break;
        }
        return sb.toString() + ellipsis;
    }

    public static String float2Text(float value) {
        if (value == (int) value)
            return ((int) value) + "";

        return value + "";
    }


    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script= Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style= Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html= Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 实现文本复制功能
     * @param content
     */
    public static void copy(String content, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * String 转 byte[]
     */
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }
    /**
     * byte[] 转 String
     */
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }
    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    /**
     * base64转为String
     * @param base64Data
     * @return
     */
    public static String base64ToString(String base64Data) {
        byte[] decode = Base64.decode(base64Data, Base64.DEFAULT);
        return decode.toString();
    }
}
