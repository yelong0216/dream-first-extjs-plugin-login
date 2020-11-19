package dream.first.extjs.plugin.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.yelong.support.servlet.resource.response.support.ResourceResponseSupporter;

/**
 * 直接使用 login.jsp 作为RequestMapping的urlMapping不起作用，所以采用在异常视图解析器里面返回登录页面
 */
public class LoginErrorViewResolver implements ErrorViewResolver {

	public static final String loginJspPath = "/login.jsp";

	private ResourceResponseSupporter resourceResponseSupporter;

	public LoginErrorViewResolver(ResourceResponseSupporter resourceResponseSupporter) {
		this.resourceResponseSupporter = resourceResponseSupporter;
	}

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		String path = (String) model.get("path");
		if (StringUtils.isBlank(path)) {
			return null;
		}
		if (loginJspPath.equals(path)) {
			ModelAndView modelAndView = new ModelAndView(new LoginView(resourceResponseSupporter));
			modelAndView.setStatus(HttpStatus.OK);
			return modelAndView;
		}
		return null;
	}

}
