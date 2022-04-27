package com.k0s.web.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Map;

public class PageGenerator {
    private static final String TEMPLATES_DIR = "templates";

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

    @SneakyThrows
    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();

            Template template = configuration.getTemplate(filename);
            template.process(data, stream);

        return stream.toString();
    }
}
