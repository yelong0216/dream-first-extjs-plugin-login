Co.initialize();
var username, password, origpassword, token;
Ext.onReady(function() {
	username = document.getElementById("username"), password = document
	.getElementById("password"), origpassword = document
	.getElementById("orig-password"), token = document
	.getElementById("token");
	if (Ext.isIE6) {
		Co.showWarning("系统不支持IE6、7浏览器，请使用IE8及以上版本浏览器！", function() {
			username.disabled = true;
			origpassword.disabled = true;
			window.location = "download/IE8-WindowsXP-x86-CHS.exe";
		});
	}

	if (Ext.isIE) {
		username.size = "24";
		origpassword.size = "25";
	}

	Ext.EventManager.addListener("username", "keydown", function(event,
			target, opts) {
		if (event.keyCode == 13) {
			doLogin();
		}
	});
	Ext.EventManager.addListener("orig-password", "keydown", function(
			event, target, opts) {
		if (event.keyCode == 13) {
			doLogin();
		}
	});

	Ext.EventManager.addListener("loginButton", "click", function(event,
			target, opts) {
		doLogin();
	});

	// Ext.EventManager.addListener("resetButton", "click", function(event, target, opts){
	// 	doReset();
	// });

	if (!Co.isEmpty(Co.getCookie("__CO_PLAT_LOGIN_USER__"))) {
		username.value = Co.getCookie("__CO_PLAT_LOGIN_USER__");
		origpassword.focus();
	} else {
		username.focus();
	}
});

function doLogin() {
	if (Co.isEmpty(username.value)) {
		Co.showError("请输入账号！", function() {
			username.focus();
		});
		return;
	}
	if (Co.isEmpty(origpassword.value)) {
		Co.showError("请输入密码！", function() {
			origpassword.focus();
		});
		return;
	}

	//password.value = hex_sha1(origpassword.value);
	password.value = origpassword.value;
	token.value = hex_md5(LOGIN_TOKEN + password.value);
	Co.request(rootPath+"/login", {
		"model.username" : username.value,
		"model.password" : password.value,
		"token" : token.value
	}, function(result, opts) {
		if (result.success) {
			//在本地保存登录名
			Co.setCookie("__CO_PLAT_LOGIN_USER__", "");
			window.location = rootPath+"/index";
			//Co.windowLocationByToken("/index");
		} else {
			Co.setCookie("__CO_PLAT_LOGIN_USER__", username.value);
			Co.showError(result.msg, function() {
				window.location = rootPath+"/login.jsp";
			});
		}
	}, function(result, opts) {
		Co.showError(result.msg, function() {
			window.location = rootPath+"/login.jsp";
		}, result);
	}, {
		waitMsg : "正在验证，请稍候...",
		reqEncrypt : true,
		reqDataIntegrityValid : true
	});
}

function doReset() {
	username.value = "";
	password.value = "";
	origpassword.value = "";
	username.focus();
}