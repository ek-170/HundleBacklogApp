package com.muramoto.extract.logics;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import com.nulabinc.backlog4j.Issue;

public class ParseIssue {

    public static List<String> toStringIssue(Issue i){
        List<String> result = new ArrayList<>();
        result.add(i.getIssueKey());
        if(i.getSummary() == null){
            result.add("");
        }else{
            result.add(i.getSummary());
        }
        if(i.getDescription() == null){
            result.add("");
        }else{
            result.add(i.getDescription());
        }
        if(i.getIssueType() == null){
            result.add("");
        }else{
            result.add(i.getIssueType().getName());
        }
        if(i.getStatus() == null){
            result.add("");
        }else{
            result.add(i.getStatus().getName());
        }
        if(i.getMilestone() == null){
            result.add("");
        }else{
            result.add(MileStoneUtil.toStringMilestones(i.getMilestone()));
        }
        if(i.getVersions() == null){
            result.add("");
        }else{
            result.add(VersionUtil.toStringVersions(i.getVersions()));
        }
        if(i.getEstimatedHours() == null){
            result.add("");
        }else{
            result.add(i.getEstimatedHours().toString());
        }
        if(i.getActualHours() == null){
            result.add("");
        }else{
            result.add(i.getActualHours().toString());
        }
        if(i.getAssignee() == null){
            result.add("");
        }else{
            result.add(i.getAssignee().getName());
        }
        result.add(i.getCreatedUser().getName());
        result.add(DateUtil.dateFormat(i.getCreated(),true));
        
        return result;
    }
    
    public static List<List<String>> toStringIssues(List<Issue> iList){
        List<List<String>> result = new ArrayList<>();
        for(Issue i : iList){
            result.add(toStringIssue(i));
        }
        return result;
    }

    public static Map<String,List<String>> toStringIssuesMap(List<Issue> iList){
        Map<String,List<String>> result = new LinkedHashMap<>();
        for(Issue i : iList){
            result.put(i.getIssueKey(),toStringIssue(i));
        }

        return result;
    }
}
