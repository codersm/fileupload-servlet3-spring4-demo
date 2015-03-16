package com.lanmingle.demo.controller.file;

import com.lanmingle.demo.config.WebMvcDispatcherServletInitializer;
import com.lanmingle.demo.server.FileStorageServer;
import com.lanmingle.demo.server.support.FileStorageServerSupport;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Set;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequestMapping("/file")
public class FileController {

    private static final String WEB_ROOT_REAL_PATH = "/";
    private static final String FILE_STORE_LOCATION = "storage";

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private FileStorageServer fileStorageServer;

    @RequestMapping(value = "/push", method = {RequestMethod.POST})
    public String push(@RequestParam("file") MultipartFile file) {
        try {
            fileStorageServer.push(file.getInputStream(), getStoreLocation(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/pull/{resourcesId}", method = {RequestMethod.GET})
    public ResponseEntity<byte[]> pull(@PathVariable("resourcesId") String resourcesId) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            FileStorageServerSupport.SimpleStoreTarget storeTarget = fileStorageServer.pull(resourcesId);
            byte[] storeFileBytes = FileUtils.readFileToByteArray(storeTarget.getStoreFile());
            String downloadName = URLEncoder.encode(storeTarget.getName(), WebMvcDispatcherServletInitializer.CHARACTER_ENCODING_UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", downloadName);
            responseEntity = new ResponseEntity<byte[]>(storeFileBytes, headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

    @RequestMapping(value = "/delete/{resourcesId}", method = {RequestMethod.GET})
    public String delete(@PathVariable("resourcesId") String resourcesId) {
        fileStorageServer.delete(resourcesId);
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/findAllFileStorage", method = {RequestMethod.GET})
    public Set<FileStorageServerSupport.SimpleStoreTarget> findAllFileStorage() {
        return fileStorageServer.findAllFileStorage();
    }

    private String getStoreLocation() {
        StringBuffer buffer = new StringBuffer(servletContext.getRealPath(WEB_ROOT_REAL_PATH)); //eg : c://xx/xx/
        buffer.append(File.separator);//eg : c://xx/xx/
        buffer.append(FILE_STORE_LOCATION); //eg : c://xx/xx/storage
        buffer.append(File.separator);//eg : c://xx/xx/storage/
        return buffer.toString();
    }

}
