package dream.first.extjs.plugin.login.handler.defaults;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.yelong.core.model.service.SqlModelService;

import com.labbol.cocoon.core.utils.security.coder.JSDesCoder;

import dream.first.core.platform.constants.State;
import dream.first.core.platform.user.model.User;
import dream.first.core.platform.user.service.UserCommonService;
import dream.first.extjs.plugin.login.handler.AbstractLoginHandler;
import dream.first.extjs.plugin.login.handler.LoginConfig;
import dream.first.extjs.plugin.login.handler.LoginHandleException;
import dream.first.extjs.plugin.login.handler.LoginResult;

public class DefaultLoginHandler extends AbstractLoginHandler {

	@Resource
	private SqlModelService modelService;

	@Resource
	private UserCommonService userCommonSerfvice;

	@Override
	public LoginResult doHandle(LoginConfig loginConfig) throws LoginHandleException {
		User sourceUser = null;
		boolean result = false;
		String authFailMessage = "";
		// String username = JSDesCoder.strDec(user.getUsername(), "1", "2", "3");
		// String password = JSDesCoder.strDec(user.getPassword(), "1", "2", "3");
		String username = loginConfig.getUsername();
		String password = loginConfig.getPassword();
		// 根据用户名查询用户，然后在验证密码
		if (StringUtils.isBlank(username)) {
			sourceUser = null;
		} else {
			User sqlModel = new User();
			sqlModel.setState(State.YES.CODE);
			sqlModel.setUsername(username);
			sourceUser = modelService.findFirstBySqlModel(User.class, sqlModel);
		}
		if (null == sourceUser) {// 账号密码错误
			result = false;
			authFailMessage = "登录验证失败，账号或密码错误！";
			sourceUser = new User();
			sourceUser.setUsername(username);
			sourceUser.setPassword(password);
		} else if ("02".equals(sourceUser.getIsLock())) {// 账号被锁定了
			result = false;
			authFailMessage = "账号已被锁定！如有问题，请与客服联系！";
		} else if (!JSDesCoder.strDec(sourceUser.getPassword(), "1", "2", "3").equals(password)) {// 密码不正确
			Integer loginFailTimes = sourceUser.getLoginFailTimes();
			if (loginFailTimes == null) {
				loginFailTimes = Integer.valueOf(0);
			}
			loginFailTimes = Integer.valueOf(loginFailTimes.intValue() + 1);
			int maxLoginFailRetryTimes = loginConfig.getMaxLoginFailRetryTimes() + 1;
			if (loginFailTimes.intValue() >= maxLoginFailRetryTimes) {
				String sql = "update CO_USER set isLock = ?, lockTime = sysdate() where username = ?";
				modelService.getBaseDataBaseOperation().update(sql, "02", username);
				authFailMessage = "重试次数已到，账号被锁定！如有问题，请与客服联系！";
			} else {
				String sql = "update CO_USER set loginFailTimes = ? where username = ? ";
				modelService.getBaseDataBaseOperation().update(sql, loginFailTimes, username);
				authFailMessage = "账号或密码错误！您还有 " + (maxLoginFailRetryTimes - loginFailTimes.intValue()) + "次重试机会！";
			}
			result = false;
		} else if (StringUtils.isBlank(sourceUser.getPwdSign())
				&& !(sourceUser.getPwdSign().equals(this.userCommonSerfvice.getPasswordSign(password)))) {
			authFailMessage = "账号信息被窜改，无法登录，请与客服联系！";
			result = false;
		} else {// 登录成功
			String sql = "update CO_USER set loginFailTimes = ? where username = ? ";
			modelService.getBaseDataBaseOperation().update(sql, 0, username);
			result = true;
		}
		return new LoginResult(result, sourceUser, authFailMessage, loginConfig);
	}

}
