package com.oris_sem01.travelplanner.config;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.ServletContext;

import java.io.File;

public class FreemarkerConfig {
    private static Configuration cfg;

    public static synchronized Configuration getConfig(ServletContext context) {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_32);
            try {
                String templatePath = context.getRealPath("/WEB-INF/templates");
                System.out.println("📁 Путь к шаблонам: " + templatePath);

                File templateDir = new File(templatePath);
                if (!templateDir.exists()) {
                    System.err.println("⚠️  Папка шаблонов не найдена: " + templatePath);
                    throw new RuntimeException("Templates directory not found: " + templatePath);
                }

                cfg.setTemplateLoader(new FileTemplateLoader(templateDir));
                cfg.setDefaultEncoding("UTF-8");
                cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
                cfg.setLogTemplateExceptions(false);
                cfg.setWrapUncheckedExceptions(true);
                cfg.setFallbackOnNullLoopVariable(false);
                cfg.setAPIBuiltinEnabled(true);
                System.out.println("✓ Freemarker сконфигурирован успешно");
            } catch (Exception e) {
                System.err.println("❌ Ошибка конфигурации FreeMarker: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return cfg;
    }
}
