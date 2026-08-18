package org.hope.mcpservergenerate.autoconfigure;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.hope.mcpservergenerate.context.HttpFileTreeContext;
import org.hope.mcpservergenerate.context.HttpToolDefinitionContext;
import org.hope.mcpservergenerate.controller.BaseController;
import org.hope.mcpservergenerate.controller.FileController;
import org.hope.mcpservergenerate.controller.GenerateController;
import org.hope.mcpservergenerate.converter.impl.HttpToolDefinitionConverter;
import org.hope.mcpservergenerate.converter.impl.TsHttpToolTemplateModelConverter;
import org.hope.mcpservergenerate.scanner.SpringToolRegistration;


import org.hope.mcpservergenerate.scanner.ToolScanner;


import org.hope.mcpservergenerate.service.impl.FileServiceImpl;
import org.hope.mcpservergenerate.service.impl.FreeMarkerService;
import org.hope.mcpservergenerate.service.impl.GenerateServiceImpl;

import org.hope.mcpservergenerate.service.impl.TreeServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;

/**
 * @author 关岁安
 * @since 2026/7/27
 * AutoConfiguration告诉 Spring Boot “我是一个 Starter 的自动配置类”
 * EnableConfigurationProperties将被寄宿者写在 application.yml 里的配置项，自动注入并绑定到一个 Java 对象中
 * ConditionalOnWebApplication用来判断是一个web项目才能使用这个依赖
 * ConditionalOnProperty根据配置文件中的某个特定参数，决定是否启动寄宿者。
 *
 * 功能：
 * 在被寄宿者启动时，把寄宿者需要的所有 Bean 注册到同一个 Spring 容器中；它不写扫描逻辑本身。
 */

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//写了这个EnableConfigurationProperties注解 可以在controller直接使用这个对象的数据
@EnableConfigurationProperties(McpGeneratorProperties.class)
@ConditionalOnProperty(
        prefix = "mcp.generator",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class McpGeneratorAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public HttpToolDefinitionContext httpToolDefinitionContext() {
        return new HttpToolDefinitionContext();
    }




    @Bean
    @ConditionalOnMissingBean
    public BaseController baseController(
            HttpToolDefinitionContext httpToolDefinitionContext,
            HttpFileTreeContext httpFileTreeContext
    ) {
        return new BaseController(httpToolDefinitionContext,httpFileTreeContext);
    }



    @Bean
    @ConditionalOnMissingBean
    public SpringToolRegistration mcpToolPostProcessor(
            HttpToolDefinitionContext httpToolDefinitionContext
    ){
        return new SpringToolRegistration(
                new ToolScanner(),
                new HttpToolDefinitionConverter(),
                httpToolDefinitionContext);
    }


    @Bean
    @ConditionalOnMissingBean
    public Configuration freemarker(){
        Configuration configuration = new Configuration(
                Configuration.VERSION_2_3_34
        );

        configuration.setClassLoaderForTemplateLoading(
                getClass().getClassLoader(),
                "/templates"
        );

        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(
                TemplateExceptionHandler.RETHROW_HANDLER
        );
        configuration.setInterpolationSyntax(
                Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX
        );


        configuration.setTagSyntax(
                Configuration.SQUARE_BRACKET_TAG_SYNTAX
        );

        return configuration;
    }

    @Bean
    @ConditionalOnMissingBean
    public FreeMarkerService freeMarkerService(Configuration freemarker){
        return new FreeMarkerService(freemarker);
    }

    @Bean
    @ConditionalOnMissingBean
    public GenerateServiceImpl generateService(Configuration freemarker,
                                               HttpToolDefinitionContext httpToolDefinitionContext,
                                               HttpFileTreeContext httpFileContext,
                                               TsHttpToolTemplateModelConverter modelConverter,
                                               TreeServiceImpl treeService,
                                               FreeMarkerService freeMarkerService){
        return new GenerateServiceImpl(freemarker, httpToolDefinitionContext, httpFileContext,modelConverter,treeService,freeMarkerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public GenerateController generateController(GenerateServiceImpl generateService){
        return new GenerateController(generateService);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpFileTreeContext httpFileTreeContext(){
        return new HttpFileTreeContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileServiceImpl fileService(HttpFileTreeContext httpFileTreeContext,TreeServiceImpl treeService,TsHttpToolTemplateModelConverter modelConverter,HttpToolDefinitionContext httpToolDefinitionContext){
        return new FileServiceImpl(httpFileTreeContext,treeService,modelConverter,httpToolDefinitionContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public FileController fileController(FileServiceImpl fileService){
        return new FileController(fileService);
    }


    @Bean
    @ConditionalOnMissingBean
    public TsHttpToolTemplateModelConverter tsHttpToolTemplateModelConverter(){return new TsHttpToolTemplateModelConverter();}

    @Bean
    @ConditionalOnMissingBean
    public TreeServiceImpl treeService(HttpFileTreeContext treeContext){
        return new TreeServiceImpl(treeContext);
    }
}
