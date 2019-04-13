package cn.tgm.tools.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FileUtils
 *
 * @author tianguomin
 * @version 1.0
 */
public class FileUtils {

    private static final String PACKAGE_NAME = "cn.tj.bdt.common";

    /**
     * 依据国际化文件创建MessageKey定义类文件
     *
     * @param inFile
     * @param className
     * @param packageName
     */
    public static void generateMessageKeyClass(String inFile, String className, String packageName) {

        FileWriter fw = null;

        try {

            String fileName = className + ".java";
            packageName = Utility.isEmpty(packageName) ? PACKAGE_NAME : packageName;
            File folder = new File("src" + File.separator + packageName.replace(".", File.separator));
            if (!folder.exists())
                folder.mkdirs();
            fw = new FileWriter(folder.getAbsolutePath() + File.separator + fileName);

            fw.write("package " + packageName + ";\n");
            fw.write("\n/**");
            fw.write("\n * Message key definition class.");
            fw.write("\n * ");
            fw.write("\n * @author tianguomin");
            fw.write("\n * @version 1.0");
            fw.write("\n */");
            fw.write("\npublic class " + className + " {\n");

            ResourceBundle bundle = ResourceBundle.getBundle(inFile);
            Enumeration<String> keys = bundle.getKeys();
            String key;

            while (keys.hasMoreElements()) {

                key = keys.nextElement();
                fw.write("\n\tpublic static final String ");
                fw.write(key.replaceAll("\\.", "\\_").toUpperCase());
                fw.write(" = \"");
                fw.write(key);
                fw.write("\";\n");
            }

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


    public static void generateModuleFiles(String configFile) {

        final String filterText = "<div class=\"mb-2 mr-sm-2 mb-sm-0 position-relative form-group\">\n"
                + "                        <label for=\"#inputId#\" class=\"mr-sm-2\" data-i18n-txt=\"#i18nName#\"></label>\n"
                + "                        <input name=\"#inputName#\" id=\"#inputId#\" type=\"text\" class=\"form-control\">\n"
                + "                    </div>";
        final String filterSelect = "<div class=\"mb-2 mr-sm-2 mb-sm-0 position-relative form-group\">\n"
                + "                        <label for=\"#inputId#\" class=\"mr-sm-2\" data-i18n-txt=\"#i18nName#\"></label>\n"
                + "                        <select name=\"#inputName#\" id=\"#inputId#\" class=\"form-control\" data-dict-id=\"#dictId#\"\n"
                + "                            style=\"width:120px;\">\n"
                + "                        </select>\n" + "                        </div>";
        final String filterDate = "<div class=\"mb-2 mr-sm-2 mb-sm-0 position-relative form-group\">\n" +
                "                            <label for=\"#inputId#\" class=\"mr-sm-2\" data-i18n-txt=\"#i18nName#\"></label>\n" +
                "                            <div class=\"input-group\">\n" +
                "                                <div class=\"input-group-prepend\">\n" +
                "                                    <span class=\"input-group-text\">\n" +
                "                                        <i class=\"lnr-calendar-full\"></i>\n" +
                "                                    </span>\n" +
                "                                </div>\n" +
                "                                <input name=\"#inputName#\" id=\"#inputId#\" type=\"text\" class=\"form-control\"\n" +
                "                                       data-toggle=\"datepicker-icon\" readonly=\"readonly\">\n" +
                "                                <div class=\"input-group-append datepicker-reset-trigger\">\n" +
                "                                    <span class=\"input-group-text\">\n" +
                "                                        <i class=\"lnr-cross\"></i>\n" +
                "                                    </span>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </div>";
        final String formText = "<div class=\"position-relative form-group\">\n"
                + "                            <label for=\"#inputId#\" class=\"\" data-i18n-txt=\"#i18nName#\"></label>\n"
                + "                            <input name=\"#inputName#\" id=\"#inputId#\" type=\"text\" class=\"form-control\"\n"
                + "                                   data-rule-required=\"true\">\n"
                + "                        </div>";
        final String formSelect = "<div class=\"position-relative form-group\">\n"
                + "                            <label for=\"#inputId#\" class=\"\" data-i18n-txt=\"#i18nName#\"></label>\n"
                + "                            <select name=\"#inputName#\" id=\"#inputId#\" class=\"form-control\" data-dict-id=\"#dictId#\"\n"
                + "                                    style=\"width:100%;\" data-rule-required=\"true\">\n"
                + "                            </select>\n" + "                        </div>";
        final String formDate = "<div class=\"position-relative form-group\">\n" +
                "                            <label for=\"#inputId#\" class=\"\" data-i18n-txt=\"#i18nName#\"></label>\n" +
                "                            <div class=\"input-group\">\n" +
                "                                <div class=\"input-group-prepend\">\n" +
                "                                    <span class=\"input-group-text\">\n" +
                "                                        <i class=\"lnr-calendar-full\"></i>\n" +
                "                                    </span>\n" +
                "                                </div>\n" +
                "                                <input name=\"#inputName#\" id=\"#inputId#\" type=\"text\" class=\"form-control\"\n" +
                "                                       data-rule-required=\"true\" data-toggle=\"datepicker-icon\" readonly=\"readonly\">\n" +
                "                                <div class=\"input-group-append datepicker-reset-trigger\">\n" +
                "                                    <span class=\"input-group-text\">\n" +
                "                                        <i class=\"lnr-cross\"></i>\n" +
                "                                    </span>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </div>";
        final String formTextarea = "<div class=\"position-relative form-group\">\n"
                + "                            <label for=\"#inputId#\" class=\"\" data-i18n-txt=\"#i18nName#\"></label>\n"
                + "                            <textarea name=\"#inputName#\" id=\"#inputId#\" class=\"form-control\"\n"
                + "                                      data-rule-required=\"true\"></textarea>\n"
                + "                        </div>";
        final String datepickerInit = "\\$('[data-toggle=\"datepicker-icon\"]').each(function () {\n" +
                "        \\$(this).datepicker({\n" +
                "            resetTrigger: \\$(this).parent().find('.datepicker-reset-trigger'),\n" +
                "            autoHide: true,\n" +
                "            format: 'yyyy-MM-dd',\n" +
                "            language: \\$.cookie('userLang')\n" +
                "        });\n" +
                "    });";
        final String datepickerJS = "<script type=\"text/javascript\" src=\"./assets/scripts/vendors/form-components/datepicker.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"./assets/scripts/vendors/form-components/datepicker.zh_CN.js\"></script>";

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(configFile))) {
            XSSFSheet sheet = wb.getSheetAt(0);
            int rows = sheet.getLastRowNum();
            XSSFRow row;
            StringBuilder buff = new StringBuilder();

            for (int n = 3; n < rows; n++) {
                row = sheet.getRow(n);
                final String status = cell2str(row.getCell(0));
                final String moduleTitle = cell2str(row.getCell(1));
                if (Utility.isEmpty(status) && Utility.notEmpty(moduleTitle)) {
                    final String moduleEditTitle = cell2str(row.getCell(2));
                    final String moduleName = cell2str(row.getCell(3));
                    final String moduleIcon = cell2str(row.getCell(4));
                    final String path = cell2str(row.getCell(5));
                    final String jspName = cell2str(row.getCell(6));
                    final String jsName = cell2str(row.getCell(7));
                    String pkId = cell2str(row.getCell(8));
                    final String pkName = cell2str(row.getCell(9));
                    if (Utility.isEmpty(pkId)) {
                        buff.delete(0, buff.length());
                        String[] words = pkName.split("_");
                        for (String tmp : words) {
                            if (buff.length() == 0) {
                                buff.append(tmp);
                            } else {
                                buff.append(tmp.substring(0, 1).toUpperCase());
                                buff.append(tmp.substring(1));
                            }
                        }
                        pkId = buff.toString();
                    }
                    final String pageKey = cell2str(row.getCell(10));
                    String ifPrefix = cell2str(row.getCell(11));
                    final String actionName = cell2str(row.getCell(12));
                    if (Utility.isEmpty(ifPrefix)) {
                        ifPrefix = actionName.substring(0, 1).toLowerCase() + actionName.substring(1);
                    }
                    final String beanName = cell2str(row.getCell(13));
                    final String tableName = cell2str(row.getCell(14));
                    String mapperFilterColumns = cell2str(row.getCell(15));
                    final String columns = cell2str(row.getCell(16));
                    String values;
                    buff.delete(0, buff.length());
                    for (String tmp : columns.split(",")) {
                        if (buff.length() > 0)
                            buff.append(",");
                        buff.append("#{").append(tmp.toLowerCase()).append("}");
                    }
                    values = buff.toString();

                    String updateColumns = cell2str(row.getCell(17));
                    buff.delete(0, buff.length());
                    for (String tmp : updateColumns.split(",")) {
                        if (buff.length() > 0)
                            buff.append(",");
                        buff.append(tmp).append(" = #{").append(tmp.toLowerCase()).append("}");
                    }
                    updateColumns = buff.toString();

                    // 元素类型:元素ID:元素名称:i18n键名:字典ID(select必须,其它为空)
                    String[] filterColumns = new String[0];
                    String fcs = cell2str(row.getCell(18));
                    if (Utility.notEmpty(fcs)) {
                        filterColumns = fcs.split(",");
                    }
                    // 元素类型:元素ID:元素名称:i18n键名:i18n键值:字典ID(select必须,其它为空)
                    String[] editColumns = new String[0];
                    String ecs = cell2str(row.getCell(19));
                    if (Utility.notEmpty(ecs)) {
                        editColumns = ecs.split(",");
                    }

                    StringBuilder initFilterContent = new StringBuilder();
                    StringBuilder filterContent = new StringBuilder();
                    StringBuilder editContent = new StringBuilder();
                    StringBuilder listColumnContent = new StringBuilder();
                    StringBuilder initAddContent = new StringBuilder();
                    StringBuilder initEditContent = new StringBuilder();
                    StringBuilder initEditCommon = new StringBuilder();
                    String line;

                    System.out.println("Start build...");
                    System.out.println("  Start build [" + moduleName + "] files...");
                    // Build List JSP
                    boolean hasDateElem = false;
                    if (filterColumns.length > 0) {
                        for (String tmp : filterColumns) {
                            String[] arr = tmp.split(":");
                            if (filterContent.length() > 0)
                                filterContent.append("\n");
                            if (Objects.equals("select", arr[0])) {
                                filterContent.append(
                                        filterSelect.replaceAll("#inputId#", arr[1]).replaceAll("#inputName#", arr[2])
                                                .replaceAll("#i18nName#", arr[3]).replaceAll("#dictId#", arr[4]));
                                initFilterContent.append("initNormalSelect('").append(arr[1]).append("');");
                            } else if (Objects.equals("date", arr[0])) {
                                filterContent.append(filterDate.replaceAll("#inputId#", arr[1])
                                        .replaceAll("#inputName#", arr[2]).replaceAll("#i18nName#", arr[3]));
                                hasDateElem = true;
                            } else {
                                filterContent.append(filterText.replaceAll("#inputId#", arr[1])
                                        .replaceAll("#inputName#", arr[2]).replaceAll("#i18nName#", arr[3]));
                            }
                        }
                        if (hasDateElem)
                            initFilterContent.append("\n").append(datepickerInit);
                    }
                    File folder = new File("src\\main\\webapp\\WEB-INF\\views\\" + path);
                    if (!folder.exists())
                        folder.mkdirs();
                    try (BufferedReader reader = new BufferedReader(new FileReader("tpl\\template.jsp"));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(
                                 "src\\main\\webapp\\WEB-INF\\views\\" + path + "\\" + jspName + ".jsp"))) {
                        line = reader.readLine();
                        while (Objects.nonNull(line)) {
                            line = line.replaceAll("#moduleName#", moduleName);
                            line = line.replaceAll("#pkId#", pkId);
                            line = line.replaceAll("#pkName#", pkName);
                            line = line.replaceAll("#moduleIcon#", moduleIcon);
                            line = line.replaceAll("#filterContent#", filterContent.toString());
                            line = line.replaceAll("#datepickerJS#", hasDateElem ? datepickerJS : "");
                            writer.write(line);
                            writer.newLine();
                            line = reader.readLine();
                        }

                        System.out.println("    Build " + jspName + ".jsp success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Build Edit JSP
                    hasDateElem = false;
                    if (editColumns.length > 0) {
                        int cols = 0;
                        for (String tmp : editColumns) {
                            String[] arr = tmp.split(":");
                            if (editContent.length() > 0)
                                editContent.append("\n");

                            if (cols == 0 || cols == 2) {
                                cols = Integer.parseInt(arr[1]);
                                editContent.append("<div class=\"form-row\">\n");
                            } else {
                                cols += Integer.parseInt(arr[1]);
                            }

                            if (Integer.parseInt(arr[1]) == 2) {
                                editContent.append("<div class=\"col-md-12\">\n");
                            } else {
                                editContent.append("<div class=\"col-md-6\">\n");
                            }

                            if (Objects.equals("text", arr[0]))
                                editContent.append(formText.replaceAll("#inputId#", arr[2])
                                        .replaceAll("#inputName#", arr[3]).replaceAll("#i18nName#", arr[4]));
                            else if (Objects.equals("textarea", arr[0]))
                                editContent.append(formTextarea.replaceAll("#inputId#", arr[2])
                                        .replaceAll("#inputName#", arr[3]).replaceAll("#i18nName#", arr[4]));
                            else if (Objects.equals("select", arr[0]))
                                editContent.append(
                                        formSelect.replaceAll("#inputId#", arr[2]).replaceAll("#inputName#", arr[3])
                                                .replaceAll("#i18nName#", arr[4]).replaceAll("#dictId#", arr[6]));
                            else if (Objects.equals("date", arr[0])) {
                                editContent.append(
                                        formDate.replaceAll("#inputId#", arr[2]).replaceAll("#inputName#", arr[3])
                                                .replaceAll("#i18nName#", arr[4]));
                                hasDateElem = true;
                            }
                            editContent.append("\n</div>\n");

                            if (cols == 2) {
                                editContent.append("</div>\n");
                            }
                        }
                    }
                    try (BufferedReader reader = new BufferedReader(new FileReader("tpl\\template_edit.jsp"));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(
                                 "src\\main\\webapp\\WEB-INF\\views\\" + path + "\\" + jspName + "_edit.jsp"))) {
                        line = reader.readLine();
                        while (Objects.nonNull(line)) {
                            line = line.replaceAll("#moduleName#", moduleName);
                            line = line.replaceAll("#pkId#", pkId);
                            line = line.replaceAll("#pkName#", pkName);
                            line = line.replaceAll("#moduleIcon#", moduleIcon);
                            line = line.replaceAll("#ifPrefix#", ifPrefix);
                            line = line.replaceAll("#editContent#", editContent.toString());
                            line = line.replaceAll("#datepickerJS#", hasDateElem ? datepickerJS : "");
                            writer.write(line);
                            writer.newLine();
                            line = reader.readLine();
                        }

                        System.out.println("    Build " + jspName + "_edit.jsp success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Build JS File
                    hasDateElem = false;
                    if (editColumns.length > 0) {
                        for (String tmp : editColumns) {
                            String[] arr = tmp.split(":");
                            listColumnContent.append("\n,");
                            if (initEditContent.length() > 0)
                                initEditContent.append("\n");
                            if (initAddContent.length() > 0)
                                initAddContent.append("\n");
                            if (Objects.equals("select", arr[0])) {
                                initEditContent.append("initNormalSelect('").append(arr[2]).append("', ");
                                initEditContent.append(moduleName).append("['").append(arr[3]).append("']);");
                                initAddContent.append("initNormalSelect('").append(arr[2]).append("');");
                            } else if (Objects.equals("date", arr[0])) {
                                initEditContent.append("\\$('#").append(arr[2]).append("').datepicker('setDate', ");
                                initEditContent.append(moduleName).append("['").append(arr[3]).append("']);");
                                hasDateElem = true;
                            } else {
                                initEditContent.append("\\$('#").append(arr[2]).append("').val(");
                                initEditContent.append(moduleName).append("['").append(arr[3]).append("']);");
                            }
                            listColumnContent.append("{field: '").append(arr[3]).append("', ");
                            listColumnContent.append("title: \\$.i18n.prop('").append(arr[4]).append("'), ");
                            listColumnContent.append("halign: 'center', align: 'center', width: 100}");
                        }
                        if (initAddContent.length() > 0) {
                            initAddContent.insert(0, " else {\n");
                            initAddContent.append("\n}\n");
                        }
                        if (hasDateElem)
                            initEditCommon.append(datepickerInit);
                    }
                    try (BufferedReader reader = new BufferedReader(new FileReader("tpl\\view.template.js"));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(
                                 "src\\main\\webapp\\assets\\scripts\\views\\view." + jsName + ".js"))) {
                        line = reader.readLine();
                        while (Objects.nonNull(line)) {
                            line = line.replaceAll("#path#", path);
                            line = line.replaceAll("#jspName#", jspName);
                            line = line.replaceAll("#moduleName#", moduleName);
                            line = line.replaceAll("#pkId#", pkId);
                            line = line.replaceAll("#pkName#", pkName);
                            line = line.replaceAll("#ifPrefix#", ifPrefix);
                            line = line.replaceAll("#pageKey#", pageKey);
                            line = line.replaceAll("#initEditCommon#", initEditCommon.toString());
                            line = line.replaceAll("#initFilterContent#", initFilterContent.toString());
                            line = line.replaceAll("#listColumnContent#", listColumnContent.toString());
                            line = line.replaceAll("#initEditContent#", initEditContent.toString());
                            line = line.replaceAll("#initAddContent#", initAddContent.toString());
                            writer.write(line);
                            writer.newLine();
                            line = reader.readLine();
                        }

                        System.out.println("    Build view." + jsName + ".js success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Build i18n File
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter("src\\main\\webapp\\assets\\i18n\\zh_CN\\" + moduleName + ".properties"))) {
                        writer.write("txt." + moduleName + ".title=" + moduleTitle);
                        writer.newLine();
                        writer.write("txt." + moduleName + ".edit.title=" + moduleEditTitle);
                        writer.newLine();
                        if (editColumns.length > 0) {
                            for (String tmp : editColumns) {
                                String[] arr = tmp.split(":");
                                writer.write(arr[4] + "=" + arr[5]);
                                writer.newLine();
                            }
                        }

                        System.out.println("    Build zh_CN/" + moduleName + ".properties success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter("src\\main\\webapp\\assets\\i18n\\en_US\\" + moduleName + ".properties"))) {
                        writer.write("txt." + moduleName + ".title=");
                        writer.newLine();
                        writer.write("txt." + moduleName + ".edit.title=");
                        writer.newLine();
                        if (editColumns.length > 0) {
                            for (String tmp : editColumns) {
                                String[] arr = tmp.split(":");
                                writer.write(arr[4] + "=");
                                writer.newLine();
                            }
                        }

                        System.out.println("    Build en_US/" + moduleName + ".properties success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Build Java Action File
                    try (BufferedReader reader = new BufferedReader(new FileReader("tpl\\TemplateAction.java"));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(
                                 "src\\main\\java\\cn\\tgm\\msf\\action\\" + actionName + "Action.java"))) {
                        line = reader.readLine();
                        while (Objects.nonNull(line)) {
                            line = line.replaceAll("#actionName#", actionName);
                            line = line.replaceAll("#beanName#", beanName);
                            line = line.replaceAll("#getPkMethod#",
                                    "get" + pkName.substring(0, 1).toUpperCase() + pkName.substring(1));
                            line = line.replaceAll("#setPkMethod#",
                                    "set" + pkName.substring(0, 1).toUpperCase() + pkName.substring(1));
                            writer.write(line);
                            writer.newLine();
                            line = reader.readLine();
                        }

                        System.out.println("    Build " + actionName + "Action.java success.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Build SQLMapper File
                    if (Utility.notEmpty(mapperFilterColumns)) {
                        buff.delete(0, buff.length());
                        for (String tmp : mapperFilterColumns.split(",")) {
                            String[] arr = tmp.split(":");
                            if (buff.length() > 0)
                                buff.append("\n");
                            buff.append("<if test=\"").append(arr[0].toLowerCase()).append(" != null");
                            buff.append(" and ").append(arr[0].toLowerCase()).append(" != ''\">\n");
                            if (Objects.equals("1", arr[1]))
                                buff.append(" AND T.").append(arr[0].toUpperCase()).append(" = #{")
                                        .append(arr[0].toLowerCase()).append("}");
                            else
                                buff.append(" AND T.").append(arr[0].toUpperCase()).append(" LIKE CONCAT('%', #{")
                                        .append(arr[0].toLowerCase()).append("},'%')");
                            buff.append("\n</if>");
                        }
                        mapperFilterColumns = buff.toString();
                    }
                    try (BufferedReader reader = new BufferedReader(new FileReader("tpl\\TemplateMapper.xml"));
                         BufferedWriter writer = new BufferedWriter(
                                 new FileWriter("src\\main\\resources\\mappers\\" + beanName + "Mapper.xml"))) {
                        line = reader.readLine();
                        while (Objects.nonNull(line)) {
                            line = line.replaceAll("#beanName#", beanName);
                            line = line.replaceAll("#mapperFilterColumns#", mapperFilterColumns);
                            line = line.replaceAll("#columns#", columns);
                            line = line.replaceAll("#values#", values);
                            line = line.replaceAll("#updateColumns#", updateColumns);
                            line = line.replaceAll("#pkName#", pkName);
                            line = line.replaceAll("#pkUpperName#", pkName.toUpperCase());
                            line = line.replaceAll("#tableName#", tableName);
                            writer.write(line);
                            writer.newLine();
                            line = reader.readLine();
                        }

                        System.out.println("    Build " + beanName + "Mapper.xml success.");

                        System.out.println("End build.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String cell2str(XSSFCell cell) {

        if (cell == null) {
            return "";
        } else if (cell.getCellTypeEnum() == CellType.BLANK) {
            return "";
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf((new Double(cell.getNumericCellValue())).intValue());
        } else {
            System.out.println(cell.toString() + " can't convert to a String value.");
            return "";
        }
    }

    /**
     * 写文件
     *
     * @param inFile
     * @param outPath
     * @param extName
     * @return
     */
    public static String writeFile(String inFile, String outPath, String extName) throws IOException {

        if (Utility.isEmpty(outPath))
            return null;

        outPath = mkdirs(outPath);

        String fileUrl;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS", Locale.CHINA);
        String newFileName = sdf.format(new Date());

        byte[] bs = new byte[1024];
        int len;

        try (InputStream in = new FileInputStream(new File(inFile));
             OutputStream out = new FileOutputStream(outPath + newFileName + "." + extName)) {
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            out.flush();
            fileUrl = outPath + newFileName + "." + extName;
        }

        return fileUrl;
    }

    /**
     * 获取图片字节流
     *
     * @param fileUrl
     * @return
     */
    public static byte[] readImage(String fileUrl) throws IOException {

        byte[] bs;

        try (InputStream in = new FileInputStream(new File(fileUrl))) {
            bs = new byte[in.available()];
            in.read(bs);
        }

        return bs;
    }

    /**
     * @param path
     * @return
     */
    public static String mkdirs(String path) {

        return mkdirs(path, new Date());
    }

    /**
     * @param path
     * @return
     */
    public static String mkdirs(String path, Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.CHINA);
        String ctxPath = path + File.separator + sdf.format(date) + File.separator;
        sdf.applyPattern("dd");
        ctxPath = ctxPath + sdf.format(date) + File.separator;

        File folder = new File(ctxPath);

        // 文件夹不存在时
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return ctxPath;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {

        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;

        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        return fileSizeString;
    }

    /**
     * 删除指定目录下的所有文件列表，包括子目录。
     *
     * @param baseDir File 指定的目录
     * @return 成功返回true，否则false
     */
    public static boolean deleteFiles(File baseDir) {

        if (Objects.isNull(baseDir))
            return true;

        File[] files = baseDir.listFiles();
        if (Objects.nonNull(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    if (!file.delete())
                        return false;
                }
            }
        }

        return baseDir.delete();
    }

    public static void main(String[] args) {

//        FileUtils.generateMessageKeyClass("i18n\\message_zh_CN", "MessageKey", null);

//        FileUtils.generateModuleFiles("src\\main\\resources\\AutoCodingConfig.xlsx");
    }
}
