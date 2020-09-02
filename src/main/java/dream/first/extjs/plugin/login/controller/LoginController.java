/**
 * 
 */
package dream.first.extjs.plugin.login.controller;

import java.util.Date;
import java.util.List;

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
import org.yelong.support.servlet.HttpServletUtils;

import dream.first.core.platform.login.model.LoginSession;
import dream.first.core.platform.login.service.LoginSessionCommonService;
import dream.first.core.platform.org.model.Org;
import dream.first.core.platform.user.model.User;
import dream.first.core.platform.user.service.UserRightCommonService;
import dream.first.extjs.controller.BaseExtJSController;
import dream.first.extjs.login.LoginSessionUser;
import dream.first.extjs.login.LoginUserInfo;
import dream.first.extjs.login.LoginUserInfoHolder;
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

	@Resource
	private UserRightCommonService userRightCommonService;

	@Resource
	private LoginSessionCommonService loginSessionCommonService;

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
		HttpSession session = request.getSession();
		JsonMsg msg = new JsonMsg(false, "登录验证失败，账号或密码错误！");
		String token = (String) request.getSession().getAttribute("__LOGIN_TOKEN__");
		token = StringUtils.isBlank(token) ? request.getParameter("__LOGIN_TOKEN__") : token;
		if (StringUtils.isBlank(token)) {
			LogRecordUtils.setLogUserName(username);
			LogRecordUtils.setLogDesc(username + "登录失败！登录说明：页面已过期，请刷新页面后登录！");
			msg.setMsg("页面已过期，请刷新页面后登录！");
			return toJson(msg);
		}
		LoginConfig loginConfig = new LoginConfig(username, password, request, getResponse());
		loginConfig.setMaxLoginFailRetryTimes(maxLoginFailRetryTimes);
		LoginResult loginResult = loginHandler.handle(loginConfig);
		User user = loginResult.getUser();
		if (loginResult.isResult()) {// 认证通过
			List<String> opRights = userRightCommonService.findModuleIds(user.getId());// 用户所拥有的权限id
			LoginUserInfo loginUserInfo = new LoginUserInfo();
			loginUserInfo.setUser(user);
			Org org = modelService.findById(Org.class, user.getOrgId());
			loginUserInfo.setOrg(org);
			loginUserInfo.setOpRights(opRights);
			LoginUserInfoHolder.setLoginUser(loginUserInfo);
			LoginSessionUser.setLoginUserInfo(session, loginUserInfo);
			// 验证该用户是否已经登录。如果已经登录则强制对象下线。下线由LoginInterceptor实现
			LoginSession loginSession = new LoginSession();
			loginSession.setLoginIp(HttpServletUtils.getIpAddrByNginxReverseProxy(getRequest()));
			loginSession.setLoginTime(new Date());
			loginSession.setSessionId(session.getId());
			loginSession.setUsername(user.getUsername());
			// loginSession.setUserAgent(getRequest().get);
			loginSessionCommonService.saveOverrideUsername(loginSession);

			msg.setSuccess(true);
			msg.setMsg("登录成功");

			// 记录日志
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "登录成功！用户部门：" + org.getOrgName());
		} else {
			msg.setMsg(loginResult.getAuthFailMessage());
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "登录失败！登录说明：" + loginResult.getAuthFailMessage());
		}
		return toJson(msg);
	}

	/**
	 * 注销
	 */
	@LogRecord(operModule = "注销登录", eventType = "01")
	@RequestMapping(value = "logout")
	@LoginValidate(validate = false)
	@RightsValidate(validate = false)
	public String logout(@ModelAttribute User model) {
		HttpSession session = getRequest().getSession();
		LoginUserInfo loginUserInfo = (LoginUserInfo) session
				.getAttribute(LoginSessionUser.SESSION_LOGIN_USER_INFO.name());
		LoginSessionUser.removeLoginUserInfo(session);
		if (null != loginUserInfo) {
			User user = loginUserInfo.getUser();
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "注销登录！");
		}
		return "error/error.jsp";
	}

}
