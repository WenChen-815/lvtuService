package com.zhoujh.lvtu.post.controller;

import com.zhoujh.lvtu.post.model.Post;
import com.zhoujh.lvtu.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    private final String uploadDirectory = "D:/lvtu/post/";

    @PostMapping("/upload")
    public Post uploadPost(
            @RequestPart("post")Post post,
            @RequestParam("identifiers") List<String> identifiers,
            @RequestParam("sequenceNumbers") List<Integer> sequenceNumbers,
            @RequestParam("totalChunks") List<Integer> totalChunks,
            @RequestParam("images") List<MultipartFile> files
            ){
        //如果图片存在
        if (post.getPictureCount()>0){
            List<String> fileNames = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String identifier = identifiers.get(i);
                int sequenceNumber = sequenceNumbers.get(i);
                int totalChunk = totalChunks.get(i);
                try {
                    // 创建目录（如果不存在）
                    File uploadDir = new File(uploadDirectory);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    // 构建文件名（使用标识符和序号）
                    String filename = file.getOriginalFilename();
                    // 截取文件扩展名
                    String extension = "";
                    int lastDotIndex = filename.lastIndexOf(".");
                    if (lastDotIndex > 0) {
                        extension = filename.substring(lastDotIndex + 1);
                    }
                    // 标识（uuid）+序号+用户id+扩展名
                    String fileName = identifier + "_" + sequenceNumber + "_" + post.getUserId()+"."+extension;
                    // 保存文件分片
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(uploadDirectory + fileName);
                    Files.write(path, bytes);
                    // 检查是否所有分片都已上传
                    if (sequenceNumber == totalChunk - 1) {
                        // 如果所有分片都已上传，则重组文件
                        String combinedFileName = identifier + "_" + post.getUserId()+"."+extension;
                        fileNames.add("/lvtu/post/"+combinedFileName);
                        File combinedFile = new File(uploadDirectory + combinedFileName);
                        for (int j = 0; j < totalChunk; j++) {
                            File partFile = new File(uploadDirectory + identifier + "_" + j + "_" + post.getUserId()+"."+extension);
                            FileOutputStream fos = new FileOutputStream(combinedFile, true);
                            FileInputStream fis = new FileInputStream(partFile);
                            FileCopyUtils.copy(fis, fos);
                            fis.close();
                            fos.close();
                            partFile.delete(); // 清理分片文件
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            post.setPicturePath(fileNames);
        }
        return postService.createPost(post);
    }
}
