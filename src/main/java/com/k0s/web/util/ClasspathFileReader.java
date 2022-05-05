package com.k0s.web.util;

import java.io.*;

public class ClasspathFileReader {

    public static InputStream getInputStream(String path) throws IOException {
        return ClasspathFileReader.class.getClassLoader().getResourceAsStream(path);
    }
}

