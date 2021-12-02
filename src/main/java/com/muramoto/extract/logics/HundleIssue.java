package com.muramoto.extract.logics;

import com.nulabinc.backlog4j.Issue;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;

public class HundleIssue{ 

    private static final String EXTENSION            = ".xlsx";
    private static final String SUFFIX               = "_u";
    private static final String SHEETNAME            = "課題リスト(名称変更不可)";
    private static final String SHEETNAME_FOR_UPDATE = "【システム用シート】編集厳禁";


    public void extractIssues(){
        System.out.println("課題の抽出を開始します");
        List<Issue> issues = ExtractIssues.extract();
        System.out.println("課題の抽出が完了しました");

        if(issues.isEmpty()){
            System.out.println("課題の取得結果が0件です。条件を変更して再度検索してください");
        }else{
            selectMode(issues);
        }
    }

    private void selectMode(List<Issue> issues){
        System.out.println("実行する機能を選択してください\nExcelを新規作成：'c'\nExcelを更新    ：'u'");
        Scanner mode = new Scanner(System.in);
        switch(mode.next()){
            case "c":
            createNewFile(issues);
            break;

            case "u":
            updateFile(issues);
            break;

            default:
            System.out.println("不正な値です。再入力してください");
            selectMode(issues);
        }

    }   

    private void createNewFile(List<Issue> issues){
        System.out.println("Excelを新規作成します");
        List<List<String>> issuesStr = ParseIssue.toStringIssues(issues);
        // A、E列には空白を設定
        for(List<String> issueStr : issuesStr){
            issueStr.add(0,"");
            issueStr.add(4,"");
        }
        Header[]           header    = IssueHeader.values();
        Workbook           wb        = new XSSFWorkbook();
        Sheet              sheet     = wb.createSheet(SHEETNAME);
        CellStyle          style     = POIUtil.setDefaultStyle(wb);

        CreateSheet ch = new CreateSheet(sheet);
        ch.writeHeader( style, header).create(issuesStr, style);
        POIUtil.copySheetInHidden(wb, sheet, SHEETNAME_FOR_UPDATE);

        System.out.println("ファイル名を入力してください。（拡張子入力不要）");
        String fileName = new Scanner(System.in).next();
        String path     = PathUtil.getCreatedExcellDirPath() + fileName;
               path     = FileUtil.checkExixtSameFileName(path, EXTENSION);

        FileUtil.outputExcelFile(wb, path);
        System.out.println("ファイルの作成が完了しました");
    }

    private void updateFile(List<Issue> issues){
        System.out.println("Excelを更新します");
        System.out.println("Updateフォルダ内のxlsx形式のファイル名を入力してください。（拡張子入力不要）");
        String       fileName     = new Scanner(System.in).next();
        String       oldFilePath  = PathUtil.getUpdatedExcellDirPath() + fileName + EXTENSION;
        Workbook     wb           = FileUtil.loadExecelFile(oldFilePath);
        Sheet        sheet4Update = wb.getSheet(SHEETNAME_FOR_UPDATE);
        String       keyString    = IssueHeader.valueOf("Key").getLabel();
        int          keyIndex     = POIUtil.getKeyIndex(sheet4Update, keyString);
        List<String> keys         = POIUtil.getKeys(sheet4Update, keyIndex); 
        List<Issue>  newIssues    = new ArrayList<>();
        List<Issue>  updateIssues = new ArrayList<>();
        for(Issue i : issues){
            if(keys.contains(i.getIssueKey())){
                updateIssues.add(i);
            }else{
                newIssues.add(i);
            }
        }
        Map<String,List<String>> updateIssuesStrMap = ParseIssue.toStringIssuesMap(updateIssues);
        Map<String,List<String>> newIssuesStrMap    = ParseIssue.toStringIssuesMap(newIssues);

        new UpdateSheet().update(keyIndex,newIssuesStrMap, updateIssuesStrMap, wb, sheet4Update);

        String updateFilePath = PathUtil.getUpdatedExcellDirPath() + fileName + SUFFIX; 
               updateFilePath = FileUtil.checkExixtSameFileName(updateFilePath, EXTENSION);

        FileUtil.outputExcelFile(wb, updateFilePath);
        System.out.println("更新が完了しました");
    }
}