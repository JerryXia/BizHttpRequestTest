/**
 * 
 */
package com.guqiankun.ssmtemplate.infrastructure.elmah;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jerryxia.devhelper.elmah.ErrorInfo;
import com.github.jerryxia.devhelper.elmah.ErrorRecordStorage;
import com.github.jerryxia.devhelper.elmah.PageInfo;

/**
 * @author Administrator
 *
 */
@Component
public class ErrorRecordMysqlStorage implements ErrorRecordStorage {
    private static final Logger log = LoggerFactory.getLogger(ErrorRecordMysqlStorage.class);

    public ErrorRecordMysqlStorage() {
        log.info("ErrorRecordMysqlStorage inited");
    }

    @Override
    public ErrorInfo detail(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageInfo<ErrorInfo> queryLimitData(int offset, int limit) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(ErrorInfo errorInfo) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long size() {
        return 0;
    }

}
