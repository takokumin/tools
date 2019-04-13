package cn.tgm.tools.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import cn.tgm.tools.common.IConstants;

/**
 * 工具类。 <br>
 * 提供常用的工具函数。
 * 
 * @author tianguomin
 * @version 1.0
 */
public class Utility {

	/**
	 * 检查指定字符串是否为空或长度为0。
	 * 
	 * @param str
	 *            需要检查的字符串
	 * @return 字符串为空或长度为0，返回true，否则返回false。
	 */
	public static boolean isEmpty(String str) {

		if (str == null)
			return true;

		return str.length() == 0 ? true : false;
	}

	/**
	 * 检查指定字符串是否不为空并且长度大于0。
	 * 
	 * @param str
	 *            需要检查的字符串
	 * @return 字符串为空或长度为0，返回false，否则返回true。
	 */
	public static boolean notEmpty(String str) {

		if (str == null)
			return false;

		return str.length() == 0 ? false : true;
	}

	/**
	 * 检查指定的Object是否为空，如果为空，返回空字符串，否则原样返回。
	 * 
	 * @param str
	 *            指定的字符串
	 * @return 转换后的字符串
	 */
	public static String null2blank(Object obj) {

		if (obj == null)
			return "";

		return String.valueOf(obj);
	}

	/**
	 * 检查指定的字符串是否为空，如果为空，返回空字符串，否则原样返回。
	 * 
	 * @param str
	 *            指定的字符串
	 * @return 转换后的字符串
	 */
	public static String null2blank(String str) {

		if (str == null)
			str = "";

		return str;
	}

	/**
	 * Convert String to long
	 * 
	 * @param value
	 * @param def
	 *            default value
	 * @return
	 */
	public static long toLong(String value, long def) {

		if (isEmpty(value)) {
			return def;
		}

		try {
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Convert String to int
	 * 
	 * @param value
	 * @param def
	 *            default value
	 * @return
	 */
	public static int toInt(String value, int def) {

		if (isEmpty(value)) {
			return def;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * 按指定位数格式化字符串,位数不足时前面补0
	 * 
	 * @param str
	 *            原始字符串
	 * @param digit
	 *            总位数
	 * @return 格式化的字符串
	 */
	public static String formatString(String str, int digit) {

		if (isEmpty(str))
			return "";

		if (str.length() >= digit)
			return str;

		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < digit - str.length(); i++) {
			buff.append("0");
		}
		return buff.append(str).toString();
	}

	/**
	 * 使用默认格式(yyyy-MM-dd HH:mm:ss)返回系统当前日期。
	 * 
	 * @return 系统日期
	 */
	public static String getSysdate() {

		return getSysdate(IConstants.DT_FORMAT_DEFAULT);
	}

	/**
	 * 使用指定格式返回系统当前日期。
	 * 
	 * @param format
	 *            日期格式
	 * @return 系统日期
	 */
	public static String getSysdate(String format) {

		if (isEmpty(format))
			format = IConstants.DT_FORMAT_DEFAULT;

		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * Parses text from a string to produce a <code>Date</code>. Default format
	 * is "dd/MM/yyyy".
	 * 
	 * @param data
	 *            A <code>String</code>, part of which should be parsed.
	 * @return A <code>Date</code> parsed from the string. In case of error,
	 *         returns null.
	 */
	public static Date parseDate(String data) {

		return parseDate(IConstants.DT_FORMAT_DEFAULT, data);
	}

	/**
	 * Parses text from a string to produce a <code>Date</code> with the given
	 * format.
	 * 
	 * @param data
	 *            A <code>String</code>, part of which should be parsed.
	 * @param format
	 *            date format
	 * @return A <code>Date</code> parsed from the string. In case of error,
	 *         returns null.
	 */
	public static Date parseDate(String format, String data) {

		try {
			return new SimpleDateFormat(format).parse(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期比较（日期格式yyyy-MM-dd）。
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * @return
	 * @throws WZException
	 */
	public static boolean isDateBefore(String date, String anotherDate) throws ParseException {

		return isDateBefore(date, anotherDate, IConstants.DT_FORMAT_YYYYDDMM);
	}

	/**
	 * 日期比较
	 * 
	 * @param date
	 *            开始日期
	 * @param anotherDate
	 *            结束日期
	 * @return
	 * @throws WZException
	 */
	public static boolean isDateBefore(String date, String anotherDate, String format) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date d1 = sdf.parse(date);
		Date d2 = sdf.parse(anotherDate);

		return d1.before(d2);
	}

	/**
	 * 半角字符检查。
	 * 
	 * @param str
	 *            检查的字符串
	 * @return 检查结果
	 */
	public static final boolean checkSingleByte(String str) {

		if (isEmpty(str))
			return true;

		return str.getBytes().length == str.length();
	}

	/**
	 * 转义SQL参数中的特殊字符。
	 * 
	 * @param param
	 *            SQL参数
	 * @return 转义后的SQL参数
	 */
	public static String escapeSQLParam(String param) {

		if (param != null)
			param = param.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("_", "\\\\_").replaceAll("%", "\\\\%");
		else
			param = "";

		return param;
	}

	/**
	 * 对SQL参数进行模糊设定。
	 * 
	 * @param param
	 *            SQL参数
	 * @param fuzzyType
	 *            模糊类型
	 * @return 模糊设定后的SQL参数
	 */
	public static String fuzzyParam(String param, int fuzzyType) {

		if (notEmpty(param)) {
			param = escapeSQLParam(param.trim());

			if (fuzzyType == IConstants.FUZZY_TYPE_FRONT)
				param = "%" + param;
			else if (fuzzyType == IConstants.FUZZY_TYPE_REAR)
				param = param + "%";
			else
				param = "%" + param + "%";
		}

		return param;
	}

	/**
	 * 小数
	 * 
	 * @param str
	 *            检查的字符串
	 * @param integerDigit
	 *            整数位
	 * @param decimalDigit
	 *            小数位
	 * @return
	 */
	public static final boolean checkNumberDigit(String str, int integerDigit, int decimalDigit) {

		if (isEmpty(str))
			return true;

		String[] num = str.split("\\.");
		if (!isNumber(num[0]) || num[0].length() > integerDigit) {
			return false;
		}

		if (num.length == 2) {
			if (!isNumber(num[1]) || num[1].length() > decimalDigit) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 数值检查
	 * 
	 * @param num
	 *            检查的字符串
	 * @return
	 */
	public static boolean isNumber(String num) {

		return Pattern.matches(IConstants.REGEX_NUMBER, num);
	}

	/**
	 * 日期的检查(YYYY-MM-DD)
	 * 
	 * @param str
	 *            检查的字符串
	 * @return
	 */
	public static final boolean isDate(String str) {

		if (isEmpty(str))
			return false;

		return Pattern.matches(IConstants.REGEX_DATE_YYYY_MM_DD, str);
	}

	/**
	 * Email的检查
	 * 
	 * @param str
	 *            检查的字符串
	 * @return
	 */
	public static final boolean isEmail(String str) {

		if (isEmpty(str))
			return false;

		return Pattern.matches(IConstants.REGEX_EMAIL, str);
	}

	/**
	 * 手机号码的检查
	 * 
	 * @param str
	 *            检查的字符串
	 * @return
	 */
	public static final boolean isPhoneNo(String str) {

		if (isEmpty(str))
			return false;

		// 電話番号
		return Pattern.matches(IConstants.REGEX_PHONE_NO, str);
	}

	/**
	 * 经纬度的检查
	 * 
	 * @param str
	 *            检查的字符串
	 * @return
	 */
	public static final boolean isLatLng(String str) {

		if (isEmpty(str))
			return false;

		// 经纬度的检查
		return Pattern.matches(IConstants.REGEX_LAT_LNG, str);
	}

	/**
	 * 带分隔符的UUID
	 * 
	 * @return
	 */
	public static final String UUID() {

		return UUID.randomUUID().toString();
	}

	/**
	 * 不带分隔符的32位UUID
	 * 
	 * @return
	 */
	public static final String UUID32() {

		return UUID().replaceAll("-", "");
	}

	@SuppressWarnings("unchecked")
	public static String mapToJson(Map<String, Object> data) {

		StringBuffer buff = new StringBuffer();
		if (data != null) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if (buff.length() == 0) {
					buff.append("{\"");
				} else {
					buff.append(",\"");
				}
				buff.append(entry.getKey());
				buff.append("\":");
				if (entry.getValue() instanceof List) {
					buff.append("[");
					int i = 0;
					for (Object obj : (List<Object>) entry.getValue()) {
						if (obj instanceof Map) {
							if (i != 0)
								buff.append(",");
							buff.append(mapToJson((Map<String, Object>) obj));
							i++;
						}
					}
					buff.append("]");
				} else if (entry.getValue() instanceof Map) {
					buff.append(mapToJson((Map<String, Object>) entry.getValue()));
				} else {
					buff.append("\"");
					buff.append(entry.getValue().toString());
					buff.append("\"");
				}
			}
			buff.append("}");
		}

		return buff.toString();
	}

	public static String getMD5(String s) {

		try {
			if (s != null) {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				byte[] digest = md5.digest(s.getBytes("utf-8"));
				return byteArr2hexStr(digest);
			}
		} catch (NoSuchAlgorithmException e) {
			// do nothing
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}

		return null;
	}

	public static String getSHA256(String s) {

		try {
			if (s != null) {
				MessageDigest md5 = MessageDigest.getInstance("SHA-256");
				byte[] digest = md5.digest(s.getBytes("utf-8"));
				return byteArr2hexStr(digest);
			}
		} catch (NoSuchAlgorithmException e) {
			// do nothing
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}

		return null;
	}

	/**
	 * convert byte array to hex string.
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArr2hexStr(byte[] b) {

		StringBuffer buff = new StringBuffer();
		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				buff.append("0");
			}
			buff.append(stmp);
		}

		return buff.toString();
	}

	/**
	 * convert hex string to byte array.
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] hexStr2byteArr(byte[] b) {

		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("Array's length is not an even number.");
		} else {
			byte[] b2 = new byte[b.length / 2];
			for (int n = 0; n < b.length; n += 2) {

				String item = new String(b, n, 2);
				b2[n / 2] = (byte) Integer.parseInt(item, 16);
			}

			return b2;
		}
	}

	public static boolean checkSignature(String token, String timestamp, String sign) {

		boolean rs = false;

		if (notEmpty(token) && notEmpty(timestamp) && notEmpty(sign)) {

			long now = Clock.systemUTC().millis();
			String timeout = ResourceUtils.getString("app_request_timeout");
			if (now - Long.parseLong(timestamp) > Integer.parseInt(timeout) * 1000) {
				// expired signature
				return false;
			}

			String[] arr = { token, timestamp };
			Arrays.sort(arr);
			String strMD5 = getMD5(arr[0] + arr[1]);
			rs = Objects.equals(sign, strMD5);
		}

		return rs;
	}
}