$(function (){



    //初始化方法
    var li = $(".wallet_li");
    li.click();
   /* $(".card-1_scroll").scroll(function(){
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
    });*/
   $(".walletETH").scroll(function(){
       var viewH = $(this).height();
       var contentH = $(this).get(0).scrollHeight;
       var scrollTop =$(this).scrollTop();
       if(scrollTop/(contentH -viewH) >= 0.95){
           findWalletRecord(false,'walletETH','/api/base/v1/ethWallet/findEthRecord')
       }
   });

    //初始化头像列表
    div_head_images();

    $(".li_wallet_module").click(function(){
        var type = $(this).attr("data-class") || "";
        if(type == 'wallet_bind'){
            $(location).prop('href','../web/personalWalletBind?'+"type="+type);
        }else{
            $(location).prop('href','../web/personalWalletBase?'+"type="+type);
        }
    });

    //昵称标签超出长度显示省略号
    var span_title = $(".span_title").html();
    if (span_title.length > 3) {
        //截取固定长度
        var span_title = span_title.substring(0, 3);
        //为非隐藏<span>标签赋值--截取固定长度
        $(".span_title").text(span_title+"...");
    } else {
        $(".span_title").text(span_title);
    }

    //屏幕分辨率小于991的初始化方法
    $(".personal_phone").click(function(){
        var type = $(this).attr("data-file") || "";
        if(type == 'generalize'){
            $(location).prop('href','../web/personalGeneralizePhone');
        }
        if(type == 'lottery_record'){
            $(location).prop('href','../web/personalPhoneType?'+"type="+type);
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
        url: "/api/base/v1/lsimage/imageType?type=1",
        dataType: "json",
        success:function (result){
            $.each(result,function(index,data){
                var div_image = '<div  class="head_img head_img_width">' +
                    '<img class="img_change" onclick="img_change(this)" src="/api/base/v1/lsimage/getImg?id='+data.id+'">' +
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
            if(data.code == '200'){//success
                location.reload();
            }else {
                layui_open(data.msg);
            }
        }
    })
}
// wallet update
function update_confirm(){
    var bAddress = $("input[name=update_bAddress]").val() || "";
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

//init gengeralize record
function gengeralizeMemberInit(classProperty){
    var gengeralizeMember = $("."+classProperty);
    var currentPage = gengeralizeMember.attr("currentPage") || 1;
    var pageSize = gengeralizeMember.attr("pageSize") || 20;
    var isFind = gengeralizeMember.attr("isFind") || 'true';
    var ul = gengeralizeMember.find("ul");
    $.ajax({
        url: '/api/base/v1/member/findGeneralizeMemberList',
        type: 'post',
        dataType: "json",
        success: function (list) {
            isFind = gengeralizeMember.attr("isFind") || 'true';
            if(isFind == 'true') {
                if (list.length < pageSize) {
                    gengeralizeMember.attr("isFind", 'false')
                }
                $.each(list, function (index, data) {
                    var li = '<li>' +
                        '<div>' +
                        '<p><span class="span_title">' + data.memberName + '</span></p>' +
                        '<p>' +
                        '<span>' + data.email + '</span>' +
                        '</p>' +
                        '</div>' +
                        '</li>';
                    if (index != 0) {
                        ul.append("<hr class='hrEth'>" + li);
                    } else {
                        ul.append(li);
                    }
                })
                gengeralizeMember.attr("currentPage", (parseInt(currentPage) + 1));
                gengeralizeMember.attr("pageSize", pageSize);
                if (list.length < pageSize) {
                    ul.append("<hr class='hrEth'>" + "<li class='li_eth_come' dataTag='noMore'>No More</li>")
                }
            }
        }
    })
}

//init ETH record
function findETHWallet(classProperty,url){
    $.ajax({
        url:url,
        type: 'post',
        dataType: "json",
        success: function (result) {
            console.log(result)
        }
    })
}
function findWalletRecord(flag,classProperty,url){
    var walletETH = $("."+classProperty);
    var currentPage = walletETH.attr("currentPage") || 1;
    var pageSize = walletETH.attr("pageSize") || 20;
    var isFind = walletETH.attr("isFind") || 'true';
    var ul = walletETH.find("ul");
    if(flag){
        ul.html("");
    }
    if(isFind == 'true') {
        $.ajax({
            url: url,
            type: 'post',
            data: {"currentPage": currentPage, "pageSize": pageSize, "start": '1,2'},
            dataType: "json",
            success: function (result) {
                var dataInfo = result.extend.data;
                var record = dataInfo.records;
                isFind = walletETH.attr("isFind") || 'true';
                if(isFind == 'true') {
                    if(record.length < pageSize){
                        walletETH.attr("isFind",'false')
                    }
                    $.each(record, function (index, data) {
                        var li = '<li>';
                        if (data.type == '0') {
                            li = "<li class='li_come'>";
                        }
                        if (data.type == '1') {
                            li = "<li class='li_comeInto'>";
                        }
                        li +=
                            '<div>' +
                            '<p><span>' + data.statusTime + '</span></p>' +
                            '<p>' +
                            '<span class="span_right" >' + data.amount + '</span>' +
                            '<span class="span_right" >' + data.balance + '</span>';
                        if(classProperty == 'walletETH'){
                            li += '<span>' + data.gasFee + '</span>';
                        }
                        li +=    '</p>' +
                            '</div>' +
                            '</li>';
                        if (flag && index == 0) {
                            ul.append(li);
                        } else {
                            ul.append("<hr class='hrEth'>" + li);
                        }
                    });
                    walletETH.attr("currentPage", (parseInt(currentPage) + 1));
                    walletETH.attr("pageSize", pageSize);
                    if (record.length < pageSize) {
                        ul.append("<hr class='hrEth'>" + "<li class='li_eth_come' dataTag='noMore'>No More</li>")
                    }
                }
            }
        })
    }
}
