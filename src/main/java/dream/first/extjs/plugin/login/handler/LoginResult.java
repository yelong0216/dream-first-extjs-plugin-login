package dream.first.extjs.plugin.login.handler;

import java.util.HashMap;

import org.yelong.commons.util.map.MapWrapper;

import dream.first.core.platform.user.model.User;

/**
 * 登录结果
 * 
 * @since 2.0
 */
public class LoginResult extends MapWrapper<String, Object> {

	private boolean result;

	private User user;

	private String authFailMessage;

	private LoginConfig loginConfig;

	public LoginResult() {
		super(new HashMap<>());
	}

	public LoginResult(boolean result, User user, String authFailMessage, LoginConfig loginConfig) {
		this();
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

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAuthFailMessage(String authFailMessage) {
		this.authFailMessage = authFailMessage;
	}

	public void setLoginConfig(LoginConfig loginConfig) {
		this.loginConfig = loginConfig;
	}

}
