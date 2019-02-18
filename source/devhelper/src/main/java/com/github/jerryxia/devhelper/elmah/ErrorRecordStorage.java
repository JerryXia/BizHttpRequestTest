/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

import java.util.List;

/**
 * @author Administrator
 *
 */
public interface ErrorRecordStorage {
    List<ErrorInfo> queryLimitData(String offset, int limit);
    long size();
    ErrorInfo detail(String id);
    boolean save(ErrorInfo errorInfo);
}
