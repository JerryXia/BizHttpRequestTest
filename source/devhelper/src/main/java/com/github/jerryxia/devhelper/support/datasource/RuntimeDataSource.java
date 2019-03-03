/**
 * 
 */
package com.github.jerryxia.devhelper.support.datasource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.github.jerryxia.devhelper.support.spring.SpringTools;

/**
 * @author guqk
 *
 */
public class RuntimeDataSource {
    private static final Logger log = LoggerFactory.getLogger(RuntimeDataSource.class);

    private static Object target;

    public static Object tryFindDataSource() {
        if (target == null) {
            synchronized (log) {
                if (target == null) {
                    Object dataSourceObject = null;
                    try {
                        dataSourceObject = SpringTools.getBean("dataSource");
                    } catch (BeansException e1) {
                        log.info("can not find bean:dataSource, try to find bean inherit ObjectMapper");
                        try {
                            dataSourceObject = SpringTools.getBean(DataSource.class);
                        } catch (BeansException e2) {
                            log.warn("can not find any bean object inherit javax.sql.DataSource");
                        }
                    }
                    log.info("final dataSourceObject is: {}", dataSourceObject.getClass().toString());
                    target = dataSourceObject;
                }
            }
        }
        return target;
    }
}
