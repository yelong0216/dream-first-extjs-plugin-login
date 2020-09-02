package dream.first.extjs.plugin.login.handler;

public interface LoginHandler {

	LoginResult handle(LoginConfig loginConfig) throws LoginHandleException;

}
