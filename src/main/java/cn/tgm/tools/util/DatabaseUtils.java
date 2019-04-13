package cn.tgm.tools.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 数据库工具类，主要功能如下：<br>
 * 1、生成建表SQL<br>
 * 2、生成Bean类
 *
 * @author tianguomin
 */
public class DatabaseUtils {

    /**
     * SQL文件名
     */
    public static final String SQL_FILE_NAME = "01.tables_create.sql";

    /**
     * 解析sheet的开始下标
     */
    private static final int STRAT_INDEX_OF_SHEET = 4;

    /**
     * 末尾忽略解析的sheet数
     */
    private static final int EXCLUDE_SHEET_COUNT = 1;

    /**
     * 表名行
     */
    private static final int ROW_TABLE_NAME = 0;

    /**
     * 表名列
     */
    private static final int COL_TABLE_NAME = 1;

    /**
     * 开始行
     */
    private static final int START_ROW = 5;

    /**
     * 主键列
     */
    private static final int COL_PK = 1;

    /**
     * 是否允许空值列
     */
    private static final int COL_NULLABLE = 2;

    /**
     * 名称列
     */
    private static final int COL_NAME = 6;

    /**
     * 注释列
     */
    private static final int COL_COMMENT = 7;

    /**
     * 类型列
     */
    private static final int COL_TYPE = 8;

    /**
     * 字符位数列
     */
    private static final int COL_STR_LEN = 9;

    /**
     * 整数位数列
     */
    private static final int COL_INT_LEN = 10;

    /**
     * 小数位数列
     */
    private static final int COL_DECIMAL_LEN = 11;

    /**
     * 默认值列
     */
    private static final int COL_DEFAULT_VAL = 12;

    /**
     * 字符类型
     */
    private static final List<String> TYPES_STR = Arrays.asList("CHAR", "VARCHAR");

    /**
     * 整数类型
     */
    private static final List<String> TYPES_INT = Arrays.asList("TINYINT", "SMALLINT", "MEDIUMINT", "INT", "BIGINT");

    /**
     * 小数类型
     */
    private static final List<String> TYPES_DECIMAL = Arrays.asList("FLOAT", "DOUBLE", "DECIMAL");

    /**
     * 日期时间类型
     */
    private static final List<String> TYPES_DATETIME = Arrays.asList("DATE", "TIME", "YEAR", "DATETIME", "TIMESTAMP");

    /**
     * Oracle数据类型映射(默认为MySQL)
     */
    public static Map<String, String> ORACLE_TYPE_MAPPED = null;

    static {
        ORACLE_TYPE_MAPPED = new HashMap<>();
        // TYPE_MAPPED.put("CHAR", "CHAR");
        // ......
    }

    /**
     * Bean类包名
     */
    private static final String PACKAGE_NAME = "cn.tj.bdt.bean";

    /**
     * Bean类忽略的字段
     */
    private static final List<String> IGNORE_FIELDS = Arrays.asList("USER_ID", "COMMENT", "UPDATE_BY", "UPDATE_ON",
            "CREATE_BY", "CREATE_ON");

    /**
     * Java数据类型映射
     */
    public static Map<String, String> JAVA_TYPE_MAPPED = null;

    static {
        JAVA_TYPE_MAPPED = new HashMap<>();
        JAVA_TYPE_MAPPED.put("TINYINT", "int");
        JAVA_TYPE_MAPPED.put("SMALLINT", "int");
        JAVA_TYPE_MAPPED.put("MEDIUMINT", "int");
        JAVA_TYPE_MAPPED.put("INT", "int");
        JAVA_TYPE_MAPPED.put("BIGINT", "long");
        JAVA_TYPE_MAPPED.put("FLOAT", "float");
        JAVA_TYPE_MAPPED.put("DOUBLE", "double");
        JAVA_TYPE_MAPPED.put("DECIMAL", "double");
        JAVA_TYPE_MAPPED.put("DATE", "Date");
        JAVA_TYPE_MAPPED.put("TIME", "Date");
        JAVA_TYPE_MAPPED.put("YEAR", "Date");
        JAVA_TYPE_MAPPED.put("DATETIME", "Date");
        JAVA_TYPE_MAPPED.put("TIMESTAMP", "Date");
        JAVA_TYPE_MAPPED.put("CHAR", "String");
        JAVA_TYPE_MAPPED.put("VARCHAR", "String");
        JAVA_TYPE_MAPPED.put("TINYBLOB", "String");
        JAVA_TYPE_MAPPED.put("TINYTEXT", "String");
        JAVA_TYPE_MAPPED.put("BLOB", "String");
        JAVA_TYPE_MAPPED.put("TEXT", "String");
        JAVA_TYPE_MAPPED.put("MEDIUMBLOB", "String");
        JAVA_TYPE_MAPPED.put("MEDIUMTEXT", "String");
        JAVA_TYPE_MAPPED.put("LOGNGBLOB", "String");
        JAVA_TYPE_MAPPED.put("LONGTEXT", "String");
        JAVA_TYPE_MAPPED.put("VARBINARY", "String");
        JAVA_TYPE_MAPPED.put("BINARY", "String");
    }

    /**
     * 依据数据库定义文件，生成建表SQL。
     *
     * @param absFileName 数据库表定义Excel文件
     * @param sqlFilePath 生成的SQL文件路径
     */
    public static void generateCreateTablesSQL(String absFileName, String sqlFilePath) {

        generateCreateTablesSQL(absFileName, sqlFilePath, false);
    }

    /**
     * 依据数据库定义文件，生成建表SQL。
     *
     * @param absFileName 数据库表定义Excel文件
     * @param sqlFilePath 生成的SQL文件路径
     * @param needComment 生成字段注释
     */
    public static void generateCreateTablesSQL(String absFileName, String sqlFilePath, boolean needComment) {

        InputStream is = null;
        XSSFWorkbook wb = null;
        FileWriter fw = null;

        try {
            long start = Clock.systemUTC().millis();
            is = new FileInputStream(absFileName);
            wb = new XSSFWorkbook(is);
            XSSFSheet sheet = null;
            XSSFRow row = null;
            StringBuffer header = new StringBuffer();
            StringBuffer buff = new StringBuffer();
            String tableName = null;
            String tableComment = null;
            String colType = null;
            String colName = null;
            String defVal = null;
            String comment = null;
            List<String> dropList = new ArrayList<>();
            List<String> pkList = new ArrayList<>();

            header.append("-- --------------------------------------------------");
            header.append("\n-- Create tables SQL script.");
            header.append("\n-- ");
            header.append("\n-- Build by: tianguomin");
            header.append("\n-- Build on: ").append(LocalDateTime.now());
            header.append("\n-- --------------------------------------------------\n");

            buff.append("\n-- --------------------------------------------------");
            for (int i = STRAT_INDEX_OF_SHEET; i < wb.getNumberOfSheets() - EXCLUDE_SHEET_COUNT; i++) {

                pkList.clear();

                sheet = wb.getSheetAt(i);
                tableName = sheet.getRow(ROW_TABLE_NAME).getCell(COL_TABLE_NAME).getStringCellValue();
                tableComment = sheet.getSheetName();

                dropList.add("DROP TABLE IF EXISTS " + tableName + ";");

                buff.append("\nCREATE TABLE ").append(tableName).append(" (\n");

                for (int rownum = START_ROW; rownum < sheet.getLastRowNum(); rownum++) {

                    row = sheet.getRow(rownum);
                    colName = cell2str(row.getCell(COL_NAME));
                    if (row.getCell(COL_NAME) != null && Utility.notEmpty(colName)) {

                        buff.append("  ");
                        if (rownum > START_ROW)
                            buff.append(",");

                        // 列名
                        buff.append(colName).append(" ");

                        // 列类型及长度
                        colType = cell2str(row.getCell(COL_TYPE));
                        buff.append(colType);
                        if (TYPES_STR.contains(colType)) {
                            buff.append("(").append(cell2str(row.getCell(COL_STR_LEN))).append(")");
                        } else if (TYPES_INT.contains(colType)) {
                            buff.append("(").append(cell2str(row.getCell(COL_INT_LEN))).append(")");
                        } else if (TYPES_DECIMAL.contains(colType)) {
                            buff.append("(").append(cell2str(row.getCell(COL_INT_LEN))).append(",");
                            buff.append(cell2str(row.getCell(COL_DECIMAL_LEN))).append(")");
                        } else if (TYPES_DATETIME.contains(colType)) {
                            String s = cell2str(row.getCell(COL_STR_LEN));
                            if (s != null && s.length() > 0)
                                buff.append("(").append(s).append(")");
                        }

                        // 是否允许为NULL
                        if ("Y".equals(cell2str(row.getCell(COL_NULLABLE)))) {
                            buff.append(" NOT NULL");
                        } else {
                            buff.append(" NULL");
                        }

                        // 默认值
                        defVal = cell2str(row.getCell(COL_DEFAULT_VAL));
                        if (Utility.notEmpty(defVal)) {
                            buff.append(" DEFAULT ");
                            if (TYPES_INT.contains(colType)) {
                                buff.append(cell2int(row.getCell(COL_DEFAULT_VAL)));
                            } else if (TYPES_DECIMAL.contains(colType)) {
                                buff.append(cell2numeric(row.getCell(COL_DEFAULT_VAL)));
                            } else if (TYPES_DATETIME.contains(colType)) {
                                buff.append("CURRENT_TIMESTAMP");
                                String s = cell2str(row.getCell(COL_STR_LEN));
                                if (s != null && s.length() > 0)
                                    buff.append("(").append(s).append(")");
                                if (Objects.equals("UPDATE_ON", colName)) {
                                    buff.append(" ON UPDATE CURRENT_TIMESTAMP");
                                }
                            } else {
                                buff.append("'").append(defVal).append("'");
                            }
                        }

                        if (needComment) {
                            comment = cell2str(row.getCell(COL_COMMENT));
                            if (Utility.notEmpty(comment)) {
                                buff.append(" COMMENT '").append(comment).append("'");
                            }
                        }

                        // 记录PK列
                        if ("○".equals(cell2str(row.getCell(COL_PK)))) {
                            pkList.add(colName);
                        }

                        buff.append("\n");
                    }
                }

                // 设置PK
                if (pkList.size() > 0) {
                    buff.append("  ,PRIMARY KEY (");
                    buff.append(pkList.toString().replace("[", "").replace("]", ""));
                    buff.append(")");
                }

                buff.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                if (needComment) {
                    buff.append(" COMMENT='").append(tableComment).append("'");
                }
                buff.append(";");
                buff.append("\n-- --------------------------------------------------");

            }

            // output SQL
            sqlFilePath = Utility.isEmpty(sqlFilePath) ? "" : sqlFilePath + File.separator;
            File folder = new File(sqlFilePath);
            if (!folder.exists())
                folder.mkdirs();
            fw = new FileWriter(folder.getAbsolutePath() + File.separator + SQL_FILE_NAME);
            fw.write(header.toString());
            for (String s : dropList) {
                fw.write(s);
                fw.write("\n");
            }
            fw.write(buff.toString());
            fw.flush();

            long end = Clock.systemUTC().millis();
            System.out.println("SQL build successful.");
            System.out.println("Total time: " + (end - start) + " ms.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();

                if (wb != null)
                    wb.close();

                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String cell2str(XSSFCell cell) {

        if (cell == null)
            return "";

        if (cell.getCellTypeEnum() == CellType.BLANK) {
            return "";
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(new Double(cell.getNumericCellValue()).intValue());
        } else {
            System.out.println(cell.toString() + " can't convert to a String value.");
            return "";
        }
    }

    private static int cell2int(XSSFCell cell) {

        if (cell == null)
            return 0;

        if (cell.getCellTypeEnum() == CellType.BLANK) {
            return 0;
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            return Integer.parseInt(cell.getStringCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return new Double(cell.getNumericCellValue()).intValue();
        } else {
            System.out.println(cell.toString() + " can't convert to a int value.");
            return 0;
        }
    }

    private static double cell2numeric(XSSFCell cell) {

        if (cell == null)
            return 0;

        if (cell.getCellTypeEnum() == CellType.BLANK) {
            return 0;
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            return Double.parseDouble(cell.getStringCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            System.out.println(cell.toString() + " can't convert to a numeric value.");
            return 0;
        }
    }

    /**
     * 依据数据库定义文件，生成Bean类。
     *
     * @param absFileName 数据库表定义Excel文件
     * @param packageName 包名
     */
    public static void generateBeanClass(String absFileName, String packageName) {

        InputStream is = null;
        XSSFWorkbook wb = null;

        try {
            long start = Clock.systemUTC().millis();
            is = new FileInputStream(absFileName);
            wb = new XSSFWorkbook(is);
            XSSFSheet sheet = null;
            XSSFRow row = null;
            String tableName = null;
            String colType = null;
            String colName = null;

            // build base bean class
            buildBaseBeanClass(packageName);

            List<String> fields = new ArrayList<>();
            List<String> types = new ArrayList<>();

            for (int i = STRAT_INDEX_OF_SHEET; i < wb.getNumberOfSheets() - EXCLUDE_SHEET_COUNT; i++) {

                fields.clear();
                types.clear();

                sheet = wb.getSheetAt(i);
                tableName = sheet.getRow(ROW_TABLE_NAME).getCell(COL_TABLE_NAME).getStringCellValue();

                for (int rownum = START_ROW; rownum < sheet.getLastRowNum(); rownum++) {

                    row = sheet.getRow(rownum);
                    colName = cell2str(row.getCell(COL_NAME));
                    if (row.getCell(COL_NAME) != null && Utility.notEmpty(colName)) {

                        // 列类型
                        colType = cell2str(row.getCell(COL_TYPE));

                        // 过滤Bean类忽略的字段(共通字段会定义在Bean基类里)
                        if (!IGNORE_FIELDS.contains(colName)) {
                            fields.add(colName);
                            types.add(JAVA_TYPE_MAPPED.get(colType));
                        }

                    }
                }

                buildBeanClass(packageName, fields, types, tableName);
            }

            long end = Clock.systemUTC().millis();
            System.out.println("Bean class build successful.");
            System.out.println("Total time: " + (end - start) + " ms.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();

                if (wb != null)
                    wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void buildBeanClass(String packageName, List<String> fields, List<String> types, String tableName) {

        String className = buildClassName(tableName);
        String fileName = className + ".java";
        FileWriter fw = null;

        try {
            packageName = Utility.isEmpty(packageName) ? PACKAGE_NAME : packageName;
            File folder = new File("src" + File.separator + packageName.replace(".", File.separator));
            if (!folder.exists())
                folder.mkdirs();
            fw = new FileWriter(folder.getAbsolutePath() + File.separator + fileName);
            fw.write("package " + packageName + ";\n");
            if (types.contains("Date")) {
                fw.write("\nimport java.util.Date;\n");
            }
            fw.write("\n/**");
            fw.write("\n * Table [" + tableName + "]'s bean class.");
            fw.write("\n * ");
            fw.write("\n * @author tianguomin");
            fw.write("\n * @version 1.0");
            fw.write("\n */");
            fw.write("\npublic class " + className + " extends BaseBean {\n");
            fw.write("\n\tprivate static final long serialVersionUID = -1;\n");

            StringBuffer buff = new StringBuffer();

            // build fields
            IntStream.range(0, fields.size()).forEach(i -> {
                buff.append("\n\tprivate " + types.get(i) + " " + fields.get(i).toLowerCase() + ";\n");
            });

            // build methods
            IntStream.range(0, fields.size()).forEach(i -> {
                final String field = fields.get(i);
                final String type = types.get(i);
                buff.append("\n\tpublic " + type + " " + buildMethod(field, "get") + "() {\n");
                buff.append("\n\t\treturn this." + field.toLowerCase() + ";\n\t}\n");
                buff.append(
                        "\n\tpublic void " + buildMethod(field, "set") + "(" + type + " " + field.toLowerCase() + ") {\n");
                buff.append("\n\t\tthis." + field.toLowerCase() + " = " + field.toLowerCase() + ";\n\t}\n");
            });

            fw.write(buff.toString());

            fw.write("}");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void buildBaseBeanClass(String packageName) {

        String className = "BaseBean";
        String fileName = "BaseBean.java";
        final List<String> fields = Arrays.asList("COMMENT", "UPDATE_BY", "UPDATE_ON", "CREATE_BY", "CREATE_ON");
        final List<String> types = Arrays.asList("String", "String", "Date", "String", "Date");
        FileWriter fw = null;

        try {
            packageName = Utility.isEmpty(packageName) ? PACKAGE_NAME : packageName;
            File folder = new File("src" + File.separator + packageName.replace(".", File.separator));
            if (!folder.exists())
                folder.mkdirs();
            fw = new FileWriter(folder.getAbsolutePath() + File.separator + fileName);
            fw.write("package " + packageName + ";\n");
            fw.write("\nimport java.io.Serializable;\n");
            fw.write("\nimport java.util.Date;\n");
            fw.write("\n/**");
            fw.write("\n * Base bean class.");
            fw.write("\n * ");
            fw.write("\n * @author tianguomin");
            fw.write("\n * @version 1.0");
            fw.write("\n */");
            fw.write("\npublic class " + className + " implements Serializable {\n");
            fw.write("\n\tprivate static final long serialVersionUID = -1;\n");

            StringBuffer buff = new StringBuffer();

            // build fields
            IntStream.range(0, fields.size()).forEach(i -> {
                buff.append("\n\tprivate " + types.get(i) + " " + fields.get(i).toLowerCase() + ";\n");
            });

            // build methods
            IntStream.range(0, fields.size()).forEach(i -> {
                final String field = fields.get(i);
                final String type = types.get(i);
                buff.append("\n\tpublic " + type + " " + buildMethod(field, "get") + "() {\n");
                buff.append("\n\t\treturn this." + field.toLowerCase() + ";\n\t}\n");
                buff.append(
                        "\n\tpublic void " + buildMethod(field, "set") + "(" + type + " " + field.toLowerCase() + ") {\n");
                buff.append("\n\t\tthis." + field.toLowerCase() + " = " + field.toLowerCase() + ";\n\t}\n");
            });

            fw.write(buff.toString());

            fw.write("}");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String buildClassName(String tableName) {

        StringBuffer buff = new StringBuffer();
        String[] tmp = tableName.toLowerCase().split("_");
        for (String s : tmp) {
            char[] cs = s.toCharArray();
            if (cs[0] >= 'a' || cs[0] <= 'z') {
                cs[0] -= 32;
            }
            buff.append(new String(cs));
        }

        return buff.toString() + "Bean";
    }

    private static String buildMethod(String fieldName, String getOrSet) {

        StringBuffer buff = new StringBuffer();
        char[] cs = fieldName.toLowerCase().toCharArray();
        if (cs[0] >= 'a' || cs[0] <= 'z') {
            cs[0] -= 32;
        }
        buff.append(getOrSet).append(new String(cs));

        return buff.toString();
    }

    public static void main(String[] args) {

        DatabaseUtils.generateCreateTablesSQL("D:\\work\\HappyAPPS\\数据库表结构设计书_HappyPay.xlsm", "db", true);
        // DatabaseUtils.generateBeanClass("D:\\work\\HappyAPPS\\数据库表结构设计书_HappyPay.xlsm",
        // "cn.tgm.bean");
    }

}
