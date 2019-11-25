$(function (){
    //初始化方法
    var li = $(".wallet_li");
    li.click();
    $(".card-1_scroll").scroll(function(){
        var $this =$(this),
            viewH =$(this).height(),//可见高度
            contentH =$(this).get(0).scrollHeight,//内容高度
            scrollTop =$(this).scrollTop();//滚动高度
        if(scrollTop/(contentH -viewH) >= 0.95){ //当滚动到距离底部5%时
            var li = '<hr style="height: 1px; border: none; border-top: 1px dashed #ddd;" />'+
                '<li>'+
                '<div>'+
                '<p><span>Lottery type</span><span>#'+contentH+'</span></p>'+
                '<p>'+
                '<span>2018.01.01 00:00:00</span>'+
                '<span>2 4 6 3 2 7</span>'+
                '<span>2 5 6 0 2 8</span>'+
                '<span>3ETH</span>'+
                '</p>'+
                '</div>'+
                '</li>';
            $(".ul_personal_lottery").append(li);
        }
    });

    //初始化头像列表
    div_head_images();

    $(".li_wallet_module").click(function(){
        var type = $(this).attr("data-class") || "";
        if(type == 'wallet_bind'){
            $(location).prop('href','./walletBind?'+"type="+type);
        }else{
            $(location).prop('href','./walletBase?'+"type="+type);
        }
    });

    //昵称标签超出长度显示省略号
    var span_title = $(".span_title").html();
    if (span_title.length > 15) {
        //截取固定长度
        var span_title = span_title.substring(0, 15);
        //为非隐藏<span>标签赋值--截取固定长度
        $(".span_title").text(span_title+"...");
    } else {
        $(".span_title").text(span_title);
    }

    //屏幕分辨率小于991的初始化方法
    $(".personal_phone").click(function(){
        var type = $(this).attr("data-file") || "";
        if(type){
            $(location).prop('href','./phoneType?'+"type="+type);
        }
    });

    // layer Time initialization
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#startDate'
        });
    });
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#endDate'
        });
    });
})

// head portrait layer
var img_head_layer;
function changePicture(){
    img_head_layer = layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        content: $(".layui_head")
    });
}

//head portrait click
function img_change(e){
    $(".img_change").removeClass("img_change_border");
    $(e).addClass("img_change_border");
}

//update head portrait
function img_change_button(){
    var src = $(".img_change_border").attr("src") || "";
    if(!src){
        layer.msg("Please select the avatar first !");
        return;
    }
    $.ajax({
        type: "get",
        url: "/api/base/v1/lsimage/headPortrait",
        data:{"headPortraitUrl":src},
        dataType: "json",
        success:function (data){
            if(data.code == '200'){//register success
                $(".img_head").attr("src",src);
                layer.close(img_head_layer);
            }
            if(data.code == '500'){//register error

            }
            layer.msg(data.extend.data);
        }
    })
}

//Get a list of head portrait
function div_head_images(){
    var divImages = $(".div_head_images");
    $.ajax({
        type: "post",
        url: "/api/base/v1/lsimage/imageType",
        dataType: "json",
        success:function (result){
            $.each(result,function(index,data){
                var div_image = '<div  class="head_img head_img_width">' +
                    '<img class="img_change" onclick="img_change(this)" src="'+data.url+'">' +
                    '</div>';
                divImages.append(div_image);
            })
        }
    })
}

//wallet create
function create_confirm(){
    var bAddress = $("input[name=bAddress]").val() || "";
    var paymentCode = $("input[name=paymentCode]").val() || "";
    var paymentCodeConfirm = $("input[name=paymentCodeConfirm]").val() || "";
    if(bAddress == ""){
        layer.msg("Please enter your wallet address !");
        return "";
    }
    if(paymentCode == ""){
        layer.msg("Please enter your password !");
        return;
    }
    if(paymentCodeConfirm == ""){
        layer.msg("Please enter your confirm password !");
        return;
    }
    if(paymentCode != paymentCodeConfirm){
        layer.msg("The password is inconsistent with the confirmation code !");
        return;
    }
    $.ajax({
        method: "post",
        url: "/api/base/v1/member/bindWallet",
        data:{"bAddress":bAddress,"paymentCode":paymentCode},
        dataType: "json",
        success:function (data){
            console.log(data);
            if(data.code == '200'){//success
                location.reload();
            }else {
                layer.msg(data.msg)
            }
        }
    })
}
// wallet update
function update_confirm(){
    var bAddress = $("input[name=update_bAddress]").val() || "";
    console.log("bAddress:"+bAddress);
    if(bAddress == ""){
        layer.msg("Please enter your wallet address !");
        return;
    }
    $.ajax({
        method: "post",
        url: "/api/base/v1/member/updateBindWallet",
        data:{"bAddress":bAddress},
        dataType: "json",
        success:function (data){
            if(data.code == '200'){//success
                location.reload();
            }
        }
    })
}