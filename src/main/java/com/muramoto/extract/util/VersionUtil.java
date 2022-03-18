package com.muramoto.extract.util;

import com.nulabinc.backlog4j.Version;
import java.util.List;

public class VersionUtil {
    public static String toStringVersions(List<Version> versionList){
        String result = "";
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for(Version v : versionList){
            sb.append(v.getName());
            if(count == versionList.size()){
                result = sb.toString();
                break;
            }
            sb.append(System.lineSeparator());
            count++;
        }
        return result;
    }
}
