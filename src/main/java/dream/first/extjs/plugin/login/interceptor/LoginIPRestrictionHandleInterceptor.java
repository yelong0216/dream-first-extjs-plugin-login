/**
 * 
 */
package dream.first.extjs.plugin.login.interceptor;

import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yelong.core.model.collector.ModelCollectors;
import org.yelong.core.model.service.SqlModelService;
import org.yelong.support.servlet.HttpServletUtils;

import dream.first.core.platform.scip.model.SCIP;
import dream.first.core.platform.scip.utils.SCIPUtils;
import dream.first.extjs.plugin.login.handler.LoginConfig;
import dream.first.extjs.plugin.login.handler.LoginHandleException;
import dream.first.extjs.plugin.login.handler.LoginHandleInterceptor;

/**
 * 文件上传IP段验证。在涉密的IP段中无法上传文件
 */
public class LoginIPRestrictionHandleInterceptor implements LoginHandleInterceptor {

	@Resource
	private SqlModelService modelService;

	public static final Logger log = LoggerFactory.getLogger(DefaultLoginResultInterceptor.class);

	@Override
	public LoginConfig process(LoginConfig loginConfig) throws LoginHandleException {
		List<SCIP> scips = modelService.collect(ModelCollectors.findAll(SCIP.class));
		if (CollectionUtils.isEmpty(scips)) {
			return loginConfig;
		}
		try {
			String userIp = HttpServletUtils.getIpAddrByNginxReverseProxy(loginConfig.getRequest());
			if (!SCIPUtils.contains(scips, userIp)) {
				throw new LoginHandleException(getExceptionMessage(loginConfig, scips, userIp));
			}
		} catch (UnknownHostException e) {
			log.error("登录IP地址段验证失败", e);
		}
		return loginConfig;
	}

	public String getExceptionMessage(LoginConfig loginConfig, List<SCIP> scips, String userIp) {
		return "IP地址(" + userIp + ")不在登录的网段内！";
	}

}
