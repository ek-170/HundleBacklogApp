package com.muramoto.extract.logics;

import org.apache.poi.ss.usermodel.*;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.github.difflib.text.*;

public class UpdateSheet {
    private static final String SHEETNAME                       = "課題リスト(名称変更不可)";
    private static final String SHEETNAME_FOR_UPDATE            = "【システム用シート】編集厳禁";
    private static final int    FIRST_ROW_NUM_WITHOUT_HEADER    = 2;

    public void update(int keyIndex, 
                       Map<String,List<String>> cellValuesNewMap, 
                       Map<String,List<String>> cellValuesUpdateMap, 
                       Workbook wb, 
                       Sheet sheet4Update){

        System.out.println("Updating Sheet..");

        // 最初にコメント情報を取得。この後コメントがある表示用シートは削除するため
        String commentString      = IssueHeader.valueOf("Comment").getLabel();
        int    commentIndex       = POIUtil.getKeyIndex(sheet4Update, commentString);
        Sheet  sheet4DisplayOld   = wb.getSheet(SHEETNAME);
        int    commentIndexS4D    = POIUtil.getKeyIndex(sheet4DisplayOld, commentString);
        Map<String,String> commentMap = getComments(keyIndex, commentIndexS4D, sheet4DisplayOld);

        // 表示用シート削除
        int sheet4DisplayOldIndex = wb.getSheetIndex(sheet4DisplayOld);
        wb.removeSheetAt(sheet4DisplayOldIndex);

        // sheet4Updateのシート名を表示用シート名に変更
        int sheetIndex = wb.getSheetIndex(sheet4Update);
        wb.setSheetName(sheetIndex, SHEETNAME);

        // sheet4Updateに新規レコード追加
        addNewRecords(cellValuesNewMap, wb, sheet4Update);

        // 差分比較更新処理を実施する前に次回更新用のシステムシートを作成
        Sheet sheet4Display = POIUtil.copySheetInHidden(wb, sheet4Update, SHEETNAME_FOR_UPDATE);
                        
        // システム用シートの値を更新
        Map<String,Row> rowMapS4D = getRows(keyIndex,sheet4Display);
        int lastColumnNum = sheet4Display.getRow(FIRST_ROW_NUM_WITHOUT_HEADER-1).getLastCellNum();
        updateEachCellWithoutComment(cellValuesUpdateMap,
                                     wb,
                                     rowMapS4D,
                                     commentIndex,
                                     lastColumnNum,
                                     false);

        // システム用シートのA列の値をクリア
        String isNewOrUpdateString = IssueHeader.valueOf("NewUpdate").getLabel();
        int    indexIsNewOrUpdate  = POIUtil.getKeyIndex(sheet4Display, isNewOrUpdateString);
        POIUtil.updateCellsInColumn(sheet4Display, null, "", indexIsNewOrUpdate, FIRST_ROW_NUM_WITHOUT_HEADER, lastColumnNum);

        // sheet4Updateに差分比較処理
        Map<String,Row> rowMapS4U = getRows(keyIndex,sheet4Update);
        updateEachCellWithoutComment(cellValuesUpdateMap,
                                     wb,
                                     rowMapS4U,
                                     commentIndex,
                                     lastColumnNum,
                                     true);
    
        // sheet4Updateのコメント列更新
        for(Map.Entry<String, String> entry : commentMap.entrySet()) {
            Row row = rowMapS4U.get(entry.getKey());
            if(row != null){
                row.getCell(commentIndex).setCellValue(entry.getValue());
            }
        }
        POIUtil.setColumnWidthAuto(sheet4Update);
        POIUtil.setColumnWidthAuto(sheet4Display);

        SheetVisibility visibility = SheetVisibility.valueOf("VISIBLE");
        wb.setSheetVisibility(sheetIndex, visibility);
    }


    private static final String CELL_TYPE_NUMERIC = "NUMERIC";
    private static final int    COLUMN_NUM_WITHOUT_FIRST_COLUMN = 1;

    private void updateEachCellWithoutComment(Map<String,List<String>> cellValuesUpdateMap, 
                                              Workbook wb, 
                                              Map<String,Row> rowMap,
                                              int commentIndex,
                                              int lastColumnNum,
                                              boolean showDiff){
        // Sheetから行を抽出し、Mapにする
        DiffRowGenerator generator = DiffRowGenerator.create()
                                                        .showInlineDiffs(true)
                                                        .inlineDiffByWord(true)
                                                        .ignoreWhiteSpaces(true)
                                                        .oldTag(f -> " -- ")
                                                        .newTag(f -> " ++ ")
                                                        .mergeOriginalRevised(true)

                                                        .build();
        CellStyle defaultStyle = POIUtil.setDefaultStyle(wb);
        CellStyle highlightStyle = POIUtil.setHighlightStyle(wb);
        for(Map.Entry<String, List<String>> entry : cellValuesUpdateMap.entrySet()) {
            Row row = rowMap.get(entry.getKey());            
            List<String> cellValuesUpdate = entry.getValue();

            int index = 0;
            boolean isUpdateRow = false;
            for(int i=COLUMN_NUM_WITHOUT_FIRST_COLUMN; i<lastColumnNum; i++){
                if(i == commentIndex){
                     continue; 
                }
                String cellType = row.getCell(i).getCellType().toString();
                String cellValueOfSheet = "";
                if(cellType.equals(CELL_TYPE_NUMERIC)){
                    cellValueOfSheet = String.valueOf(row.getCell(i).getNumericCellValue());
                }else{
                    cellValueOfSheet = row.getCell(i).getStringCellValue();
                }

                String cellValueUpdate  = cellValuesUpdate.get(index);
                if(!cellValueUpdate.equals(cellValueOfSheet)){
                    String cellValue = "";
                    if(showDiff){
                        cellValue = compareDiff(generator, cellValueOfSheet, cellValueUpdate);
                        row.setHeight((short)-1);
                        POIUtil.updateCell(cellValue, row, highlightStyle, i);
                    }else{
                        cellValue = cellValueUpdate;
                        row.setHeight((short)-1);
                        POIUtil.updateCell(cellValue, row, defaultStyle, i);
                    }
                    isUpdateRow = true;
                }
                index++;
            }
            if(isUpdateRow){
                POIUtil.updateCell("Update", row, null, 0);
            }
        }
    }

    private Map<String,Row> getRows(int keyIndex, Sheet sheet){
        Map<String,Row> result = new HashMap<>();

        int lastRowNum = sheet.getLastRowNum();
        for(int i=FIRST_ROW_NUM_WITHOUT_HEADER; i<=lastRowNum; i++){
            Row    row = sheet.getRow(i);
            String key = row.getCell(keyIndex).getStringCellValue();
            result.put(key,row);
        }
        return result;
    }

    private Map<String,String> getComments(int keyIndex, int commentIndex,Sheet sheet){
        Map<String,String> result = new HashMap<>();
        Map<String,Row>    rowMap = getRows(keyIndex, sheet);
        for(Map.Entry<String, Row> entry : rowMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getCell(commentIndex).getStringCellValue());
        }
        return result;
    }

    private List<List<String>> convertMapToList(Map<String,List<String>> map){
        List<List<String>> result = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

        private String compareDiff(DiffRowGenerator generator, String cellValueOfSheet, String cellValueUpdate){
        List<String> original = new ArrayList<>();
        original.add(cellValueOfSheet);
        
        List<String> revised = new ArrayList<>();
        revised.add(cellValueUpdate);

        List<DiffRow> diffCellValue = generator.generateDiffRows(original, revised);

        StringBuilder result = new StringBuilder();
        
        for(int j=0; j<diffCellValue.size(); j++){
            result.append(diffCellValue.get(j).getOldLine());
            result.append("\r\n");
        }
        return result.toString();
    }

    private void addNewRecords(Map<String,List<String>> cellValuesNewMap, Workbook wb, Sheet sheet){
        int sizeOfNewValues = cellValuesNewMap.size();
        int lastRowNum      = sheet.getLastRowNum();
        if(sizeOfNewValues != 0){
            sheet.shiftRows(FIRST_ROW_NUM_WITHOUT_HEADER, lastRowNum, sizeOfNewValues);
        }
        List<List<String>> cellValuesNew = convertMapToList(cellValuesNewMap);
        for(List<String> cvn : cellValuesNew){
            cvn.add(0, "New");
            cvn.add(4, "");
        }
        new CreateSheet(FIRST_ROW_NUM_WITHOUT_HEADER, sheet).create(cellValuesNew, POIUtil.setDefaultStyle(wb));
    }
}