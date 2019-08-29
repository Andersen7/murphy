package top.lishaobo.framework.util;

/**
 * Created by Jenova_Hui
 * On 2018/9/26
 */
public enum ErrEnum {

    SYS_ERR(-1, "系统异常"),
    SUCCESS(0, "");



    private Integer errCode;
    private String errMsg;

    ErrEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    }
