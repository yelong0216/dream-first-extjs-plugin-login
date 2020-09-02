package dream.first.extjs.plugin.login.handler;

import dream.first.core.platform.user.model.User;

/**
 * 登录结果
 * 
 * @since 2.0
 */
public class LoginResult {

	private final boolean result;

	private final User user;

	private final String authFailMessage;

	private final LoginConfig loginConfig;

	public LoginResult(boolean result, User user, String authFailMessage, LoginConfig loginConfig) {
		this.result = result;
		this.user = user;
		this.authFailMessage = authFailMessage;
		this.loginConfig = loginConfig;
	}

	public boolean isResult() {
		return result;
	}

	public User getUser() {
		return user;
	}

	public String getAuthFailMessage() {
		return authFailMessage;
	}

	public LoginConfig getLoginConfig() {
		return loginConfig;
	}

}
