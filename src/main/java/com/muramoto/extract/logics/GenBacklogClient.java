package com.muramoto.extract.logics;

import com.nulabinc.backlog4j.BacklogClient;
import com.nulabinc.backlog4j.BacklogClientImpl;
import com.nulabinc.backlog4j.BacklogAPIException;
import com.nulabinc.backlog4j.conf.*;
import java.util.Map;
import java.net.MalformedURLException;

public class GenBacklogClient{

    private static final String COM      = "backlog.com";
    private static final String JP       = "backlog.jp";
    private static final String TOOL_COM = "backlogtool.com";

    public static BacklogClient generate(String configPropFilePath){
        System.out.println("Backlogに接続中");

        Map<String, String> configMap = ReadProps.get(configPropFilePath);

        String apiKey          = configMap.get("apiKey");
        String backlogSpaceURL = configMap.get("backlogSpaceURL");
        String spaceId         = configMap.get("spaceId");

        if(apiKey==null || backlogSpaceURL==null || spaceId==null){
            throw new BacklogAPIException("apiKey,backlogSpaceURL,spaceIdのいずれかがnullです") ;
        }else{
            return new BacklogClientImpl(setBacklogConf(apiKey,backlogSpaceURL,spaceId));
        }
    }


    private static BacklogConfigure setBacklogConf(String apiKey, String backlogSpaceURL, String spaceId){
        try{
            if(backlogSpaceURL.contains(COM)){
                return new BacklogComConfigure(spaceId).apiKey(apiKey);
            }else if(backlogSpaceURL.contains(JP)){
                return new BacklogJpConfigure(spaceId).apiKey(apiKey);
            }else if(backlogSpaceURL.contains(TOOL_COM)){
                return new BacklogToolConfigure(spaceId).apiKey(apiKey);
            }else{
                throw new BacklogAPIException("URLが不正です: " + backlogSpaceURL);
            }
        }catch(MalformedURLException e){
          e.printStackTrace();
          throw new BacklogAPIException("URLが不正です。: " + backlogSpaceURL);
        }
    }
}