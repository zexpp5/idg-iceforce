package com.chaoxiang.imrestful.bean;

import java.io.File;

/**
 * @auth lwj
 * @date 2016-01-04
 * @desc
 */
public class FileInput {
    public String key;
    public String filename;
    public File file;

    public FileInput(String name, String filename, File file) {
        this.key = name;
        this.filename = filename;
        this.file = file;
    }
}
