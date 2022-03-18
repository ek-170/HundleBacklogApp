package com.muramoto.extract.backlog;

import java.io.File;

import com.muramoto.extract.util.PathUtil;
import com.nulabinc.backlog4j.BacklogClient;
import com.nulabinc.backlog4j.Issue;
import com.nulabinc.backlog4j.api.option.GetIssuesParams;
import java.util.List;
import java.util.ArrayList;

/**
 * Backlogから課題を抽出
 */
public class ExtractIssues{
    private static final String CONFIG = "config.properties";
    private static final String QUERY  = "issuequeryparams.properties";
    
    public static List<Issue> extract(){
        BacklogClient       backlog    = GenBacklogClient.generate(PathUtil.getBasePath() + File.separator + CONFIG);
        long                offset     = 0L;
        List<Issue>         issues = new ArrayList<>();
        
        for(int i=1; true; i++){
            GetIssuesParams qParams = GenGetIssuesParams.generate(PathUtil.getBasePath() + File.separator + QUERY, offset);
            issues.addAll(backlog.getIssues(qParams));
                System.out.println("取得した課題数: " + issues.size());
            if(isEndLoop(issues.size(), i)){
                break;
            }
            offset += 100;
        }
        return issues;
    }

    private static boolean isEndLoop(int listSize, int loopCount){
      return listSize/loopCount < 100 ?  true : false;
    }

    
}