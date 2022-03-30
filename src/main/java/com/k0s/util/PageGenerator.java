package com.k0s.util;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class PageGenerator {
    private static final String TEMPLATES_DIR = "templates";
    private static final String HTML_DIR = "src/main/resources/templates";


    private static final PageGenerator INSTANCE = new PageGenerator();
    private final Configuration configuration;

    public PageGenerator(){
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.clearTemplateCache();
        configuration.setClassLoaderForTemplateLoading(PageGenerator.class.getClassLoader(), TEMPLATES_DIR);
    }

    public static PageGenerator getInstance(){
        return INSTANCE;
    }


    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();

        try {
            Template template = configuration.getTemplate(filename);
//            Template template = configuration.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return stream.toString();
    }
}
