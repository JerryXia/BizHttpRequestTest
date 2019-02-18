/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class ErrorRecordFileStorage implements ErrorRecordStorage {
    private final File storageDirectory;

    public ErrorRecordFileStorage(String storageDirectory) throws IOException {
        this.storageDirectory = new File(storageDirectory);
        if (this.storageDirectory.exists()) {
            if (!this.storageDirectory.isDirectory()) {
                throw new IOException("There is a file exists " + storageDirectory);
            }
        } else {
            this.storageDirectory.mkdirs();
        }
    }

    @Override
    public List<ErrorInfo> queryLimitData(String offset, int limit) {
        String names[] = this.storageDirectory.list();
        if (names == null) {
            return null;
        }
        if (offset == null) {
            offset = "";
        }
        if (limit < 0) {
            limit = 0;
        }
        int count = 0;
        boolean isAfterOffset = offset.length() == 0 ? true : false;
        ArrayList<String> files = new ArrayList<String>(limit);
        for (int i = 0; i < names.length; i++) {
            if (count < limit) {
                String name = names[i];
                if (isAfterOffset) {
                    files.add(name);
                    count++;
                } else {
                    // name < offset
                    isAfterOffset = name.equals(offset);
                }
            } else {
                // count 已满
            }
        }

        ArrayList<ErrorInfo> errorInfos = new ArrayList<ErrorInfo>(files.size());
        if (files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                File file = new File(this.storageDirectory, files.get(i));
                // System.out.println(file.getPath());
                
            }
        }
        return null;
    }

    @Override
    public long size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ErrorInfo detail(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(ErrorInfo errorInfo) {
        // TODO Auto-generated method stub
        return false;
    }
}
