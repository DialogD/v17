package com.hgz.v17center;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CenterApplicationTests {
    @Autowired
    private FastFileStorageClient client;


    @Test
    public void uploadFileTest() throws FileNotFoundException {
        File file = new File("F:\\MavenIdea\\v17\\v17-web\\v17-center\\147523.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        StorePath storePath = client.uploadImageAndCrtThumbImage(inputStream, file.length(), "jpg", null);
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getGroup());
        System.out.println(storePath.getPath());
    }

    @Test
    public void deleteFile(){
        client.deleteFile("group1/M00/00/00/CiQICF26wKyAR9rrAADOTov00KY722.jpg");
        System.out.println("delete-->ok");
    }

}
