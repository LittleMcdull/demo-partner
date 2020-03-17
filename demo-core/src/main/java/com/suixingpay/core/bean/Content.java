/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/04
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.bean;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/04 20:04
 * @version: V1.0
 */
public class Content {

    /**
     * 默认：UTF-8编码
     */
    public static String ENCODING_UTF_8 = "UTF-8";

    public static String RSA = "RSA";

    public static String version = "1.0";
    /** 蜻蜓版本号*/
    public static String version3 = "3.0";
    /** 越客蜻蜓版本号*/
    public static String version4 = "4.0";

    public static final String PARAM = "tradenum";

    public static final String HISTORY_CONTENT_JOINER = "/";

    public static final int SCHEDULED_THREAD_POOL = 20;

    public static final String NOTIFY_CODE_FAILED = "9999";

    public static final String NOTIFY_CODE_SUCCESS = "0000";

    public static final String NOTIFY_CODE_SUCCESS_STRING = "success";
    public static int NOTIFY_NUMBER_MAX = 2;

    public static String TOKEN_IS_NOT_NULL = "token不能为空";

    public static String TOKEN_NO_PARTNER_CORRESPONDENCE = "没有企业对应此token";

    public static String NO_QUERY_THE_ORDER = "没有查询到该笔订单";

    public static String NO_MISMATCH_AS_TOKEN = "企业与token不匹配";

    public static String ACCOUNT_FILE_NOT_FOUND = "对应对账文件不存在";

    public static String XLS_FILE_SUFFIX = ".xls";
    public static String XLSX_FILE_SUFFIX = ".xlsx";
    public static String CSV_FILE_SUFFIX = ".csv";
    public static String PDF_FILE_SUFFIX = ".pdf";

    public static String RICHTEXT_FLODER_NAME = "RichText";

    public static String BANNERIMG_FLODER_NAME = "BannerImg";

    public static String BANNER_NO_FOUND = "没有找到该Banner";

    public static int RICHTEXT_FILE_MAXSIZE = 10;//最大上传富文本大小单位M

    public static String FILE_GREATER_MAXSIZE = "文件大小超过限制";//最大上传富文本大小

    public static String RICHTEXT_FILE_TYPE = ".txt";//最大上传富文本大小

    public static String FILE_UPLOAD_NOT_NULL = "上传文件不能为空";

    public static String FILE_UPLOAD_TYPE_IMG = "需要上传图片类型的文件";

    public static String SUPPORT_UPLOAD_TYPE_IMG = ".BMP|.JPG|.JPEG|.PNG|.GIF";

    public static String STATICRES_URL_PREFIX = "/staticRes/";//静态资源访问前缀

    public static String STATICRES_PUBLIC_PATH_SUFFIX = "public";//公共静态文件路径

    public static String UPLOAD_OPT_GUIDE_TYPE = "1";//上传操作指南的TYPE

    public static String OPT_GUIDE_CODE_TYPE = "OPT_GUIDE";//操作指南码表Code类型

    public static String OPT_GUIDE_PATH_TYPE = "PATH";//操作指南码表地址code_name

    public static String OPT_GUIDE_STATUS_TYPE = "STATUS";//操作指南码表状态code_name

    public static String XLS_FILE_FOLDER = "Excel";

    public static String PDF_FILE_FOLDER = "PDF";

    public static String CSV_FILE_FOLDER = "CSV";

    public static int DEFAULT_REQUEST_TIMEOUT_SECONDS = 10000;

    public static final String NOTIFY_FLAG_TYPE_CT = "01";

    public static final String NOTIFY_FLAG_TYPE_UR_CODE = "02";

    public static final String NOTIFY_FLAG_TYPE_CONSUME_REVERSE = "03";

    public static final String TRAN_STSTUS_RESULT_SUCCESS = "01";//成功

    public static final String TRAN_STSTUS_RESULT_FAIL = "00";//失败

    public static final String TRAN_STSTUS_RESULT_UNPAID = "02";//待支付

    public static final String SETRAM = "settlenum";

    public static final String SYSID = "ICM";

    public static final String PRODNO = "800";

    public static final String orgIdKey = "icm:orgid:";

    public static final String orgIdKeyMq = "icm:mq:orgid:";
    public static final String orgIdKeyCallback = "api-order-callback:mq:orgid:";

    public static final int BATCH_SIZE=1000;

    public  static  final  String HSHTN="HSTN0001";
    public  static  final  String HSHD1="HSD10001";
    public  static  final  String FAIL="FAIL";
    public  static  final  String HSH0305="0305";

    public static String print() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("                   _ooOoo_\n");
        sb.append("                  o8888888o\n");
        sb.append("                  88\" . \"88\n");
        sb.append("                  (| -_- |)\n");
        sb.append("                  O\\  =  /O\n");
        sb.append("               ____/`---'\\____\n");
        sb.append("             .'  \\\\|     |//  `.\n");
        sb.append("            /  \\\\|||  :  |||//  \\ \n");
        sb.append("           /  _||||| -:- |||||-  \\ \n");
        sb.append("           |   | \\\\\\  -  /// |   |\n");
        sb.append("           | \\_|  ''\\---/''  |   |\n");
        sb.append("           \\  .-\\__  `-`  ___/-. /\n");
        sb.append("         ___`. .'  /--.--\\  `. . __\n");
        sb.append("      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n");
        sb.append("     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n");
        sb.append("     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n");
        sb.append("======`-.____`-.___\\_____/___.-`____.-'======\n");
        sb.append("                   `=---='\n");
        sb.append("...................................................\n");
        return sb.toString();
    }
}
