package com.tttare.springDemo.common.template;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FreemarkerStaticModels extends HashMap<String, Object> {
    private static Logger logger = LoggerFactory.getLogger(FreemarkerStaticModels.class);

    public FreemarkerStaticModels(Map<String, String> classMap) {
        for (String key : classMap.keySet()) {
            put(key, getModel(classMap.get(key)));
        }
    }

    private TemplateHashModel getModel(String packageName) {
        BeansWrapper wrapper = new BeansWrapperBuilder(new Version("2.3.23")).build();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        TemplateHashModel fileStatics;
        try {
            fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (TemplateModelException e) {
            logger.error(e.toString());
        }
        return null;
    }
}