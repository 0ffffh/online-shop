package com.k0s.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileReader {

    public static InputStream getInputStream(String path) throws IOException {
        URL url = FileReader.class.getClassLoader().getResource(path.substring(1));
        File file = new File(url.getPath());
        if(file.isDirectory()){
            return null;
        }
        return FileReader.class.getClassLoader().getResourceAsStream(path.substring(1));
    }
}

