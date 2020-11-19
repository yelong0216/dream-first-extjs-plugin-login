/**
 * 
 */
package dream.first.extjs.plugin.login.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.commons.lang.Strings;
import org.yelong.support.spring.mvc.HandlerResponseWay;
import org.yelong.support.spring.mvc.ResponseWay;

import dream.first.core.exception.RequestException;
import dream.first.core.platform.user.model.User;
import dream.first.extjs.controller.BaseExtJSController;
import dream.first.extjs.login.LoginSessionUser;
import dream.first.extjs.login.LoginUserInfo;
import dream.first.extjs.login.LoginValidate;
import dream.first.extjs.plugin.login.ExtJSPluginLogin;
import dream.first.extjs.plugin.login.handler.LoginConfig;
import dream.first.extjs.plugin.login.handler.LoginHandler;
import dream.first.extjs.plugin.login.handler.LoginResult;
import dream.first.extjs.support.msg.JsonMsg;
import dream.first.plugin.support.log.LogRecord;
import dream.first.plugin.support.log.LogRecordUtils;
import dream.first.plugin.support.rights.RightsValidate;

@Controller
public class LoginController extends BaseExtJSController {

	@Resource
	private LoginHandler loginHandler;

	public static final String LOGIN_RESPONSE_JSONMSG_NAME = "jsonMsg";

	@Value("${" + ExtJSPluginLogin.PROPERTIES_PREFIX + ".maxLoginFailRetryTimes:"
			+ LoginConfig.DEFAULT_MAX_LOGIN_FAIL_RETRY_TIMES + "}")
	private Integer maxLoginFailRetryTimes;

	@ResponseBody
	@RequestMapping(value = "login")
	@LoginValidate(validate = false)
	@LogRecord(eventType = "01", operModule = "登录")
	public String login() throws Exception {
		String username = getParameter("model.username");
		String password = getParameter("model.password");
		Strings.requireNonBlank(username, "请输入账号！");
		Strings.requireNonBlank(username, "请输入密码！");
		HttpServletRequest request = getRequest();
		String token = (String) request.getSession().getAttribute("__LOGIN_TOKEN__");
		token = StringUtils.isBlank(token) ? request.getParameter("__LOGIN_TOKEN__") : token;
		if (StringUtils.isBlank(token)) {
			LogRecordUtils.setLogUserName(username);
			LogRecordUtils.setLogDesc(username + "登录失败！登录说明：页面已过期，请刷新页面后登录！");
			return toJson(new JsonMsg(false, "页面已过期，请刷新页面后登录！"));
		}
		LoginConfig loginConfig = new LoginConfig(username, password, request, getResponse());
		loginConfig.setMaxLoginFailRetryTimes(maxLoginFailRetryTimes);
		LoginResult loginResult = loginHandler.handle(loginConfig);
		return toJson(loginResult.get(LOGIN_RESPONSE_JSONMSG_NAME));
	}

	/**
	 * 注销
	 */
	@LogRecord(operModule = "注销登录", eventType = "01")
	@RequestMapping(value = "logout")
	@LoginValidate(validate = false)
	@RightsValidate(validate = false)
	@ResponseWay(HandlerResponseWay.MODEL_AND_VIEW)
	public void logout(@ModelAttribute User model) {
		HttpSession session = getRequest().getSession();
		LoginUserInfo loginUserInfo = (LoginUserInfo) session
				.getAttribute(LoginSessionUser.SESSION_LOGIN_USER_INFO.name());
		LoginSessionUser.removeLoginUserInfo(session);
		if (null != loginUserInfo) {
			User user = loginUserInfo.getUser();
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "注销登录！");
		}
		throw new RequestException("此异常不用管，该异常抛出后会交给异常解析器返回登录页面");
	}

}
