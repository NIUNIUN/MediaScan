package com.qinglianyun.mediascan.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tang_xqing on 2020/12/9.
 */
public class StringUtils {

    /**
     * string转byte数组，utf-8编码
     * @param string
     * @return
     */
    public static byte[] string2Bytes(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte数组转string，utf-8编码
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
    ////////////////////////////////////////////////////////////////////////
    private static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = messagedigest.digest(input.getBytes());
        return bytesToHexString(md5Bytes);
    }

    public static String bytesToHexString(byte[] bytes) {
        return bytesToHexString(bytes, 0, bytes.length);
    }

    public static String bytesToHexString(byte[] bytes, int offset, int len) {
        StringBuffer stringbuffer = new StringBuffer(2 * len);
        int k = offset + len;
        for (int l = offset; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX_DIGITS[((bt & 0xF0) >> 4)];
        char c1 = HEX_DIGITS[(bt & 0xF)];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String null2Empty(String s) {
        return s == null ? "" : s;
    }

    public static String join(Object[] collections, String seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        Object[] arrayOfObject = collections;
        int j = collections.length;
        for (int i = 0; i < j; i++) {
            Object obj = arrayOfObject[i];
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }
            String text = obj == null ? "" : obj.toString();

            builder.append(text);
        }
        return builder.toString();
    }

    public static String join(Collection<? extends Object> collections, String seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Iterator<? extends Object> iter = collections.iterator(); iter.hasNext();) {
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }
            Object obj = iter.next();
            String text = obj == null ? "" : obj.toString();

            builder.append(text);
        }
        return builder.toString();
    }

    public static String join(Collection<? extends Object> collections, char seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Iterator<? extends Object> iter = collections.iterator(); iter.hasNext();) {
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }
            Object obj = iter.next();

            append(builder, obj, seperator);
        }
        return builder.toString();
    }

    public static void append(StringBuilder builder, Object obj, char seperator) {
        String text = Uri.encode(obj == null ? "" : obj.toString());
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == seperator) {
                builder.append('\\');
            } else if (text.charAt(i) == '\\') {
                builder.append('\\');
            }
            builder.append(text.charAt(i));
        }
    }

    public static String[] split(String value, char seperator) {
        if (value.length() == 0) {
            return new String[0];
        }
        value = value + seperator;

        List<String> ret = new ArrayList<String>();

        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '\\') {
                if (i + 1 < value.length()) {
                    i++;
                }
            }
            if (value.charAt(i) == seperator) {
                ret.add(Uri.decode(temp.toString()));
                temp.delete(0, temp.length());
            } else {
                temp.append(value.charAt(i));
            }
        }
        return (String[]) ret.toArray(new String[ret.size()]);
    }

    public static String trimString(String str) {
        if (str == null) {
            return null;
        }
        int start, end;
        for (start = 0; start < str.length(); start++) {
            if ((!Character.isWhitespace(str.charAt(start))) && (!Character.isSpaceChar(str.charAt(start)))) {
                break;
            }
        }
        if (start == str.length()) {
            return "";
        }
        for (end = str.length() - 1; end >= 0; end--) {
            if ((!Character.isWhitespace(str.charAt(end))) && (!Character.isSpaceChar(str.charAt(end)))) {
                break;
            }
        }
        return str.substring(start, end + 1);
    }

    public static boolean isDigital11(CharSequence phoneNumber) {
        return Pattern.matches("\\d{11}", phoneNumber);
    }

    public static boolean equals(String str1, String str2) {
        if ((str1 == null) || (str2 == null)) {
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean isEmpty(String data) {
        return data == null || data.equals("");
    }

    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) return false;
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][0-9]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

}
