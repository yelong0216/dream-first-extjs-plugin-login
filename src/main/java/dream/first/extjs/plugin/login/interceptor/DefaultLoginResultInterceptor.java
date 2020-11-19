package dream.first.extjs.plugin.login.interceptor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yelong.core.model.collector.ModelCollectors;
import org.yelong.core.model.service.SqlModelService;
import org.yelong.support.servlet.HttpServletUtils;

import dream.first.core.platform.login.model.LoginSession;
import dream.first.core.platform.login.service.LoginSessionCommonService;
import dream.first.core.platform.org.model.Org;
import dream.first.core.platform.user.model.User;
import dream.first.core.platform.user.service.UserRightCommonService;
import dream.first.extjs.login.LoginSessionUser;
import dream.first.extjs.login.LoginUserInfo;
import dream.first.extjs.login.LoginUserInfoHolder;
import dream.first.extjs.plugin.login.controller.LoginController;
import dream.first.extjs.plugin.login.handler.LoginConfig;
import dream.first.extjs.plugin.login.handler.LoginHandleException;
import dream.first.extjs.plugin.login.handler.LoginResult;
import dream.first.extjs.plugin.login.handler.LoginResultInterceptor;
import dream.first.extjs.support.msg.JsonMsg;
import dream.first.plugin.support.log.LogRecordUtils;

public class DefaultLoginResultInterceptor implements LoginResultInterceptor {

	@Resource
	private UserRightCommonService userRightCommonService;

	@Resource
	private LoginSessionCommonService loginSessionCommonService;

	@Resource
	private SqlModelService modelService;

	public static final Logger log = LoggerFactory.getLogger(DefaultLoginResultInterceptor.class);

	@Override
	public LoginResult process(LoginConfig loginConfig, LoginResult loginResult) throws LoginHandleException {
		HttpServletRequest request = loginConfig.getRequest();
		HttpSession session = request.getSession();
		User user = loginResult.getUser();
		if (loginResult.isResult()) {// 认证通过
			List<String> opRights = userRightCommonService.findModuleIds(user.getId());// 用户所拥有的权限id
			LoginUserInfo loginUserInfo = new LoginUserInfo();
			loginUserInfo.setUser(user);
			Org org = modelService.collect(ModelCollectors.getModelByOnlyPrimaryKeyEQ(Org.class, user.getOrgId()));
			loginUserInfo.setOrg(org);
			loginUserInfo.setOpRights(opRights);
			LoginUserInfoHolder.setLoginUser(loginUserInfo);
			LoginSessionUser.setLoginUserInfo(session, loginUserInfo);
			try {
				// 验证该用户是否已经登录。如果已经登录则强制对象下线。下线由LoginInterceptor实现
				LoginSession loginSession = new LoginSession();
				loginSession.setLoginIp(HttpServletUtils.getIpAddrByNginxReverseProxy(request));
				loginSession.setLoginTime(new Date());
				loginSession.setSessionId(session.getId());
				loginSession.setUsername(user.getUsername());
				// loginSession.setUserAgent(getRequest().get);
				loginSessionCommonService.saveOverrideUsername(loginSession);
			} catch (Exception e) {
				log.error("单用户登录异常", e);
			}
			loginResult.put(LoginController.LOGIN_RESPONSE_JSONMSG_NAME, new JsonMsg(true, "登录成功"));
			// 记录日志
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "登录成功！用户部门：" + org.getOrgName());
		} else {
			loginResult.put(LoginController.LOGIN_RESPONSE_JSONMSG_NAME,
					new JsonMsg(false, loginResult.getAuthFailMessage()));
			LogRecordUtils.setLogUserName(user.getUsername());
			LogRecordUtils.setLogDesc(user.getUsername() + "登录失败！登录说明：" + loginResult.getAuthFailMessage());
		}
		return loginResult;
	}

}
