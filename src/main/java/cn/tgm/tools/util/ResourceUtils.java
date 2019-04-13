package cn.tgm.tools.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 读取系统设定文件，提供接口获取系统设定参数。
 * 
 * @author tianguomin
 * @version 1.0
 */
public class ResourceUtils implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1924743293060062776L;

	private static ResourceBundle bundle;

	private static Map<String, ResourceBundle> bundles = new HashMap<>();

	static {
		try {
			bundle = ResourceBundle.getBundle("configuration");
		} catch (Exception e) {
			// nothing to do
		}
	}

	/** 构造函数 */
	private ResourceUtils() {

	}

	public static void load(String region, String baseName) {

		load(region, baseName, null);
	}

	public static void load(String region, String baseName, Locale locale) {

		if (bundles.get(region) != null)
			return;

		if (locale == null)
			locale = Locale.ENGLISH;
		ResourceBundle b = ResourceBundle.getBundle(baseName, locale);
		if (b != null)
			bundles.put(region, b);
	}

	public static String get(String region, String key, Object... arguments) {

		String result = null;

		try {
			ResourceBundle b = bundles.get(region);
			result = b.getString(key);
			result = MessageFormat.format(result, arguments);
		} catch (Exception e) {
			// nothing to do
		}

		return result;
	}

	public static String getString(String key) {

		String result = null;

		try {
			result = bundle.getString(key);
		} catch (Exception e) {
			// nothing to do
		}
		return result;
	}

	/**
	 * Return the integer value in configuration file by the key passed.<br>
	 * Or return default value when configuration value is null or incorrect.
	 * 
	 * @param key
	 *            key in configuration file
	 * @param defaultValue
	 *            default value
	 * @return
	 */
	public static int getInteger(String key, int defaultValue) {

		int i = 0;

		try {

			String s = bundle.getString(key);
			i = Integer.parseInt(s);

		} catch (Exception e) {
			i = defaultValue;
		}

		return i;
	}

	public static void main(String[] args) {

		System.out.println(ResourceUtils.getString("app_request_timeout"));
	}
}