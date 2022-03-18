package com.muramoto.extract.backlog;

import com.nulabinc.backlog4j.api.option.GetIssuesParams;
import com.nulabinc.backlog4j.api.option.GetIssuesParams.SortKey;
import com.muramoto.extract.config.ReadProps;
import com.nulabinc.backlog4j.BacklogAPIException;
import java.util.List;
import java.util.Map;


public class GenGetIssuesParams{
  private static final String ASSIGNEEIDS    = "assigneeIds";
  private static final String CATEGORYIDS    = "categoryIds";
  private static final String CREATEDUSERIDS = "createdUserIds";
  private static final String ISSUETYPEIDS   = "issueTypeIds";
  private static final String MILESTONEIDS   = "milestoneIds";
  private static final String PROJECTID		 = "projectId";
  private static final String STATUSIDS      = "statusIds";
  private static final String VERSIONIDS     = "versionIds";

  
    public static GetIssuesParams generate(String queryPropFilePath, long offset){
        Map<String, List<String>> queryParamsMap = ReadProps.getAsList(queryPropFilePath);
        
        GetIssuesParams result = new GetIssuesParams(queryParamsMap.get(PROJECTID));
        result = setParams(result, queryParamsMap, offset);

        return result;
    }

    private static GetIssuesParams setParams(GetIssuesParams qParams, Map<String, List<String>> queryParamsMap, long offset){
		setParamsIfNotNull(ASSIGNEEIDS    ,qParams ,queryParamsMap);
		setParamsIfNotNull(CATEGORYIDS    ,qParams ,queryParamsMap);
		setParamsIfNotNull(CREATEDUSERIDS ,qParams ,queryParamsMap);
		setParamsIfNotNull(ISSUETYPEIDS   ,qParams ,queryParamsMap);
		setParamsIfNotNull(MILESTONEIDS   ,qParams ,queryParamsMap);
		setParamsIfNotNull(STATUSIDS      ,qParams ,queryParamsMap);
		setParamsIfNotNull(VERSIONIDS     ,qParams ,queryParamsMap);

        return qParams.offset(offset).count(100).sort(SortKey.Created);
    }

	private static GetIssuesParams setParamsIfNotNull(String paramName, GetIssuesParams qParams, Map<String,List<String>> queryParamsMap){
		try{
			if(queryParamsMap.get(paramName).get(0).equals("")){
				return qParams;
			}
		}catch(NullPointerException e){
			throw new BacklogAPIException("クエリパラメータの値が不正です。正しいパラメータは '" + paramName + "'です。");
		}
		switch(paramName){
			case ASSIGNEEIDS:
				return qParams.assigneeIds(queryParamsMap.get(paramName));
			case CATEGORYIDS:
				return qParams.categoryIds(queryParamsMap.get(paramName));
			case CREATEDUSERIDS:
				return qParams.createdUserIds(queryParamsMap.get(paramName));
			case ISSUETYPEIDS:
				return qParams.issueTypeIds(queryParamsMap.get(paramName));
			case MILESTONEIDS:
				return qParams.milestoneIds(queryParamsMap.get(paramName));
			case STATUSIDS:
				return qParams.statusIds(queryParamsMap.get(paramName));	
			case VERSIONIDS:
				return qParams.versionIds(queryParamsMap.get(paramName));	
			default:
				return qParams;
		}
	}
}