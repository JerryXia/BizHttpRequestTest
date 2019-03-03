/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class ElmahServletProperties {
    private String urlPattern;
    private String errorRecordStorage;
    private String errorRecordFileStoragePath;

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getErrorRecordStorage() {
        return errorRecordStorage;
    }

    public void setErrorRecordStorage(String errorRecordStorage) {
        this.errorRecordStorage = errorRecordStorage;
    }

    public String getErrorRecordFileStoragePath() {
        return errorRecordFileStoragePath;
    }

    public void setErrorRecordFileStoragePath(String errorRecordFileStoragePath) {
        this.errorRecordFileStoragePath = errorRecordFileStoragePath;
    }
}
