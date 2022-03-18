package com.muramoto.extract.util;

import com.nulabinc.backlog4j.Milestone;
import java.util.List;

public class MileStoneUtil {
    public static String toStringMilestones(List<Milestone> milestoneList){
        String result = "";
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for(Milestone m : milestoneList){
            sb.append(m.getName());
            if(count == milestoneList.size()){
                result = sb.toString();
                break;
            }
            sb.append(System.lineSeparator());
            count++;
        }
        return result;
    } 
}
