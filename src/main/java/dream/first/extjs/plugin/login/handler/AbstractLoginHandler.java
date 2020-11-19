package dream.first.extjs.plugin.login.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象实现
 */
public abstract class AbstractLoginHandler implements LoginHandler {

	protected final List<LoginHandleInterceptor> loginHandleInterceptors = new ArrayList<>();

	protected final List<LoginResultInterceptor> loginResultInterceptors = new ArrayList<>();

	@Override
	public final LoginResult handle(LoginConfig loginConfig) throws LoginHandleException {
		for (LoginHandleInterceptor loginHandleInterceptor : loginHandleInterceptors) {
			loginConfig = loginHandleInterceptor.process(loginConfig);
		}
		LoginResult loginResult = doHandle(loginConfig);
		for (LoginResultInterceptor loginResultInterceptor : loginResultInterceptors) {
			loginResult = loginResultInterceptor.process(loginConfig, loginResult);
		}
		return loginResult;
	}

	public abstract LoginResult doHandle(LoginConfig loginConfig) throws LoginHandleException;

	@Override
	public void addLoginHandleInterceptor(LoginHandleInterceptor loginHandleInterceptor) {
		loginHandleInterceptors.add(loginHandleInterceptor);
	}

	@Override
	public void addLoginHandleInterceptors(List<LoginHandleInterceptor> loginHandleInterceptors) {
		this.loginHandleInterceptors.addAll(loginHandleInterceptors);
	}

	@Override
	public void addLoginResultInterceptor(LoginResultInterceptor loginResultInterceptor) {
		loginResultInterceptors.add(loginResultInterceptor);
	}

	@Override
	public void addLoginResultInterceptors(List<LoginResultInterceptor> loginResultInterceptors) {
		this.loginResultInterceptors.addAll(loginResultInterceptors);
	}

}
