package com.k0s.util;

import java.io.IOException;
import java.io.InputStream;

public class FileReader {


    public static InputStream getInputStream(String path) throws IOException {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(path.substring(1)); // ?????

        if (inputStream == null){
            throw new IOException("File " + path + " not found");
        }
        return inputStream;
    }
}

