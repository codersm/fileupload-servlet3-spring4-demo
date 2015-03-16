package com.lanmingle.demo.server;

import com.lanmingle.demo.server.support.FileStorageServerSupport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface FileStorageServer {

    String push(InputStream inputStream, String rootPath, String name) throws IOException;

    FileStorageServerSupport.SimpleStoreTarget pull(String resourcesId) throws FileNotFoundException;

    Set<FileStorageServerSupport.SimpleStoreTarget> findAllFileStorage();

    void delete(String resourcesId);
}
