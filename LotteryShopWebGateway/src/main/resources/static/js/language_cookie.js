var name = "somoveLanguage";
/*server*/
var ctx = window.document.location.href.substring(0, window.document.location.href.indexOf(window.document.location.pathname));
function chgLang() {
    //var value = $("#ddlSomoveLanguage").children('option:selected').val();
    var value = getCookie(name) || "en";
    SetCookie(name, value);
    location.reload();
}
function SetCookie(name, value ,setDay) {
    if(!setDay) {
        setDay = 30; //此 cookie 将被保存 30 天
    }
    var exp = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + setDay * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
//删除谷歌翻译插件存储的cookie值
function delCookie(name) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() - Days * 24 * 60 * 60 * 30);

    //这里一定要注意，如果直接访问ip的话，不用注明域名domain
    //但访问的是域名例如www.baidu.com时，翻译插件的cookie同时存在于一级和二级域名中
    //即删除翻译cookie时要把domain=www.baidu.com和domain=.baidu.com两个cookie一起删除才行
    var domain = document.domain;
    var domainIsIp = false;
    var dd = domain.split(".");
    if(dd.length==4){
        domainIsIp=true;
    }
    document.cookie = name + "='';path=/;expires="+ exp.toUTCString();
    if(domainIsIp==false){
        domain="."+dd[1]+"."+dd[2];
        document.cookie = name + "='';domain="+domain+";expires="+exp.toGMTString()+";path=/";
    }
}

function getCookie(name)//取cookies函数     
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) {
        return unescape(arr[2])
    } else {
        return null;
    } 
}
/*console.log("getCookie:"+getCookie(name));*/
$(function () {

    var uulanguage = (navigator.language || navigator.browserLanguage).toLowerCase();
    //判断浏览器的语言
    if (uulanguage.indexOf("en") > -1) {
        $("[data-localize]").localize("text", { pathPrefix: ctx + "/lang", language: "en" });
    } else if (uulanguage.indexOf("zh") > -1) {
        $("[data-localize]").localize("text", { pathPrefix: ctx + "/lang", language: "zh" });
    } else {
        //默认显示英文
        $("[data-localize]").localize("text", { pathPrefix: ctx + "/lang", language: "en" });
    };
    if (getCookie(name) != "") {
        if (getCookie(name) == "zh") {
            $("[data-localize]").localize("text", { pathPrefix: ctx + "/lang", language: "zh" });
        }
        if (getCookie(name) == "en") {
            $("[data-localize]").localize("text", { pathPrefix: ctx + "/lang", language: "en" });
        }       
    }
});