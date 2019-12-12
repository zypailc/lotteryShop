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

function layui_open(conent){
    layer.open({
        type: 1,
        shade: false,
        title: false,
        content: "<p style='margin: 5px 20px;text-align: center;'>"+conent+"</p>",
    });
}

