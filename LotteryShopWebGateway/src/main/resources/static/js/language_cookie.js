﻿var name = "somoveLanguage";
/*server*/
var ctx_1 = window.document.location.href.substring(0, window.document.location.href.indexOf(window.document.location.pathname));
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
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString()+";path=/";
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

function getLanguage(){
    var language_navigator = (navigator.language || navigator.browserLanguage).toLowerCase();
    //中文瀏覽器取的語言簡寫是zh-cn錯誤
    if(language_navigator.indexOf("zh") > -1){
        language_navigator = "zh";

    }
    var language_cookie = getCookie("somoveLanguage") || "";
    if(language_cookie){
        language_navigator = language_cookie;
    }
    return language_navigator;
}
/*console.log("getCookie:"+getCookie(name));*/
$(function () {
    setTimeout(function () {
        var uulanguage = (navigator.language || navigator.browserLanguage).toLowerCase();
        var languageType = "en";
        var cookieLanguage = getCookie(name);
        //判断浏览器的语言
        if (cookieLanguage && cookieLanguage != "") {
            var v = getCookie("googtrans");
            if((!v || v == null || v == "") && cookieLanguage != "zh" && cookieLanguage != "en" ){
                if (uulanguage.indexOf("zh") > -1){
                    cookieLanguage = "zh";
                }else{
                    cookieLanguage = "en";
                }
                SetCookie(name, cookieLanguage);
            }
            if (cookieLanguage == "zh") {
                languageType = "zh";
                $("[data-localize]").localize("text", { pathPrefix: ctx_1 + "/lang", language: "zh" });
            }
            if (cookieLanguage == "en") {
                $("[data-localize]").localize("text", { pathPrefix: ctx_1 + "/lang", language: "en" });
            }
        }else{
            if (uulanguage.indexOf("en") > -1) {
                $("[data-localize]").localize("text", { pathPrefix: ctx_1 + "/lang", language: "en" });
            } else if (uulanguage.indexOf("zh") > -1) {
                languageType = "zh";
                $("[data-localize]").localize("text", { pathPrefix: ctx_1 + "/lang", language: "zh" });
            } else {
                //默认显示英文
                $("[data-localize]").localize("text", { pathPrefix: ctx_1 + "/lang", language: "en" });
            };
            //
            SetCookie(name, languageType);
        }
    },500);
    //TODO Language switch load file due to browser version
});
