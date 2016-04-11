package com.epam.app.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Script files utils test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
public class ScriptFileUtilsTest {
    private static final Logger testLogger = Logger.getLogger(ScriptFileUtils.class);
    private static final String TEST_ERROR_MESSAGE = "test";
    private static final String TEST_AUTHOR_AND_TAGS_SEARCH_SCRIPT_FILE_NAME
            = "author-and-tags-test-search-query.sql";

    @Autowired
    private ScriptFileUtils scriptFileUtils;


    @Test
    public void validSearchScriptDirectoryAndFileName() {
        String searchScriptDirectory = scriptFileUtils.getTestSearchScriptDirectoryPath();
        String scriptPart = scriptFileUtils.getScriptPart(searchScriptDirectory,
                TEST_AUTHOR_AND_TAGS_SEARCH_SCRIPT_FILE_NAME, null, null);

        assertNotEquals("", scriptPart);
    }

    @Test
    public void searchScriptFileDirectoryIsValid() {
        String searchScriptFileDirectory = scriptFileUtils.getSearchScriptDirectoryPath();

        assertTrue(searchScriptFileDirectory.endsWith("/classes/script/sql/"));
    }

    @Test
    public void testSearchScriptFileDirectoryIsValid() {
        String testSearchScriptFileDirectory = scriptFileUtils.getTestSearchScriptDirectoryPath();

        assertTrue(testSearchScriptFileDirectory.endsWith("/test-classes/script/sql/"));
    }

    @Test
    public void invalidSearchScriptDirectory() {
        String invalidSearchScriptDirectory = "test";
        String scriptPart = scriptFileUtils.getScriptPart(invalidSearchScriptDirectory,
                TEST_AUTHOR_AND_TAGS_SEARCH_SCRIPT_FILE_NAME, testLogger, TEST_ERROR_MESSAGE);

        assertEquals("", scriptPart);
    }

    @Test
    public void invalidSearchScriptFileName() {
        String searchScriptDirectory = scriptFileUtils.getTestSearchScriptDirectoryPath();
        String invalidSearchScriptFileName = "test";
        String scriptPart = scriptFileUtils.getScriptPart(searchScriptDirectory,
                invalidSearchScriptFileName, testLogger, TEST_ERROR_MESSAGE);

        assertEquals("", scriptPart);
    }
}
