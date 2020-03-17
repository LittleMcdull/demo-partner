//package com.suixingpay.model.swagger;
//
//import com.google.common.base.Predicate;
//import com.google.common.base.Predicates;
//import com.suixingpay.model.swagger.properties.SwaggerProperties;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestMethod;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.builders.ResponseMessageBuilder;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.ResponseMessage;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
//import static org.springframework.http.HttpStatus.OK;
//
//@Configuration
//@EnableSwagger2
//public class Swagger2 implements BeanFactoryAware {
//
//    @Autowired
//    private SwaggerProperties swaggerProperties;
//
//    private BeanFactory beanFactory;
//
//    /**
//     * @return
//     */
//    @PostConstruct
//    public List<Docket> createRestApi() {
//        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
//        // 没有分组
//        if (swaggerProperties.getDocket().isEmpty()) {
//            configurableBeanFactory.registerSingleton("defaultDocket", defaultDocket());
//            return null;
//        }
//
//        // 分组创建
//        List<Docket> docketList = new LinkedList<>();
//        for (String groupName : swaggerProperties.getDocket().keySet()) {
//            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(groupName);
//            Docket docket = buildDocket(groupName, docketInfo);
//            configurableBeanFactory.registerSingleton(groupName, docket);
//            docketList.add(docket);
//        }
//        return docketList;
//    }
//
//    /**
//     * @return
//     */
//    private Docket defaultDocket() {
//        ApiInfo apiInfo = new ApiInfoBuilder().title(swaggerProperties.getTitle())
//                .description(swaggerProperties.getDescription()).version(swaggerProperties.getVersion())
//                .license(swaggerProperties.getLicense()).licenseUrl(swaggerProperties.getLicenseUrl())
//                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
//                        swaggerProperties.getContact().getEmail()))
//                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).build();
//
//        // base-path处理
//        // 当没有配置任何path的时候，解析/**
//        if (swaggerProperties.getBasePath().isEmpty()) {
//            swaggerProperties.getBasePath().add("/**");
//        }
//        List<Predicate<String>> basePath = new ArrayList<>();
//        for (String path : swaggerProperties.getBasePath()) {
//            basePath.add(PathSelectors.ant(path));
//        }
//
//        // exclude-path处理
//        List<Predicate<String>> excludePath = new ArrayList<>();
//        for (String path : swaggerProperties.getExcludePath()) {
//            excludePath.add(PathSelectors.ant(path));
//        }
//        Set<String> produces = swaggerProperties.getProduces();
//        if (null == produces) {
//            produces = new LinkedHashSet<>();
//        }
//        if (produces.isEmpty()) {
//            produces.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        }
//        List<ResponseMessage> responseMessages = getResponseMessages();
//        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).host(swaggerProperties.getHost())
//                .apiInfo(apiInfo)
//                .useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessages)
//                .globalResponseMessage(RequestMethod.POST, responseMessages)
//                .globalResponseMessage(RequestMethod.PUT, responseMessages)
//                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
//                .produces(produces)
//                .select();
//        String basePackage = swaggerProperties.getBasePackage();
//        if (null != basePackage && basePackage.length() > 0) {
//            builder.apis(RequestHandlerSelectors.basePackage(basePackage));
//        }
//
//        Docket docket = builder.paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build();
//        return docket;
//    }
//
//    /**
//     * @param groupName
//     * @param docketInfo
//     * @return
//     */
//    private Docket buildDocket(String groupName, SwaggerProperties.DocketInfo docketInfo) {
//        ApiInfo apiInfo = new ApiInfoBuilder()
//                .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
//                .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription() : docketInfo.getDescription())
//                .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
//                .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
//                .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
//                .contact(new Contact(
//                        docketInfo.getContact().getName().isEmpty() ? swaggerProperties.getContact().getName() : docketInfo.getContact().getName(),
//                        docketInfo.getContact().getUrl().isEmpty() ? swaggerProperties.getContact().getUrl() : docketInfo.getContact().getUrl(),
//                        docketInfo.getContact().getEmail().isEmpty() ? swaggerProperties.getContact().getEmail() : docketInfo.getContact().getEmail()))
//                .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
//                .build();
//
//        // base-path处理
//        // 当没有配置任何path的时候，解析/**
//        if (docketInfo.getBasePath().isEmpty()) {
//            docketInfo.getBasePath().add("/**");
//        }
//        List<Predicate<String>> basePath = new ArrayList<>();
//        for (String path : docketInfo.getBasePath()) {
//            basePath.add(PathSelectors.ant(path));
//        }
//
//        // exclude-path处理
//        List<Predicate<String>> excludePath = new ArrayList<>();
//        for (String path : docketInfo.getExcludePath()) {
//            excludePath.add(PathSelectors.ant(path));
//        }
//        Set<String> produces = swaggerProperties.getProduces();
//        if (null == produces) {
//            produces = docketInfo.getProduces();
//        }
//        if (null == produces) {
//            produces = new LinkedHashSet<>();
//        }
//        List<ResponseMessage> responseMessages = getResponseMessages();
//        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).host(swaggerProperties.getHost())
//                .apiInfo(apiInfo)
//                .useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessages)
//                .globalResponseMessage(RequestMethod.POST, responseMessages)
//                .globalResponseMessage(RequestMethod.PUT, responseMessages)
//                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessages).produces(produces).groupName(groupName)
//                .select();
//        String basePackage = docketInfo.getBasePackage();
//        if (null != basePackage && basePackage.length() > 0) {
//            builder.apis(RequestHandlerSelectors.basePackage(basePackage));
//        }
//        Docket docket = builder.paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build();
//        return docket;
//
//    }
//
//    /**
//     * @return
//     */
//    private List<ResponseMessage> getResponseMessages() {
//        List<ResponseMessage> responseMessages = new ArrayList<>();
//        responseMessages.add(new ResponseMessageBuilder().code(OK.value()).message(OK.getReasonPhrase())
//                .responseModel(null).build());
//        return responseMessages;
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
//    }
//
////    /**
////     * 通过 createRestApi函数来构建一个DocketBean
////     * 函数名,可以随意命名,喜欢什么命名就什么命名
////     */
////    @Bean
////    public Docket createRestApi() {
////        return new Docket(DocumentationType.SWAGGER_2)
////                //调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
////                .apiInfo(apiInfo())
////                .select()
////                //控制暴露出去的路径下的实例
////                //如果某个接口不想暴露,可以使用以下注解
////                //@ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
////                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
////                .paths(PathSelectors.any())
////                .build();
////    }
////
////    /**
////     * 构建 api文档的详细信息函数
////     */
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder()
////                //页面标题
////                .title(swaggerProperties.getTitle())
////                //条款地址
////                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
////                .contact(new Contact(
////                        swaggerProperties.getContact().getName(),
////                        swaggerProperties.getContact().getUrl(),
////                        swaggerProperties.getContact().getEmail()))
////                .version(swaggerProperties.getVersion())
////                //描述
////                .description(swaggerProperties.getDescription())
////                .build();
////    }
//}