package com.tttare.springDemo.config;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import com.tttare.springDemo.common.template.FreemarkerStaticModels;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
/**
 * @author verywell
 * @date 2018/5/31.
 * 设置FreeMarker配置信息
 */
@Configuration
public class FreeMarkerConfig{


  @Bean
  public ViewResolver viewResolverFtl() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setCache(false);
    resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
    resolver.setRequestContextAttribute("re");
    resolver.setExposeRequestAttributes(true);
    resolver.setExposeSessionAttributes(true);
    resolver.setSuffix(".ftl");
    resolver.setContentType("text/html;charset=UTF-8");
    resolver.setOrder(0);
	resolver.setAttributesMap(freemarkerStaticModels());
    return resolver;
  }

  @Bean
  public ViewResolver viewResolverHtml() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setCache(false);
    resolver.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
    resolver.setRequestContextAttribute("re");
    resolver.setExposeRequestAttributes(true);
    resolver.setExposeSessionAttributes(true);
    resolver.setOrder(1);
    resolver.setSuffix(".html");
    resolver.setContentType("text/html;charset=UTF-8");
    return resolver;
  }
	@Bean
	public FreemarkerStaticModels freemarkerStaticModels() {
		Map<String, String> maps = Maps.newHashMap();
		maps.put("Json", "com.whty.hxx.web.common.json.Json2");
		FreemarkerStaticModels staticModels = new FreemarkerStaticModels(maps);
		return staticModels;
	}

  @Bean
  public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
    FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
    factory.setTemplateLoaderPath("classpath:/templates/");
    factory.setDefaultEncoding("UTF-8");
    factory.setPreferFileSystemAccess(false);
    FreeMarkerConfigurer result = new FreeMarkerConfigurer();
    freemarker.template.Configuration configuration = factory.createConfiguration();
    configuration.setClassicCompatible(true);
    result.setConfiguration(configuration);
    Properties settings = new Properties();
    settings.put("template_update_delay", "0");
    settings.put("default_encoding", "UTF-8");
    settings.put("number_format", "0.######");
    settings.put("classic_compatible", true);
    settings.put("template_exception_handler", "ignore");
    result.setFreemarkerSettings(settings);
    return result;
  }

}