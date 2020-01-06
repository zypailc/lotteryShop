/*$(function(){
    $(document).ajaxSend(function(e, xhr, options) {
        ////options.data.put({"access_token":"bae582fc-f5b5-4cad-83d4-2c821a4a6848"});
        var dataStr = options.data;
        if(dataStr){
            dataStr = dataStr+"&access_token="+"bae582fc-f5b5-4cad-83d4-2c821a4a6848";
        }else{
            dataStr = "access_token="+"bae582fc-f5b5-4cad-83d4-2c821a4a6848";
        }
        options.data = dataStr;
        xhr.setRequestHeader("access_token", "bae582fc-f5b5-4cad-83d4-2c821a4a6848");
    });
})*/
$(function(){
    var languageType = getLanguage();
    if(languageType != 'zh'){
        languageType = 'en'
    }
    infoContentNotice(languageType);
    hero_side_text_container_button_click();
    hero_side_text_container_niticeHear_click();
})
//延時跳轉
function set_Time_out(url,time){
    if(!url){
        return;
    }
    if(!time){
        time = 5000
    }
    setTimeout(function () {
        window.location.href=url;
    }, time);
}
var layuiopen;
function layui_open(conent){
    layer.close(layuiopen);
    layuiopen = layer.open({
        type: 1,
        shade: false,
        title: false,
        content: "<p style='margin: 5px 20px;text-align: center;'>"+conent+"</p>",
    });
}

function hero_side_text_container_niticeHear_click(){
    $(".notice_head").click(function(){
        $(".hero_side_text_container_button").show();
        $(".hero_side_text_container").hide();
    })
}
function hero_side_text_container_button_click(){
    $(".hero_side_text_container_button").click(function(){
        $(".hero_side_text_container_button").hide();
        $(".hero_side_text_container").show();
        updateNoticeRead();
    })
}
function updateNoticeRead(){
    $.ajax({
        url:'/api/base/v1/member/updateNoticeIsRead',
        type:'get',
        dataType:"json",
        success:function (result) {

        }
    });
}

function infoContentNotice(languageType){
    $.ajax({
        url:'/apiauthorization/base/authorization/v1/baseInfo/findNotice',
        type:'get',
        data:{"languageType":languageType},
        dataType:"json",
        success:function (result) {
            if(result.length > 0){
                var info = $(".notice_content");
                info.html("");
                $.each(result,function(index,data){
                    var p = "<p>"+(index + 1)+"."+data.content+"</p>";
                    info.append(p);
                })
            }
        }
    })
}

