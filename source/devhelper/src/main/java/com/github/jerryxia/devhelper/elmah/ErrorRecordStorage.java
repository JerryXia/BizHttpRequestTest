/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

/**
 * @author Administrator
 *
 */
public interface ErrorRecordStorage {
    PageInfo<ErrorInfo> queryLimitData(int offset, int limit);
    long size();
    ErrorInfo detail(String id);
    boolean save(ErrorInfo errorInfo);
}
