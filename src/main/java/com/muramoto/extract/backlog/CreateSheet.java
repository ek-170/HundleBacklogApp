package com.muramoto.extract.backlog;

import org.apache.poi.ss.usermodel.*;
import java.util.List;

import com.muramoto.extract.util.POIUtil;

public class CreateSheet{

    private int rowNum;
    private Sheet sheet;

    public CreateSheet(Sheet sheet){
        this.sheet  = sheet;
        this.rowNum = 2; // ヘッダの直下の行。ファイルの新規作成用
    }

    public CreateSheet(int rowNum, Sheet sheet){
        this.sheet  = sheet;
        this.rowNum = rowNum; // 特定の行の下に行を追加する用
    }

    public void create(List<List<String>> cellValues, CellStyle style){
        System.out.println("Excelシートを作成中");

        for(List<String> cellvalue : cellValues){
            Row row = sheet.createRow(rowNum);
            row.setHeight((short)-1);
            POIUtil.createCells(cellvalue, row, style);
            rowNum++;
        }      

        POIUtil.setColumnWidthAuto(sheet);
    }

    public CreateSheet writeHeader(CellStyle style, Header[] header){
        Row headerRow = sheet.createRow(1);
        headerRow.setHeight((short)-1);
        int columnNum   = 0;
        for(Header h : header){
            Cell cell = headerRow.createCell(columnNum);
            cell.setCellValue(h.getLabel());
            if(cell.getColumnIndex() != 0){
                cell.setCellStyle(style);
            }
            columnNum ++;
        }
        return this;
    }
}