/**
 * 
 */
package dream.first.extjs.plugin.login.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @since 2.0
 */
public class LoginConfig {

	/**
	 * 默认登录失败重试次数
	 */
	public static final int DEFAULT_MAX_LOGIN_FAIL_RETRY_TIMES = 5;

	private String username;

	private String password;

	private HttpServletRequest request;

	private HttpServletResponse response;

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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
