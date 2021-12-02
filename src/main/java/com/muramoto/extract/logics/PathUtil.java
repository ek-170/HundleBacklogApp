package com.muramoto.extract.logics;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import com.nulabinc.backlog4j.BacklogAPIException;

public class PathUtil {

    private static final String EXCEL_DIR_NAME   = "Sheet";
    private static final String CREATED_DIR_NAME = "Created";
    private static final String UPDATED_DIR_NAME = "Updated";
    
    private static Path basePath;
    
    public static Path getBasePath(){
        return basePath;
    }

    public static String getCreatedExcellDirPath(){
        return basePath + File.separator + EXCEL_DIR_NAME + File.separator + CREATED_DIR_NAME + File.separator;
    }

    public static String getUpdatedExcellDirPath(){
      return basePath + File.separator + EXCEL_DIR_NAME + File.separator + UPDATED_DIR_NAME + File.separator;
    }

    static {
        Class cls = new Object(){}.getClass();
        ProtectionDomain pd       = cls.getProtectionDomain();
        CodeSource       cs       = pd.getCodeSource();
        URL              location = cs.getLocation();
        try{
            URI uri = location.toURI();
            basePath = Paths.get(uri).getParent();
        }catch(URISyntaxException e){
            e.printStackTrace();
            throw new BacklogAPIException(e);
        }
    }
}