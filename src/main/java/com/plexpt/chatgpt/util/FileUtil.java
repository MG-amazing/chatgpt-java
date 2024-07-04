package com.plexpt.chatgpt.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    
    /**
     * 将 MultipartFile 转换为 File
     * 
     * @param multipartFile 要转换的 MultipartFile 对象
     * @return 转换后的 File 对象
     * @throws IOException 如果发生 I/O 错误
     */
    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        // 获取临时文件目录
        File tempFile = File.createTempFile("temp", multipartFile.getOriginalFilename());
        
        // 将 MultipartFile 转换为 File
        multipartFile.transferTo(tempFile);
        
        // 删除临时文件，程序退出时删除
        tempFile.deleteOnExit();
        
        return tempFile;
    }
}
