/**
 *
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class ElmahServletProperties {
    public static final String DEFAULT_URL_PATTERN = "/admin/elmah/*";

    private boolean enabled;
    private String urlPattern;
    private String errorRecordStorage;
    private String errorRecordFileStoragePath;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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
