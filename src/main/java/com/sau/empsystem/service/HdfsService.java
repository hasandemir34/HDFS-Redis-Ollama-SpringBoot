package com.sau.empsystem.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

@Service
public class HdfsService {

    @Value("${hdfs.uri:hdfs://localhost:9000}")
    private String hdfsUri;

    @Value("${hdfs.path:/football}")
    private String hdfsPath;

    public String upload(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new IOException("Dosya adı bulunamadı.");
        }

        String fileName = Paths.get(originalName).getFileName().toString();
        Path folder = new Path(hdfsPath);
        Path target = new Path(hdfsPath + "/" + fileName);

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUri);

        try (FileSystem fileSystem = FileSystem.get(URI.create(hdfsUri), configuration)) {
            fileSystem.mkdirs(folder);
            try (var inputStream = file.getInputStream();
                 var outputStream = fileSystem.create(target, true)) {
                inputStream.transferTo(outputStream);
            }
        }

        return fileName;
    }
}
