package dream.first.extjs.plugin.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.yelong.support.servlet.resource.response.ResourceResponseProperties;
import org.yelong.support.servlet.resource.response.support.ResourceResponseSupporter;

import dream.first.core.utils.RSAUtils;
import dream.first.extjs.plugin.login.ExtJSPluginLogin;

/**
 * 登录页面视图
 */
public class LoginView implements View {

	private ResourceResponseSupporter resourceResponseSupporter;

	public LoginView(ResourceResponseSupporter resourceResponseSupporter) {
		this.resourceResponseSupporter = resourceResponseSupporter;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String token = String.valueOf(System.currentTimeMillis());
		request.getSession().setAttribute("__LOGIN_TOKEN__", token);

		String rsaPublicKey = RSAUtils.getJsPublicKey();
		request.setAttribute("RSA_PUBLIC_KEY", rsaPublicKey);

		response.setStatus(200);
		resourceResponseSupporter.responseHtml(new ResourceResponseProperties(request, response),
				ExtJSPluginLogin.RESOURCE_PRIVATES_PACKAGE, ExtJSPluginLogin.RESOURCE_PREFIX + "/html/login.html");
	}

}
