/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:48 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.bean.response.ResponseBean;
import com.suixingpay.core.enums.CommonExceptionEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 11:48 AM
 */
public class CheckUtil {

    private static Validator validator;

    @Autowired
    private void setValidator(Validator validator){
        CheckUtil.validator = validator;
    }

    private CheckUtil() {
    }

    /**
     * 验证某一个对象
     *
     * @param obj
     * @param extra
     * @return
     */
    public static String validateModel(Object obj, String extra) {

        // 用于存储验证后的错误信息
        StringBuilder buffer = new StringBuilder();

        String errorMsg = "";
        // 验证某个对象,，其实也可以只验证其中的某一个属性的
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);

        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation<Object> constraintViolation = iter.next();
            String message = constraintViolation.getMessage();
            Path name = constraintViolation.getPropertyPath();
            if (StringUtils.isEmpty(extra) || !extra.contains("|" + name + "|")) {
                buffer.append(name + message + ",");
            }
        }
        if (StringUtils.isNotBlank(buffer)) {
            errorMsg = buffer.substring(0, buffer.length() - 1);
        }
        return errorMsg;
    }

    /**
     * 校验包含在extra里面的属性
     *
     * @param obj
     * @param extra
     * @return
     */
    public static String validateModelContainsExtra(Object obj, String extra) {// 验证某一个对象
        StringBuilder buffer = new StringBuilder();// 用于存储验证后的错误信息

        String errorMsg = "";
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);// 验证某个对象,，其实也可以只验证其中的某一个属性的

        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation<Object> constraintViolation = iter.next();
            String message = constraintViolation.getMessage();
            Path name = constraintViolation.getPropertyPath();
            if (StringUtils.isNotEmpty(extra) && extra.contains("|" + name + "|")) {
                buffer.append(name + message + ",");
            }
        }
        if (StringUtils.isNotBlank(buffer)) {
            errorMsg = buffer.substring(0, buffer.length() - 1);
        }
        return errorMsg;
    }

    public static boolean checkParam(Object obj, String extra, ResponseBean<Object> responseBean) {
        return check(obj, extra, responseBean);

    }

    public static boolean checkParam(Object obj, ResponseBean<Object> responseBean) {
        return check(obj, null, responseBean);
    }

    public static boolean check(Object obj, String extra, ResponseBean<Object> responseBean) {
        boolean flag = true;
        String result = CheckUtil.validateModel(obj, extra);
        if (StringUtils.isNoneBlank(result)) {
            responseBean.setCode(CommonExceptionEnum.VALIDATE_EXCEPTION.getCode());
            responseBean.setMsg(result);
            flag = false;
        }

        return flag;
    }

    /**
     * 只校验包含在extra里面的属性
     * @param obj
     * @param extra
     * @param responseBean
     * @return
     */
    public static boolean checkContainsExtra(Object obj, String extra, ResponseBean<Object> responseBean) {
        boolean flag = true;
        String result = CheckUtil.validateModelContainsExtra(obj, extra);
        if (StringUtils.isNoneBlank(result)) {
            responseBean.setCode(CommonExceptionEnum.VALIDATE_EXCEPTION.getCode());
            responseBean.setMsg(result);
            flag = false;
        }

        return flag;
    }

    /**
     * 返回错误信息与错误码放在内层jsonObject
     * @param obj
     * @param extra
     * @param respJsonObj
     * @return
     */
    public static boolean checkPlus(Object obj, String extra, JSONObject respJsonObj) {
        boolean flag = true;
        String result = CheckUtil.validateModel(obj, extra);
        if (StringUtils.isNoneBlank(result)) {
            respJsonObj.put("bizCode","0001");
            respJsonObj.put("bizMsg",result);
            flag = false;
        }

        return flag;
    }

    /**
     * 判断小数点后2位的数字的正则表达式
     *
     * @param amt
     * @param respJsonObj
     * @return
     */
    public static boolean checkAmt(String amt, JSONObject respJsonObj){
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match=pattern.matcher(amt);
        if(!match.matches()){
            respJsonObj.put("bizCode","0001");
            respJsonObj.put("bizMsg","金额格式有误");
            return false;
        }
        return true;
    }

    /**
     * 判断对象是否为空
     * @param object
     * @return
     */
    public static boolean objCheckIsNull(Object object){

        if (null == object) {
            return true;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 得到所有属性
        Field fields[] = clazz.getDeclaredFields();
        boolean flag = true;
        for(Field field : fields){
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(fieldValue != null){
                //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }
}
