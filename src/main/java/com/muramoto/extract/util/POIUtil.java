package com.muramoto.extract.util;

import org.apache.poi.ss.usermodel.*;
import java.util.List;
import java.util.ArrayList;
import com.nulabinc.backlog4j.BacklogAPIException;
import java.util.Iterator;

public class POIUtil {

    public static int getKeyIndex(Sheet sheet, String keyStr){
        // ヘッダー行番号
        int firstRowNum  = sheet.getFirstRowNum();
        // ヘッダーRow
        Row row          = sheet.getRow(firstRowNum);
        // ヘッダー行のセル数
        int numberOfCell = row.getPhysicalNumberOfCells();

        // キー項目のセル番号を検索
        for(int i=0; i<numberOfCell ;i++){
            String cellValue = row.getCell(i).getStringCellValue();
            if(cellValue.equals(keyStr)){
                return row.getCell(i).getColumnIndex(); 
            }
        }
        throw new BacklogAPIException("ヘッダーにキー項目が無いため更新処理を続行できません");

    }

    // Sheetからキー項目をすべて抽出
    public static List<String> getKeys(Sheet sheet, int keyIndex){
        List<String> result = new ArrayList<>();
        int numberOfRow = sheet.getPhysicalNumberOfRows();
        // ヘッダー行番
        int headerRowNum = sheet.getFirstRowNum();
        // 最初のレコード行番
        int recordRowNum = headerRowNum + 1; 
        for(int i=recordRowNum; i<=numberOfRow ;i++){
            Row row = sheet.getRow(i);
            result.add(row.getCell(keyIndex).getStringCellValue());
        }
        return result;
    }


    public static CellStyle setDefaultStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();

        setDefaultBorder(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        setFont(wb, style,"游ゴシック");

        return style;
    }

    public static CellStyle setHighlightStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();

        setHighlightBorder(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        setFont(wb, style,"游ゴシック");

        return style;
    }

    public static void setFont(Workbook wb, CellStyle style, String fontName){
        Font font = wb.createFont();
        font.setFontName(fontName);
        style.setFont(font);
    }
 
    public static void setDefaultBorder(CellStyle style){
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }

    public static void setHighlightBorder(CellStyle style){
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        style.setTopBorderColor(IndexedColors.RED.getIndex());
        style.setBottomBorderColor(IndexedColors.RED.getIndex());
        style.setLeftBorderColor(IndexedColors.RED.getIndex());
        style.setRightBorderColor(IndexedColors.RED.getIndex());
    }

    public static void createCells(List<String> cellValuesStr, Row row, CellStyle style){
        int columnNum = 0;
        for(String c : cellValuesStr){
            if(columnNum > 0){
                createCell(c, row, style, columnNum);
            }else{
                createCell(c, row, null, columnNum);
            }
            columnNum++;
        }
    }

    public static void createCell(String cellValueStr, Row row, CellStyle style, int columnNum){
        Cell cell = row.createCell(columnNum);
        cell.setCellValue(cellValueStr);
        if(style != null){
            cell.setCellStyle(style);
        }
    }

    public static void updateCell(String cellValueStr, Row row, CellStyle style, int columnNum){
        Cell cell = row.getCell(columnNum);
        cell.setCellValue(cellValueStr);
        if(style != null){
            cell.setCellStyle(style);
        }
    }

    // Sheetにデータ入力後にコールしないと動作しません
    public static void setColumnWidthAuto(Sheet sheet){
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }
    
    public static Sheet copySheetInHidden(Workbook wb, Sheet sheet, String sheetName){
        Sheet result = wb.cloneSheet(wb.getSheetIndex(sheet));
        wb.setSheetName(wb.getSheetIndex(result), sheetName);
        wb.setSheetHidden(wb.getSheetIndex(result), true);

        return result;
    }

    public static void updateCellsInColumn(Sheet sheet, CellStyle style, String value, int columnNum, int startRowNum, int finishRowNum){
        for(int i=startRowNum; i<=finishRowNum; i++){
            Row row = sheet.getRow(i);
            if(row != null){
                updateCell(value, row, style, columnNum);
            }
        }
    }
}