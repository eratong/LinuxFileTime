package com.example.demo.exception;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: jun.lei@counect.com
 * Date: 2017-12-14
 * Time: 14:58
 */
public class ReceiptProcessException extends RuntimeException {
	
	public final static String CUBE_NOT_EXIST = "N_CUBE";
	public final static String RECEIPT_TYPE_NULL = "N_TYPE";
	public final static String ENDING_PARA_NULL = "N_END";
	public final static String BOKE_PARA_NULL = "N_BOKE";
	public final static String LAST_E1_FILE_NULL = "N_E1_FILE";
	public final static String RECEIPT_FILE_EMPTY = "EMPTY_RECEIPT_FILE";
	
	public final static String TAR_GZ_FILE_NULL = "N_TAR_GZ";
	public final static String BOKE_NO_FILE = "N_FILE";
	public final static String BOKE_SYSTEM_ERROR = "B_SYSE";
	public final static String BOKE_FIFOLOOP_ERROR = "B_FIFOLOOP_ERROR";
	
	private String errorCode;
	
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	
	public ReceiptProcessException() {
		super();
	}
	
	public ReceiptProcessException(String code, String msg) {
		super(msg);
		this.errorCode = code;
	}
	
	public ReceiptProcessException(String code, String msg,
                                   Throwable cause) {
		super(msg, cause);
		this.errorCode = code;
	}
	
	public ReceiptProcessException(Throwable cause) {
		super(cause);
	}
}
