/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

import com.github.pagehelper.PageInfo;

/**
 * @author Administrator
 *
 */
public class ErrorRecordNoOpStorage implements ErrorRecordStorage {
    @Override
    public PageInfo<ErrorInfo> queryLimitData(int offset, int limit) {
        PageInfo<ErrorInfo> result = new PageInfo<ErrorInfo>();
        return result;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public ErrorInfo detail(String id) {
        return null;
    }

    @Override
    public boolean save(ErrorInfo errorInfo) {
        return false;
    }
}
