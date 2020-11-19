package dream.first.extjs.plugin.login.handler;

import java.util.List;

/**
 * 登录处理器
 */
public interface LoginHandler {

	/**
	 * 登录处理
	 * 
	 * @param loginConfig 登录配置
	 * @return 登录结果
	 * @throws LoginHandleException 登录处理异常
	 */
	LoginResult handle(LoginConfig loginConfig) throws LoginHandleException;

	/**
	 * 添加登录处理前的拦截器
	 */
	void addLoginHandleInterceptor(LoginHandleInterceptor loginHandleInterceptor);

	/**
	 * 添加登录处理前的拦截器
	 */
	void addLoginHandleInterceptors(List<LoginHandleInterceptor> loginHandleInterceptors);

	/**
	 * 添加登录处理后返回结果前的拦截器
	 */
	void addLoginResultInterceptor(LoginResultInterceptor loginResultInterceptor);

	/**
	 * 添加登录处理后返回结果前的拦截器
	 */
	void addLoginResultInterceptors(List<LoginResultInterceptor> loginResultInterceptors);

}
