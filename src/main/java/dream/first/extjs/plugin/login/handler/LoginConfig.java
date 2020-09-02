/**
 * 
 */
package dream.first.extjs.plugin.login.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @since 2.0
 */
public final class LoginConfig {

	/**
	 * 默认登录失败重试次数
	 */
	public static final int DEFAULT_MAX_LOGIN_FAIL_RETRY_TIMES = 5;

	private final String username;

	private final String password;

	private final HttpServletRequest request;

	private final HttpServletResponse response;

	private int maxLoginFailRetryTimes = DEFAULT_MAX_LOGIN_FAIL_RETRY_TIMES;

	public LoginConfig(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		this.username = username;
		this.password = password;
		this.request = request;
		this.response = response;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getMaxLoginFailRetryTimes() {
		return maxLoginFailRetryTimes;
	}

	public void setMaxLoginFailRetryTimes(int maxLoginFailRetryTimes) {
		this.maxLoginFailRetryTimes = maxLoginFailRetryTimes;
	}

}
