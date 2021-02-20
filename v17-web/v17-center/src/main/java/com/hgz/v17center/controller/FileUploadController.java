package com.hgz.v17center.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hgz.commons.base.ResultBean;
import com.hgz.v17center.pojo.WangEditorResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @Author: DJA
 * @Date: 2019/11/1 10:05
 */
@RestController
@RequestMapping("file")
@Slf4j
public class FileUploadController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${image.server}")
    private String imageServer;

    //上传图片
    @RequestMapping(value = "upload")
    public ResultBean upload(@RequestParam("file_upload") MultipartFile file){
        log.info("fileupload...");
        //获取文件后缀
        String filename = file.getOriginalFilename();
        String endName = filename.substring(filename.lastIndexOf(".") + 1);
        StorePath storePath = null;
        try {
            storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(), endName, null);
            StringBuilder builder = new StringBuilder(imageServer).append(storePath.getFullPath());
            return new ResultBean("200",builder);
        } catch (IOException e) {
            //TODO 结合日志框架写入日志信息
            e.printStackTrace();
            return new ResultBean("500","服务器繁忙，请稍后再试...");
        }

    }

    @PostMapping("batchUpload")
    public WangEditorResultBean uploads(@RequestParam("file_uploads") MultipartFile[] files){
        System.out.println("batchUploads....");
        String[] data = new String[files.length];  //字符数组的长度为文件的个数
        try {
            for(int i=0;i<files.length;i++){
                String filename = files[i].getOriginalFilename();
                String endName = filename.substring(filename.lastIndexOf(".") + 1);
                StorePath storePath = client.uploadImageAndCrtThumbImage(files[i].getInputStream(), files[i].getSize(), endName, null);
                StringBuilder builder = new StringBuilder(imageServer).append(storePath.getFullPath());
                data[i] = builder.toString();

            }
        }catch (IOException e) {
            //TODO 结合日志框架写入日志信息
            e.printStackTrace();
            return new WangEditorResultBean("1",null);
        }
        return new WangEditorResultBean("0",data);   //不能放在循环内，try--catch最好在循环外
    }

}
