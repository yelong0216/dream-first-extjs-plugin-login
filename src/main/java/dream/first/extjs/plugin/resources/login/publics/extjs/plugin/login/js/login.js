
//==================================================浏览器==================================================
IEVersion();
function IEVersion() {
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
	var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
	var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
	var oinput=document.getElementsByTagName("input")
	var btn=document.getElementsByTagName("button")[0]
	if(isIE) {
		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
		reIE.test(userAgent);
		var fIEVersion = parseFloat(RegExp["$1"]);
		if(fIEVersion<10){
			document.getElementById("ie").style.display="block"
				for (var i = 0; i < oinput.length; i++) {
					oinput[i].setAttribute("disabled","disabled")

				}
			btn.setAttribute("disabled","disabled")		
			return
		}else{
			document.getElementById("ie").style.display="none"
				for (var i = 0; i < $("input").length; i++) {
					$("input").eq(i).disabled = "";
				}
			$("button").eq(0).disabled = "";	
			return 
		}  
	} else if(isEdge) {
		return 'edge';//edge
	} else if(isIE11) {
		return 11; //IE11  
	}else{
		return -1;//不是ie浏览器
	}
}
var login = document.getElementById("box");
login.style.height = document.documentElement.clientHeight + 'px';
$(window).resize(function() {
	login.style.height = document.documentElement.clientHeight + 'px'
});