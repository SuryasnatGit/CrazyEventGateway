package com.cisco.ceg.common;

public interface CEGConstants {

	public static final String API_ENDPOINT = "/api/v1/events";

	public static final String FILE_EXTN = ".dat";
	public static final String FILE_NAME_SDF = "dd-MM-yy";
	public static final String APPEND_DATE_SDF = "dd/MM/yy";
	public static final String FILE_SIZE_LIMIT = "10MB";
	public static final String ENCODER_PATTERN = "%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n";
	public static final String ROLLING_FILE_NAME_PATTERN = "logfile-%d{yyyy-MM-dd_HH}.log";

	// Security constants
	public static final String USER_ROLE = "USER";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String[] ALL_ROLES = { "USER", "ADMIN" };
	public static final String ALLOWED_END_POINT = "/events/**";
	public static final String BCRYPT_ENCODINGID = "bcrypt";
	public static final String PBKDF2_PWD_ENCODER = "pbkdf2";
	public static final String SCRYPT_PWD_ENCODER = "scrypt";

}
