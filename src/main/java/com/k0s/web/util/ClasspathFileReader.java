package com.k0s.web.util;

import java.io.*;

public class ClasspathFileReader {

    public static InputStream getInputStream(String path) throws IOException {

//        ClassLoader classLoader = ClasspathFileReader.class.getClassLoader();

//        return new FileInputStream(classLoader.getResource(path.substring(1)).getFile());
        return ClasspathFileReader.class.getClassLoader().getResourceAsStream(path);

    }
}

