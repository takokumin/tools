package cn.tgm.tools.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Objects;

/**
 * ExcelUtils
 *
 * @author tianguomin
 * @version 1.0
 */
public class ExcelUtils {

    /**
     * 导出数据
     *
     * @param title    标题
     * @param columns  列名
     * @param dataList 行数据
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook export(String title, String[] columns, List<Object[]> dataList) throws Exception {

        if (Objects.isNull(title) || Objects.isNull(columns) || Objects.isNull(dataList)) {
            throw new Exception("No title or row data found.");
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet(title);

        // 产生表格标题行
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell cellTitle = titleRow.createCell(0);

        // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
        HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook); // 获取列头样式对象
        HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (columns.length - 1)));
        cellTitle.setCellStyle(columnTopStyle);
        cellTitle.setCellValue(title);

        // 定义所需列数
        int columnNum = columns.length;
        HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(CellType.STRING); // 设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(columns[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
        }

        // 将数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {

            Object[] obj = dataList.get(i); // 遍历每个对象
            HSSFRow row = sheet.createRow(i + 3); // 创建所需的行数

            for (int j = 0; j < obj.length; j++) {
                HSSFCell cell; // 设置单元格的数据类型
//                if (j == 0) {
//                    cell = row.createCell(j, CellType.NUMERIC);
//                    cell.setCellValue(i + 1);
//                } else {
                cell = row.createCell(j, CellType.STRING);
                if (Objects.isNull(obj[j])) {
                    // null设为"",防止调整列宽时空指针错误
                    obj[j] = "";
                }
                cell.setCellValue(obj[j].toString()); // 设置单元格的值
//                }
                cell.setCellStyle(style); // 设置单元格样式
            }
        }

        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }

//            if (workbook != null) {
//                try {
//                    String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
//                    String headStr = "attachment; filename=\"" + fileName + "\"";
//                    response = getResponse();
//                    response.setContentType("APPLICATION/OCTET-STREAM");
//                    response.setHeader("Content-Disposition", headStr);
//                    OutputStream out = response.getOutputStream();
//                    workbook.write(out);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        return workbook;
    }

    /*
     * 列头单元格样式
     */
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /*
     * 列数据信息单元格样式
     */
    private static HSSFCellStyle getStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        //font.setFontHeightInPoints((short)10);
        // 字体加粗
        //font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

}
