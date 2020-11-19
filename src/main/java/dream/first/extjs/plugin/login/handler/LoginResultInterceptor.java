package dream.first.extjs.plugin.login.handler;

/**
 * 登录结果拦截器
 */
@FunctionalInterface
public interface LoginResultInterceptor {

	/**
	 * 登录处理后的结果处理
	 */
	LoginResult process(LoginConfig loginConfig, LoginResult loginResult) throws LoginHandleException;

}
