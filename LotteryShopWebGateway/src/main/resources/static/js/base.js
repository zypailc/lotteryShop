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
        //$(".hero_side_text_container_button").show();
        //$(".hero_side_text_container").hide();
    })
}
function hero_side_text_container_button_click(){
    $(".hero_side_text_container_button").click(function(){
        //$(".hero_side_text_container_button").hide();
        //$(".hero_side_text_container").show();
        var width = document.body.clientWidth;
        var area = ["30%","50%"];
        if(width < 991){
            width = document.body.clientWidth;
            area = ["95%","50%"]
        }
        layer.open({
            type: 1,
            title: false,
            shadeClose: true,
            area:area,
            content: $(".div_margin_notice").html()
        });
        $(".notice_ul_li").click(function(){
            var li = $(this);
            var title = li.find(".notice_ul_li_title").html();
            var content = li.find(".notice_ul_li_title").attr("datacontent");
            var detail = $(this).parent().parent().parent().find(".div_content_notice_detail");
            console.log(detail);
            console.log(detail.find(".div_content_detail").find(".notice_title"));
            console.log(detail.find(".div_content_detail").find(".notice_detail"));
            detail.find(".div_content_detail").find(".notice_title").html(title);
            detail.find(".div_content_detail").find(".notice_detail").html(content);
            console.log( $(this).parent().parent().parent().find(".div_content_notice_list"));
            console.log( $(this).parent().parent().parent().find(".div_content_notice_detail"));
            $(this).parent().parent().parent().find(".div_content_notice_list").hide();
            $(this).parent().parent().parent().find(".div_content_notice_detail").show();
            updateNoticeRead($(this).attr("dataid"));
        });
        $(".notice_detail_back").click(function(){
            var notice = $(this).parent().parent().parent().parent();
            console.log(notice);
            notice.find(".div_content_notice_list").show();
            notice.find(".div_content_notice_detail").hide();
        });
    })
}

function updateNoticeRead(id){
    $.ajax({
        url:'/api/base/v1/member/updateNoticeIsRead',
        type:'get',
        data:{"id":id},
        dataType:"json",
        success:function (result) {

        }
    });
}

function infoContentNotice(languageType){
    console.log("languageType:"+languageType);
    $.ajax({
        url:'/apiauthorization/base/authorization/v1/baseInfo/findNotice',
        type:'get',
        data:{"languageType":languageType},
        dataType:"json",
        success:function (result) {
            if(result.length > 0){
                var info = $(".notice_content");
                info.html("");
                var ul = $(".notice_list_ul");
                ul.html("");
                $.each(result,function(index,data){
                    console.log(data);
                    var li =
                        '<li class="notice_ul_li" dataId="'+data.id+'">';
                        if(data.type != '0'){
                         li +=   '<p class="notice_li_img"><img src="../images/notice/d.png"></p>';
                        }
                        li +='<p class="notice_ul_li_time">'+data.createTime+'</p>'+
                            '<p class="notice_ul_li_title" dataContent="'+data.content+'">'+data.title+'</p>'+
                            '</li>';
                    ul.append(li);
                    console.log("我是")
                })

                console.log("你好，我玩了");
            }
        }
    })
}

