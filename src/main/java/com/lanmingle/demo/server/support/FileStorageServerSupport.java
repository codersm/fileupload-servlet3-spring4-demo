package com.lanmingle.demo.server.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanmingle.demo.server.FileStorageServer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileStorageServerSupport implements FileStorageServer {

    private static final Map<String, SimpleStoreTarget> FILE_STORAGE = new Hashtable<String, SimpleStoreTarget>();

    @Override
    public String push(InputStream inputStream, String rootPath, String name) throws IOException {
        String resourcesId = generateUUID(); // uuid
        String storePath = new StringBuffer(rootPath).append(resourcesId).toString(); //c://xx/xx/storage/uuid  -> not file extend name
        SimpleStoreTarget storeTarget = new SimpleStoreTarget(resourcesId, storePath, name);
        File storeFile = new File(storePath);
        //if db save transactional --> if error rollback ,file not write
        FileUtils.copyInputStreamToFile(inputStream, storeFile);
        FILE_STORAGE.put(resourcesId, storeTarget);
        return resourcesId;
    }

    @Override
    public SimpleStoreTarget pull(String resourcesId) throws FileNotFoundException {
        if (FILE_STORAGE.containsKey(resourcesId)) {
            SimpleStoreTarget storeTarget = FILE_STORAGE.get(resourcesId);
            if (storeTarget.getStoreFile() == null) {
                File storeFile = new File(storeTarget.getPath());
                if (!storeFile.exists()) {
                    throw new FileNotFoundException("file path not found , path :" + storeTarget.getPath());
                }
                storeTarget.setStoreFile(storeFile);
            }
            return storeTarget;
        }
        return null;
    }

    @Override
    public Set<SimpleStoreTarget> findAllFileStorage() {
        Set<SimpleStoreTarget> storeTargets = new HashSet<SimpleStoreTarget>();
        Set<Map.Entry<String, SimpleStoreTarget>> entries = FILE_STORAGE.entrySet();
        for (Map.Entry<String, SimpleStoreTarget> entry : entries) {
            storeTargets.add(entry.getValue());
        }
        return storeTargets;
    }

    @Override
    public void delete(String resourcesId) {
        if (FILE_STORAGE.containsKey(resourcesId)) {
            SimpleStoreTarget storeTarget = FILE_STORAGE.get(resourcesId);
            if (storeTarget.getStoreFile() != null && storeTarget.getStoreFile().exists()) {
                storeTarget.getStoreFile().delete();
            }
            FILE_STORAGE.remove(resourcesId);
        }
    }

    private String generateUUID() {
        StringBuffer buffer = new StringBuffer(UUID.randomUUID().toString().replaceAll("-", ""));
        return buffer.toString();
    }

    public static class SimpleStoreTarget {

        @JsonProperty("id")
        private String resourcesId;
        @JsonProperty("path")
        private String path;
        @JsonProperty("name")
        private String name;
        @JsonIgnore
        private File storeFile;

        public SimpleStoreTarget(String resourcesId, String path, String name) {
            this.resourcesId = resourcesId;
            this.path = path;
            this.name = name;
        }

        public File getStoreFile() {
            return storeFile;
        }

        public String getResourcesId() {
            return resourcesId;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        private void setStoreFile(File storeFile) {
            this.storeFile = storeFile;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleStoreTarget)) return false;

            SimpleStoreTarget that = (SimpleStoreTarget) o;

            if (!resourcesId.equals(that.resourcesId)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return resourcesId.hashCode();
        }

    }

}
