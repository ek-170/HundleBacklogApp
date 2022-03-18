package com.muramoto.extract.config;

import com.nulabinc.backlog4j.BacklogAPIException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.Properties;
import java.util.Arrays;
import java.util.List;
import java.io.FileInputStream;


/**
 * プロパティファイルから各種情報を取得するクラス
 */
public class ReadProps{
    
    public static Map<String, String> get(String filePath){
        Map<String, String> result = new HashMap<>();
        Properties          props  = readPropFile(filePath);

        for(Map.Entry<Object, Object> entry : props.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }

    public static Map<String, List<String>> getAsList(String filePath){
        Map<String, List<String>> result = new HashMap<>();
        Properties                props  = readPropFile(filePath);

        for(Map.Entry<Object, Object> entry : props.entrySet()) {
            result.put(entry.getKey().toString(), Arrays.asList(entry.getValue().toString().split(",")));
        }
        return result;
    }

    private static Properties readPropFile(String filePath){
        Properties props = new Properties();

        try (InputStream is = new FileInputStream(filePath)){
            props.load(is);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new BacklogAPIException(e);
        }
        return props;
    }
}