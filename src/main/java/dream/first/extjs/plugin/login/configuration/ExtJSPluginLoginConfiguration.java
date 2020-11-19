package dream.first.extjs.plugin.login.configuration;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.yelong.support.servlet.resource.response.support.ResourceResponseSupporter;

import dream.first.extjs.plugin.login.ExtJSPluginLogin;
import dream.first.extjs.plugin.login.controller.LoginController;
import dream.first.extjs.plugin.login.controller.LoginErrorViewResolver;
import dream.first.extjs.plugin.login.handler.LoginHandleInterceptor;
import dream.first.extjs.plugin.login.handler.LoginHandler;
import dream.first.extjs.plugin.login.handler.LoginResultInterceptor;
import dream.first.extjs.plugin.login.handler.defaults.DefaultLoginHandler;
import dream.first.extjs.plugin.login.interceptor.DefaultLoginResultInterceptor;
import dream.first.extjs.plugin.login.interceptor.LoginIPRestrictionHandleInterceptor;
import dream.first.extjs.plugin.login.servlet.LoginResourceServletRegistrationBean;

public class ExtJSPluginLoginConfiguration {

	@Bean
	@ConditionalOnMissingBean(LoginController.class)
	public LoginController loginController() {
		return new LoginController();
	}

	@Bean
	@Order(10000)
	@ConditionalOnMissingBean(DefaultLoginResultInterceptor.class)
	public LoginResultInterceptor defaultLoginResultInterceptor() {
		return new DefaultLoginResultInterceptor();
	}

	/**
	 * 登录IP限制拦截器
	 */
	@Bean
	@Order(-10000)
	@ConditionalOnProperty(prefix = ExtJSPluginLogin.PROPERTIES_PREFIX, name = "loginip", havingValue = "true", matchIfMissing = false)
	@ConditionalOnMissingBean(LoginIPRestrictionHandleInterceptor.class)
	public LoginIPRestrictionHandleInterceptor loginIPRestrictionHandleInterceptor() {
		return new LoginIPRestrictionHandleInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(LoginHandler.class)
	public LoginHandler loginHandler(ObjectProvider<List<LoginHandleInterceptor>> LoginHandleInterceptorProviders,
			ObjectProvider<List<LoginResultInterceptor>> loginResultInterceptorProviders) {
		DefaultLoginHandler loginHandler = new DefaultLoginHandler();
		List<LoginHandleInterceptor> LoginHandleInterceptors = LoginHandleInterceptorProviders.getIfAvailable();
		if (null != LoginHandleInterceptors) {
			loginHandler.addLoginHandleInterceptors(LoginHandleInterceptors);
		}
		List<LoginResultInterceptor> loginResultInterceptors = loginResultInterceptorProviders.getIfAvailable();
		if (null != loginResultInterceptors) {
			loginHandler.addLoginResultInterceptors(loginResultInterceptors);
		}
		return loginHandler;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@ConditionalOnMissingBean(LoginErrorViewResolver.class)
	public LoginErrorViewResolver loginErrorViewResolver(ResourceResponseSupporter resourceResponseSupporter) {
		return new LoginErrorViewResolver(resourceResponseSupporter);
	}

	@Bean
	@ConditionalOnMissingBean(LoginResourceServletRegistrationBean.class)
	public LoginResourceServletRegistrationBean loginResourceServletRegistrationBean() {
		return new LoginResourceServletRegistrationBean();
	}

}
