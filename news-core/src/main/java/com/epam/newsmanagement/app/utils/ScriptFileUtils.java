package com.epam.newsmanagement.app.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Script files utils.
 */
public class ScriptFileUtils {
    /**
     * Returns search script directory
     * path.
     * @return search script directory
     * path.
     */
    public String getSearchScriptDirectoryPath() {
        String classesDirectoryPath = ScriptFileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String searchScriptDirectoryPath = classesDirectoryPath +
                "script" + File.separator +
                "sql" + File.separator;
        return searchScriptDirectoryPath;
    }


    /**
     * Returns test search script
     * directory path.
     * @return search test script
     * directory path.
     */
    public String getTestSearchScriptDirectoryPath() {
        String searchScriptDirectoryPath = getSearchScriptDirectoryPath();
        String testSearchScriptDirectoryPath = searchScriptDirectoryPath.replace("classes", "test-classes");
        return testSearchScriptDirectoryPath;
    }


    /**
     * Loads search script parts.
     * @param directoryPath directory where
     * files with search script parts are located.
     * @param fileName script file name.
     * @param logger logger error message is written to.
     * @param errorMessage message to write to
     * log if script file is not found.
     * @return search script part.
     */
    public String getScriptPart(String directoryPath,
                                       String fileName,
                                       Logger logger,
                                       String errorMessage) {
        String scriptPart = "";
        try {
            scriptPart = FileUtils.readFileToString(new File(directoryPath + fileName));
        } catch (IOException e) {
            logger.error(errorMessage, e);
        }
        scriptPart = scriptPart.trim();
        return scriptPart;
    }
}
