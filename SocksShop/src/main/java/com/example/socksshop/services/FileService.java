package com.example.socksshop.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public interface FileService {
    boolean saveToFile(String json);

    String readFromFile();

    boolean cleanFile();

    File getDataFile();

    Path createTempFile(String suffix);
}
