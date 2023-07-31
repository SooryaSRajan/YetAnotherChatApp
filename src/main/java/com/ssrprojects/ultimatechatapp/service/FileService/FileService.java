package com.ssrprojects.ultimatechatapp.service.FileService;

public interface FileService<T> {

    T saveFile(T file);

    T getFile(String id);

    void deleteFile(String id);

}
