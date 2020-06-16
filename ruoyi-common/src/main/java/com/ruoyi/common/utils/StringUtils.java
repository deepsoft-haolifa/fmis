package com.ruoyi.common.utils;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import com.ruoyi.common.core.text.StrFormatter;

/**
 * 字符串工具类
 * 
 * @author ruoyi
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线 */
    private static final char SEPARATOR = '_';


    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }
    /**
     * 去空格
     */
    public static Double toDouble(String str)
    {
        String s = (str == null ? "" : str.trim());
        try {
            return Double.parseDouble(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Double(0);
    }
    public static Long toLong(String str)
    {
        String s = (str == null ? "0" : str.trim());
        try {
            return Long.parseLong(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0L;
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     * 
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     * 
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty())
        {
            // 没必要转换
            return "";
        }
        else if (!name.contains("_"))
        {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty())
            {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /*
     * 如果是小数，保留两位，非小数，保留整数
     * @param number
     */
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }
    public static String getDoubleString0(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("#");
            numberStr = df.format(number);
        }
        return numberStr;
    }

    // 大写数字
    private final static String[] STR_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍",
            "陆", "柒", "捌", "玖" };
    // 整数单位
    private final static String[] STR_UNIT = { "", "拾", "佰", "仟", "万", "拾",
            "佰", "仟", "亿", "拾", "佰", "仟" };
    // 小数单位
    private final static String[] STR_UNIT2 = { "角", "分", "厘" };

    private static String getInteger(String num) {
        if (num.indexOf(".")!= -1) {                                //判断是否包含小数点
            num = num.substring(0,num.indexOf("."));
        }
        num = new StringBuffer(num).reverse().toString();           //反转字符串
        StringBuffer temp = new StringBuffer();                     //创建一个StringBuffer对象
        for(int i=0;i<num.length();i++) {
            temp.append(STR_UNIT[i]);                               //加入单位
            temp.append(STR_NUMBER[num.charAt(i) - 48]);
        }
        num = temp.reverse().toString();//反转字符串
        num = numReplace(num,"零拾","零");// 替换字符串的字符
        num = numReplace(num,"零佰","零");// 替换字符串的字符
        num = numReplace(num,"零仟","零");// 替换字符串的字符
        num = numReplace(num,"零万","万");// 替换字符串的字符
        num = numReplace(num,"零亿","亿");// 替换字符串的字符
        num = numReplace(num,"零零","零");// 替换字符串的字符
        num = numReplace(num,"亿万","亿");// 替换字符串的字符
        //如果字符串以零结尾，则将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0,num.length() - 1);
        }
        return num;
    }
    /*
     * 转换小数点后面的数
     */
    private static String getDecimal(String num) {
        // 判断是否包含小数点
        if(num.indexOf(".")==-1) {
            return "";
        }
        num = num.substring(num.indexOf(".") + 1);
        //反转字符串
        num = new StringBuffer(num).reverse().toString();
        //创建一个StringBuffer对象
        StringBuffer temp = new StringBuffer();
        //加入单位
        for(int i = 0;i<num.length();i++) {
            temp.append(STR_UNIT2);
            temp.append(STR_NUMBER[num.charAt(i) - 48]);
        }
        num = temp.reverse().toString();//反转字符串
        num = numReplace(num, "零角", "零"); // 替换字符串的字符
        num = numReplace(num, "零分", "零"); // 替换字符串的字符
        num = numReplace(num, "零厘", "零"); // 替换字符串的字符
        num = numReplace(num, "零零", "零"); // 替换字符串的字符
        // 如果字符串以零结尾将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }
    /*
     * num  字符串
     * oldStr 被替换内容
     * newStr 新内容
     */
    private static String numReplace(String num, String oldStr, String newStr) {
        while(true) {
            //判断字符串中是否包含指定字符
            if(num.indexOf(oldStr)==-1) {
                break;
            }
            //替换字符串
            num = num.replace(oldStr, newStr);
        }
        //返回替换后的字符串
        return num;
    }

    //金额转换
    public static String convert(double d) {
        // 实例化DecimalFormat对象
        DecimalFormat df = new DecimalFormat("#");
        //格式化double数字
        String strNum = df.format(d);
        //判断是否包含小数点
        if(strNum.indexOf(".")!= -1) {
            String num = strNum.substring(0,strNum.indexOf("."));
            //整数部分大于12不能转换
            if(num.length()>12) {
                System.out.println("数字太大，不能完成转换!");
                return "";
            }
        }
        String point = "";            //小数点
        if(strNum.indexOf(".")!= -1) {
            point = "元";
        } else {
            point = "元整";
        }
        String result = getInteger(strNum)+point+getDecimal(strNum);   //转换结果
        if(result.startsWith("元")) {                                  //判断字符串是否以"元"结尾
            result = result.substring(1,result.length());             //截取字符串
        }
        return result;                                                 //返回新的字符串
    }

    public static String addDouble (String s,String s1) {
        try {
            if (StringUtils.isEmpty(s)) {
                s = "0";
            }
            if (StringUtils.isEmpty(s1)) {
                s1 = "0";
            }

            Double s2 = Double.parseDouble(s) + Double.parseDouble(s1);
            return s2.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";
    }
    public static Double addDouble (Double s,Double s1) {
        try {

            Double s2 = s + s1;
            return s2;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Double(0);
    }
}