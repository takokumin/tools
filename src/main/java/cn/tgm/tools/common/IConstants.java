package cn.tgm.tools.common;

/**
 * 系统常量类。
 * 
 * @author tianguomin
 * @version 1.0
 */
public interface IConstants {

	/** 系统名 */
	public static final String SYSTEM_NAME = "";

	/** 日志输出对象名 */
	public static final String LOGGER_NAME = "TGM";

	// ///////////////////////////////////////////////
	// ///////////系统共用Map的key定义/////////////////
	// ///////////////////////////////////////////////
	/** Session信息 */
	public static final String _SESSION = "_SESSION";

	/** 系统权限 */
	public static final String _SYSTEM_PERMISSIONS = "_SYSTEM_PERMISSIONS";

	/** 画面ID */
	public static final String _PAGE_ID = "_PAGE_ID";

	/** 画面请求地址 */
	public static final String _PAGE_URL = "_PAGE_URL";

	// ///////////////////////////////////////////////
	// ///////////请求结果相关定义/////////////////
	// ///////////////////////////////////////////////
	/** 结果状态KEY */
	public static final String RET_STATUS = "status";

	/** 结果信息KEY */
	public static final String RET_MESSAGE = "message";

	/** 结果数据KEY */
	public static final String RET_DATA = "data";

	/** 结果数量KEY */
	public static final String RET_COUNT = "count";

	/** 结果状态：成功 */
	public static final String RET_STATUS_SUCCESS = "0";

	/** 结果状态：失败 */
	public static final String RET_STATUS_ERROR = "-1";

	// ///////////////////////////////////////////////
	// ///////////常用变量定义/////////////////////
	// ///////////////////////////////////////////////
	/** 否 */
	public static final String NO = "0";

	/** 是 */
	public static final String YES = "1";

	// ///////////////////////////////////////////////
	// ///////////日期时间格式定义/////////////////////
	// ///////////////////////////////////////////////
	/** 日期格式 yyyy-MM-dd HH:mm:ss */
	public static final String DT_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	/** 日期格式 yyyy/MM/dd HH:mm:ss */
	public static final String DT_FORMAT_DEFAULT_SLASH = "yyyy/MM/dd HH:mm:ss";

	/** 日期格式 yyyy-MM-dd HH:mm:ss.SSS */
	public static final String DT_FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";

	/** 日期格式 yyyy/MM/dd HH:mm:ss.SSS */
	public static final String DT_FORMAT_MILLISECOND_SLASH = "yyyy/MM/dd HH:mm:ss.SSS";

	/** 日期格式 yyyy-MM-dd */
	public static final String DT_FORMAT_YYYYDDMM = "yyyy-MM-dd";

	/** 日期格式 yyyy/MM/dd */
	public static final String DT_FORMAT_YYYYDDMM_SLASH = "yyyy/MM/dd";

	// ///////////////////////////////////////////////
	// ///////////常量定义/////////////////////
	// ///////////////////////////////////////////////
	/** 0 */
	public static final String ZERO = "0";

	/** 1 */
	public static final String ONE = "1";

	/** 2 */
	public static final String TWO = "2";

	/** 3 */
	public static final String THREE = "3";

	/** 4 */
	public static final String FOUR = "4";

	/** 5 */
	public static final String FIVE = "5";

	/** 6 */
	public static final String SIX = "6";

	/** 7 */
	public static final String SEVEN = "7";

	/** 8 */
	public static final String EIGHT = "8";

	/** 9 */
	public static final String NINE = "9";

	/** blank */
	public static final String BLANK = "";

	/** true */
	public static final String TRUE = "true";

	/** false */
	public static final String FALSE = "false";

	// ///////////////////////////////////////////////
	// ///////////模糊类型定义/////////////////////
	// ///////////////////////////////////////////////
	/** 模糊类型 前方一致 */
	public static final int FUZZY_TYPE_FRONT = 1;

	/** 模糊类型 后方一致 */
	public static final int FUZZY_TYPE_REAR = 2;

	/** 模糊类型 部分一致 */
	public static final int FUZZY_TYPE_PART = 3;

	// ///////////////////////////////////////////////
	// ///////////正则表达式定义/////////////////////
	// ///////////////////////////////////////////////

	/** 日期格式(yyy-MM-dd)正则表达式 */
	public static final String REGEX_DATE_YYYY_MM_DD = "^(?:(?!0000)[0-9]{4}-?(?:(?:0[1-9]|1[0-2])"
			+ "-?(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-?(?:29|30)|(?:0[13578]|1[02])"
			+ "-?31)|(?:[0-9]{2}-?(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)02-?29)$";

	/** Email正则表达式 */
	public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@"
			+ "((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" + "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,3})(\\]?)$";

	/** 数值正则表达式 */
	public static final String REGEX_NUMBER = "^-?\\d+$";

	/** 手机号码正则表达式 */
	public static final String REGEX_PHONE_NO = "^[\\d\\-\\*\\#]+$";

	/** 经纬度正则表达式 */
	public static final String REGEX_LAT_LNG = "(||-)\\d{1,3}.\\d{6,17}";

	/** 字母、数字、下划线表达式 */
	public static final String REGEX_CHARACTER_NUMBER = "\\d+.\\d+|\\w+";

}