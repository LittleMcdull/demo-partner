package com.suixingpay.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/05 15:45
 * @version: V1.0
 */
@SpringBootApplication(scanBasePackages = "com.suixingpay")
@EnableSwagger2
//@ComponentScan("com.suixingpay.conf")
public class ModelApplication {

    protected final static Logger logger = LoggerFactory.getLogger(ModelApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(ModelApplication.class);
            app.setBannerMode(Banner.Mode.OFF);
            ApplicationContext context = app.run(args);
            String[] activeProfiles = context.getEnvironment().getActiveProfiles();
//            logger.info(Content.print());
            logger.info("ActiveProfiles = " + StringUtils.arrayToCommaDelimitedString(activeProfiles));
        } catch (Exception e) {// 一定要加此try catch, 方便解决问题
            // 打印启动失败的错误信息
            logger.error("启动失败：", e);
        }
    }
}
