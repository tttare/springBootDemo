package com.tttare.springDemo.common.template;


import java.util.HashMap;

public class FreemarkerStaticModels extends HashMap<String, Object> {
    /*private static Logger logger = LoggerFactory.getLogger(FreemarkerStaticModels.class);

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
    }*/
}