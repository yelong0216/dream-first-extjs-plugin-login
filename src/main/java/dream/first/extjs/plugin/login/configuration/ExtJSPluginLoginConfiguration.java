package dream.first.extjs.plugin.login.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import dream.first.extjs.plugin.login.controller.LoginController;
import dream.first.extjs.plugin.login.handler.LoginHandler;
import dream.first.extjs.plugin.login.handler.impl.DefaultLoginHandler;

public class ExtJSPluginLoginConfiguration {

	@Bean
	public LoginController loginController() {
		return new LoginController();
	}

	@Bean
	@ConditionalOnMissingBean(LoginHandler.class)
	public LoginHandler loginHandler() {
		return new DefaultLoginHandler();
	}

}
