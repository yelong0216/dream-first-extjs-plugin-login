package dream.first.extjs.plugin.login.handler;

/**
 * 登录处理拦截器
 */
public interface LoginHandleInterceptor {

	/**
	 * 登录处理前的处理工作
	 */
	LoginConfig process(LoginConfig loginConfig) throws LoginHandleException;

}
