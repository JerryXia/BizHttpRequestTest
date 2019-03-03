/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.io.FilePathUtil;
import org.springside.modules.utils.io.FileUtil;

import com.github.jerryxia.devhelper.support.json.RuntimeJsonComponentProviderFactory;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author Administrator
 *
 */
public class ErrorRecordFileStorage implements ErrorRecordStorage {
    private static final Logger log = LoggerFactory.getLogger(ErrorRecordFileStorage.class);

    private final File storageDirectory;

    public ErrorRecordFileStorage(String storageDirectory) throws IOException {
        log.info("ErrorRecordFileStorage.storageDirectory: {}", storageDirectory);
        this.storageDirectory = new File(storageDirectory);
        FileUtil.makesureDirExists(this.storageDirectory);
    }

    @Override
    public PageInfo<ErrorInfo> queryLimitData(int offset, int limit) {
        String names[] = this.storageDirectory.list();
        if (names == null) {
            return null;
        }
        if (offset < 0) {
            offset = 0;
        }
        if (limit < 1) {
            limit = 1;
        }
        int count = 0;
        ArrayList<String> files = new ArrayList<String>(limit);
        for (int i = 0; i < names.length; i++) {
            if (count < limit) {
                if (i >= offset) {
                    files.add(names[i]);
                    count++;
                } else {
                    // name < offset
                }
            } else {
                // count 已满
            }
        }
        int[] rowBounds = { offset, limit };
        Page<ErrorInfo> pagedErrorInfos = new Page<ErrorInfo>(rowBounds, false);
        pagedErrorInfos.setTotal(names.length);
        if (files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                File file = new File(this.storageDirectory, files.get(i));
                String jsonString = null;
                try {
                    jsonString = FileUtil.toString(file);
                } catch (IOException e) {
                    log.error("读取文件失败", e);
                }
                ErrorInfo errorInfo = RuntimeJsonComponentProviderFactory.tryFindImplementation().fromJson(jsonString, ErrorInfo.class);
                pagedErrorInfos.add(errorInfo);
            }
        }
        PageInfo<ErrorInfo> pageInfo = pagedErrorInfos.toPageInfo();
        pagedErrorInfos.close();
        return pageInfo;
    }

    @Override
    public long size() {
        String names[] = this.storageDirectory.list();
        return names.length;
    }

    @Override
    public ErrorInfo detail(String id) {
        File savedFile = new File(FilePathUtil.contact(this.storageDirectory.getPath(), id));
        String jsonString = null;
        try {
            jsonString = FileUtil.toString(savedFile);
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
        ErrorInfo errorInfo = RuntimeJsonComponentProviderFactory.tryFindImplementation().fromJson(jsonString,
                ErrorInfo.class);
        return errorInfo;
    }

    @Override
    public boolean save(ErrorInfo errorInfo) {
        File saveingFile = new File(FilePathUtil.contact(this.storageDirectory.getPath(), errorInfo.getId()));
        String jsonString = RuntimeJsonComponentProviderFactory.tryFindImplementation().toJson(errorInfo);
        try {
            FileUtil.write(jsonString, saveingFile);
            return true;
        } catch (IOException e) {
            log.error("写入文件失败", e);
            return false;
        }
    }
}
