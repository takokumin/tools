package cn.tgm.tools.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Objects;

/**
 * PinyinUtils
 *
 * @author tianguomin
 * @version 1.0
 */
public class PinyinUtils {

    public static final String UPPERCASE = "UPPERCASE";
    public static final String LOWERCASE = "LOWERCASE";

    public static String getPingYin(String chinese) throws BadHanyuPinyinOutputFormatCombination {

        return getPingYin(chinese, UPPERCASE);
    }

    /**
     * Return full pinyin of the given chinese.
     *
     * @param chinese  Chinese to be converted
     * @param caseType {@link #UPPERCASE} or {@link #LOWERCASE}
     * @return full pinyin
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPingYin(String chinese, String caseType) throws BadHanyuPinyinOutputFormatCombination {

        String result = null;

        if (Objects.nonNull(chinese)) {
            char[] charArray = chinese.toCharArray();
            StringBuilder pinyin = new StringBuilder();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            //设置大小写格式
            if (Objects.equals(UPPERCASE, caseType))
                defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            else
                defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            // 设置声调格式
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < charArray.length; i++) {
                // 匹配中文,非中文转换会转换成null
                if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                    if (pinyinArr != null) {
                        String string = pinyinArr[0];
                        pinyin.append(string);
                    }
                } else {
                    pinyin.append(charArray[i]);
                }
            }

            result = pinyin.toString();
        }

        return result;
    }

    public static String getPinYinHeadChar(String chinese) throws BadHanyuPinyinOutputFormatCombination {

        return getPinYinHeadChar(chinese, UPPERCASE);
    }

    /**
     * Return first pinyin char of the given chinese.
     *
     * @param chinese  Chinese to be converted
     * @param caseType {@link #UPPERCASE} or {@link #LOWERCASE}
     * @return First pinyin char
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPinYinHeadChar(String chinese, String caseType) throws BadHanyuPinyinOutputFormatCombination {

        String result = null;

        if (Objects.nonNull(chinese)) {
            char[] charArray = chinese.toCharArray();
            StringBuilder pinyin = new StringBuilder();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            // 设置大小写格式
            if (Objects.equals(UPPERCASE, caseType))
                defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            else
                defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            // 设置声调格式
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < charArray.length; i++) {
                //匹配中文,非中文转换会转换成null
                if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                    if (pinyinArr != null) {
                        pinyin.append(pinyinArr[0].charAt(0));
                    }
                } else {
                    pinyin.append(charArray[i]);
                }
            }

            result = pinyin.toString();
        }

        return result;
    }


    public static void main(String[] args) {

        try {
            System.out.println(PinyinUtils.getPingYin("好课堂堂练", PinyinUtils.LOWERCASE));
            System.out.println(PinyinUtils.getPinYinHeadChar("好课堂堂练"));
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
    }
}
