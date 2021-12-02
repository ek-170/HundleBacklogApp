package com.muramoto.extract.logics;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.*;
import com.nulabinc.backlog4j.BacklogAPIException;

public class FileUtil {

    public static String checkExixtSameFileName(String path, String extension) {
        File f = new File(path + extension);

        for (int i = 1; f.exists(); i++) {
             f = new File(path + "(" + i + ")" + extension);
        }
        return f.getPath();
    }

    public static Workbook loadExecelFile(String filePath){
        try (InputStream is = new FileInputStream(filePath)){
            return WorkbookFactory.create(is);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new BacklogAPIException("ファイルの読み込みに失敗しました");
        }
    }

    public static void outputExcelFile(Workbook wb, String path){
        try(FileOutputStream fos = new FileOutputStream(path)){
            wb.write(fos);
        }catch(IOException e){
            e.printStackTrace();
            throw new BacklogAPIException("Excelの出力に失敗しました");
        }
    }
}