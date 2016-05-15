package com.epam.newsmanagement.app.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
        String searchScriptDirectoryPath;
        if (classesDirectoryPath.endsWith("jar"))
            searchScriptDirectoryPath = "jar:file:" + classesDirectoryPath + "!" +
                File.separator + "script" + File.separator + "sql" + File.separator;
        else
            searchScriptDirectoryPath = classesDirectoryPath +
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
        if (directoryPath.startsWith("jar")) {
            URL url;
            try {
                url = new URL(directoryPath + fileName);
            } catch (MalformedURLException e) {
                logger.error(errorMessage, e);
                return "";
            }
            try {
                InputStream inputStream = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String buff;
                while ((buff = bufferedReader.readLine()) != null) {
                    stringBuilder.append(buff);
                    stringBuilder.append("\n");
                }
                scriptPart = stringBuilder.toString();
            } catch (IOException e) {
                logger.error(errorMessage, e);
            }
        }
        else {
            try {
                scriptPart = FileUtils.readFileToString(new File(directoryPath + fileName));
            } catch (IOException e) {
                logger.error(errorMessage, e);
            }
        }
        scriptPart = scriptPart.trim();
        return scriptPart;
    }
}
