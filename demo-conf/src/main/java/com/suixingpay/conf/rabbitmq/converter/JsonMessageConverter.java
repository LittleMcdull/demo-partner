package com.suixingpay.conf.rabbitmq.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * MQ是解耦工具，消息经常会跨系统投递，在这样的场景下，使用JSON是最好的。
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年3月7日 上午8:48:28
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年3月7日 上午8:48:28
 */
@Slf4j
@Setter
@Component
public class JsonMessageConverter extends AbstractMessageConverter {

    private static final SimpleMessageConverter SIMPLE_MESSAGE_CONVERTER = new SimpleMessageConverter();

    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();

    static {
        // 忽略大小写
        jsonObjectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个JsonMappingException异常。这个特性一般式所有其他处理方法对未知属性处理都无效后才被尝试，属性保留未处理状态。默认情况下，该设置是被打开的。
        // 但在这里需要将它关闭
        jsonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final JavaType javaType;

    private boolean autoGetAmqpMessage = false;

    public JsonMessageConverter(Type type) {
        super();
        JavaType _javaType = null;
        if (null != type) {
            _javaType = jsonObjectMapper.getTypeFactory().constructType(type);
        }
        if (null == _javaType) {
            throw new IllegalArgumentException(" type is null");
        }
        this.javaType = _javaType;
    }

    public JsonMessageConverter() {
        super();
        this.javaType = null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        String contentType = null;
        if (properties != null) {
            contentType = properties.getContentType();
        }
        if (contentType != null) {
            if (contentType.equals(MessageProperties.CONTENT_TYPE_JSON)) {
                String json = new String(message.getBody(), StandardCharsets.UTF_8);
                if (log.isTraceEnabled()) {
                    log.trace("the json fromMessage:{}", json);
                }
                try {
                    content = jsonToObject(json);
                } catch (Exception e) {
                    // 为了方便补偿处理，将消息打印出来
                    log.error("rabbitmq message=" + json);
                    throw new MessageConversionException("failed to convert serialized Message content", e);
                }
            }
        }
        if (content == null) {
            // 为了兼容老框架数据类型问题
            content = SIMPLE_MESSAGE_CONVERTER.fromMessage(message);
        }
        // 无法通过ContentType 反序列化时，返回原始数据
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }

    /**
     * 获取JSON及字符串类型的消息内容
     *
     * @param message
     * @return
     */
    public static String getMessageContent(Message message) {
        String content = "不是字符串类型消息";
        MessageProperties properties = message.getMessageProperties();
        String contentType = null;
        if (properties != null) {
            contentType = properties.getContentType();
        }
        if (contentType != null) {
            if (contentType.equals(MessageProperties.CONTENT_TYPE_JSON)) {
                content = new String(message.getBody(), StandardCharsets.UTF_8);
            } else if (contentType.startsWith("text")) {
                String encoding = properties.getContentEncoding();
                if (encoding == null) {
                    encoding = StandardCharsets.UTF_8.name();
                }
                try {
                    content = new String(message.getBody(), encoding);
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return content;
    }

    /**
     * 将JSON字符串转化为 指定类型
     *
     * @param json json
     * @return Object 结果
     */
    public Object jsonToObject(String json) throws Exception {
        Object object = null;
        if (json != null && json.trim().length() > 0) {
            if (null == javaType) {
                // @{see SimpleMessageProducer.returnedMessage} 无法获取javaType，只能直接返回JSON字符串
                return json;
            }
            object = jsonObjectMapper.readValue(json, javaType);
        }
        return object;
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties)
            throws MessageConversionException {
        byte[] bytes = null;
        if (object instanceof byte[]) {
            bytes = (byte[]) object;
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_BYTES);
        } else if (object instanceof String) {
            bytes = ((String) object).getBytes(StandardCharsets.UTF_8);

            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        } else {
            String contentType = messageProperties.getContentType();
            // 如果外部没有改变contentType，默认使用JSON
            if (null == contentType || MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
                    || MessageProperties.DEFAULT_CONTENT_TYPE.equals(contentType)) {
                try {
                    String json = jsonObjectMapper.writeValueAsString(object);
                    bytes = json.getBytes(StandardCharsets.UTF_8);
                    messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                    //messageProperties.setHeader();
                } catch (Exception e) {
                    throw new MessageConversionException("failed to convert to serialized Message content", e);
                }
            } else {
                // 为了兼容老框架数据类型问题
                return SIMPLE_MESSAGE_CONVERTER.toMessage(object, messageProperties);
            }
        }
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);

    }

}