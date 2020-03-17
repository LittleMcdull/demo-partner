/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/04
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.mq;

import com.suixingpay.core.bean.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/04 18:54
 * @version: V1.0
 */
@SpringBootApplication
public class MqApplication {

    protected final static Logger logger = LoggerFactory.getLogger(MqApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(MqApplication.class);
            app.setBannerMode(Banner.Mode.OFF);
            ApplicationContext context = app.run(args);
            String[] activeProfiles = context.getEnvironment().getActiveProfiles();
            logger.info(Content.print());
            logger.info("ActiveProfiles = " + StringUtils.arrayToCommaDelimitedString(activeProfiles));
        } catch (Exception e) {// 一定要加此try catch, 方便解决问题
            // 打印启动失败的错误信息
            logger.error("启动失败：",e);
        }
    }
}
