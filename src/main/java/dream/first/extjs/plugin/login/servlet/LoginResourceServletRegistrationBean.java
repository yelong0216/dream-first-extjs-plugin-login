package dream.first.extjs.plugin.login.servlet;

import org.yelong.support.servlet.resource.ResourceServlet;
import org.yelong.support.servlet.resource.response.ResourceResponseHandler;
import org.yelong.support.spring.boot.servlet.resource.ResourceServletRegistrationBean;

import dream.first.extjs.plugin.login.servlet.LoginResourceServletRegistrationBean.LoginResourceServlet;


public class LoginResourceServletRegistrationBean extends ResourceServletRegistrationBean<LoginResourceServlet> {

	public static final String urlPrefix = "/resources/extjs/plugin/login";

	public static final String resourceRootPath = "/dream/first/extjs/plugin/resources/login/publics/extjs/plugin/login";

	public LoginResourceServletRegistrationBean() {
		this(urlPrefix);
	}

	public LoginResourceServletRegistrationBean(String urlPrefix) {
		this(urlPrefix, resourceRootPath);
	}

	public LoginResourceServletRegistrationBean(String urlPrefix, String resourceRootPath) {
		super(urlPrefix, resourceRootPath, new LoginResourceServlet());
	}

	public static final class LoginResourceServlet extends ResourceServlet {

		private static final long serialVersionUID = -454745587938652439L;

		public LoginResourceServlet() {
		}

		public LoginResourceServlet(ResourceResponseHandler resourceResponseHandler) {
			super(resourceResponseHandler);
		}

	}

}
