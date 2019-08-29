package top.lishaobo.framework.util;

import java.io.Serializable;

/**
 * 输出json数组对象
 * @author jeff_gao
 * @Comment Beans
 */
public class ResponseBean implements Serializable{
	
	/**
	 * 标识  //0成功  1失败
	 */
	private Integer code;
	/**
	 * 提示信息
	 */
	private  String msg;
	
	/**
	 * 数据
	 */
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseBean() {
		super();
	}
	
	public ResponseBean(Integer code, String msg){
		this(code, msg, "");
	}

	public ResponseBean(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/*public ResponseBean(ErrEnum errEnum){
		this.code = errEnum.getErrCode();
		this.msg = errEnum.getErrMsg();
	}*/
}
