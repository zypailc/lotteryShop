$(function(){
    $(".search_lottery_record").click(function(){
        findLotteryRecord(true,"lottery_record");
    });
    $(".search_lottery_record_phone").click(function(){
        findLotteryRecord(true,"lottery_record_phone");
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
function gengeralizeMemberInit(flag,classProperty){
    var gengeralizeMember = $("."+classProperty);
    var currentPage = gengeralizeMember.attr("currentPage") || 1;
    var pageSize = gengeralizeMember.attr("pageSize") || 20;
    var isFind = gengeralizeMember.attr("isFind") || 'true';
    var ul = gengeralizeMember.find("ul");
    var li = $("."+classProperty).find("ul").find("li:first-child");
    if(flag){
        ul.html("");
        gengeralizeMember.attr("currentPage","1");
        gengeralizeMember.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
    }
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
                    var new_li;
                    if(flag && index == 0){
                        new_li = li.clone();
                    }else {
                        new_li = $("."+classProperty).find("ul").find("li:first-child").clone();
                    }
                    new_li.find(".generalizePersonalRecordNikeName").html(data.memberName);
                    new_li.find(".generalizePersonalRecordEmail").html(data.email);
                    ul.append(new_li);
                })
                gengeralizeMember.attr("currentPage", (parseInt(currentPage) + 1));
                gengeralizeMember.attr("pageSize", pageSize);
                if (list.length < pageSize) {
                    ul.append("<li class='li_eth_come' style='text-align: center;' dataTag='noMore'>No More</li>")
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
            if(result.code == 200) {
                var dataInfo = result.extend.data;
                var ethHead;
                var walletdetail;
                if(classProperty == 'walletETH'){
                    ethHead = $(".eth_record");
                    walletdetail = $(".wallet_address_self_eth");
                }
                if(classProperty == 'walletPutMoney'){
                    ethHead = $(".wallet_head");
                }
                if(classProperty == 'walletFLAT'){
                    ethHead = $(".lsb_record");
                    walletdetail = $(".wallet_address_self_flat");
                }
                ethHead.find(".wallet_span_total").html(dataInfo.total);
                ethHead.find(".balance").html(" : "+dataInfo.balance);
                ethHead.find(".freeze").html(" : "+dataInfo.freeze);
                if(classProperty == 'walletETH' || classProperty == 'walletFLAT'){
                    walletdetail.find(".wallet_span_total").html(dataInfo.total);
                    walletdetail.find(".wallet_span_total").attr("dataValue",dataInfo.total);
                    walletdetail.find(".balance").html(dataInfo.balance);
                    walletdetail.find(".balance").attr("dataValue",dataInfo.balance);
                    walletdetail.find(".freeze").html(dataInfo.freeze);
                    walletdetail.find(".freeze").attr("dataValue",dataInfo.freeze);
                }
            }
        }
    })
}
function findWalletRecord(flag,classProperty,url){
    var walletETH = $("."+classProperty);
    var currentPage = walletETH.attr("currentPage") || 1;
    var pageSize = walletETH.attr("pageSize") || 20;
    var isFind = walletETH.attr("isFind") || 'true';
    var ul = walletETH.find("ul");
    var li = $("."+classProperty).find("ul").find("li:first-child");
    if(flag){
        ul.html("");
        walletETH.attr("currentPage","1")
        walletETH.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
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
                        var new_li;
                        if(flag && index == 0){
                            new_li = li.clone();
                        }else {
                            new_li = $("."+classProperty).find("ul").find("li:last-child").clone();
                        }
                        if(data.type == '0'){
                            new_li.find(".recorded").hide();
                            new_li.find(".expend").show();
                        }
                        if(data.type == '1'){
                            new_li.find(".expend").hide();
                            new_li.find(".recorded").show();
                        }

                        new_li.find(".statusTime").html(data.statusTime);
                        new_li.find(".expendOrReceipts").html(data.amount);
                        new_li.find(".balance1").html(data.balance);

                        var gas = typeof(data.gasFee);
                        if(gas && gas != 'undefined'){
                            new_li.find(".ethGasStr").html(data.gasFee);
                        }else {
                            new_li.find(".ethGas").hide();
                        }
                        ul.append( new_li);
                    });
                    walletETH.attr("currentPage", (parseInt(currentPage) + 1));
                    walletETH.attr("pageSize", pageSize);
                    if (record.length < pageSize) {
                        ul.append("<li class='li_eth_come' style='text-align: center;' dataTag='noMore'>No More</li>")
                    }
                }
            }
        })
    }
}

function findLotteryRecord(flag,classProperty){
    var walletETH = $("."+classProperty);
    var currentPage = walletETH.attr("currentPage") || 1;
    var pageSize = walletETH.attr("pageSize") || 20;
    var isFind = walletETH.attr("isFind") || 'true';
    var ul = walletETH.find("ul");
    var startTime = $(".startDateInput").val() || "";
    var endTime = $(".endDateInput").val() || "";
    var type = $(".lotteryTypeSelect").val() || "";
    var li = $("."+classProperty).find("ul").find("li:first-child");
    if(flag){
        ul.html("");
        walletETH.attr("currentPage","1")
        walletETH.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
    }
    if(isFind == 'true') {
        $.ajax({
            url: "/api/base/v1/member/findLotterPurchaseResord",
            type: 'post',
            data: {"currentPage": currentPage, "pageSize": pageSize, "type":type,"startTime":startTime,"endTime":endTime},
            dataType: "json",
            success: function (result) {
                var dataInfo = result.extend.data;
                var record = dataInfo;
                isFind = walletETH.attr("isFind") || 'true';
                if(isFind == 'true') {
                    if(record.length < pageSize){
                        walletETH.attr("isFind",'false')
                    }
                    $.each(record, function (index, data) {
                        var new_li;
                        if(flag && index == 0){
                            new_li = li.clone();
                        }else {
                            new_li = $("."+classProperty).find("ul").find("li:last-child").clone();
                        }
                        if(data.lotteryType == '1'){
                            new_li.find(".lotteryType").html("lotteryA")
                        }
                        new_li.find(".lotteryIssue").html("#"+data.issueNum);
                        new_li.find(".selfLuckNum").html(data.selfLuckNum);
                        new_li.find(".luckNum").html(data.luckNum);
                        new_li.find(".luckTotal").html(data.luck_total || 0);
                        new_li.find(".startTime").html(data.startTime);
                        new_li.find(".endTime").html(data.endTime);
                        ul.append( new_li);
                    });
                    walletETH.attr("currentPage", (parseInt(currentPage) + 1));
                    walletETH.attr("pageSize", pageSize);
                    if (record.length < pageSize) {
                        ul.append("<li class='li_eth_come' style='text-align: center;' dataTag='noMore'>No More</li>")
                    }
                }
            }
        })
    }
}


function findwalletTotal(exchangeRate,personal_wallet_center){
    $.ajax({
        url:"/api/base/v1/member/findWalletTotal",
        type:"get",
        data:{"exchangeRate":exchangeRate},
        dataType:"json",
        success:function (result){
            var info = result.extend.data;
            var personalWalletCenter = $("."+personal_wallet_center);
            personalWalletCenter.find(".walletTotalEth").html(info.ethTotal);
            personalWalletCenter.find(".ethTotalToUsd").html(info.ethTotalToUsd);
            personalWalletCenter.find(".walletEth").html(info.eth);
            personalWalletCenter.find(".ethToUsd").html(info.ethToUsd);
            personalWalletCenter.find(".walletGold").html(info.lsb);
            personalWalletCenter.find(".goldToEth").html(info.lsbToEth);
            personalWalletCenter.find(".goldToUsd").html(info.lsbToUsd);
            personalWalletCenter.find(".walletPutMoney").html(info.dlb);
            personalWalletCenter.find(".putMoneyToEth").html(info.dlbToEtb);
            personalWalletCenter.find(".putMoneyToUsd").html(info.dlbToUsd);
        }
    })
}

function getEthToUsd(personal_wallet_center){
// https://docs.coincap.io/?version=latest#2a87f3d4-f61f-42d3-97e0-3a9afa41c73b
    $.ajax({
        url:"https://api.coincap.io/v2/rates/ethereum",
        type:"get",
        dataType:"json",
        success:function (info){
            var data = info.data;
            findwalletTotal(data.rateUsd,personal_wallet_center);
        }
    })
}

function getWalletConfig(){
    $.ajax({
        url:"/api/base/v1/baseWallet/walletConfig",
        type:"get",
        dataType:"json",
        success:function (result){
            console.log(result);
            var data = result.extend.data;
            var ethToLsb = data.ethToLsb;
            var lsbToEth = data.lsbToEth;
            $(".lsbToEth").html(lsbToEth);
            $(".ethToLsb").html(ethToLsb)
        }
    })
}

function openPlayCode(type){
    $("#playPassword").find("input").attr("operationType",type);
    lay_play_code = layer.open({
        type: 1,
        title:"支付密碼",
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $("#playPassword").html()
    });
}

function withdrawCashEth(e){
    var obj = $(e);
    var propertyType = obj.attr("propertyType") || "-1";
    var propertyObj = "";
    if(propertyType == "-1"){
        layer.msg("There is an error in the operation, please refresh before operation !");
        return;
    }
    if(propertyType == "1"){//Eth
        propertyObj = "wallet_address_self_eth_withdrawCash";
    }
    if(propertyType == "2"){//Lsb to Eth
        propertyObj = "wallet_address_self_flat_withdrawCash";
    }
    if(propertyType == "3"){// Eth To Lsb
        propertyObj = "wallet_address_self_flat_recharge";
    }
    var balance = $("."+propertyObj).find(".balance").attr("dataValue") || "";
    var extractTheNumber = $("."+propertyObj).find("input").val() || "10";
    if(!extractTheNumber){
        layer.msg("Please enter quantity ! ");
        return;
    }
    if((!balance) && (propertyType == "1" || propertyType == "2")){
        layer.msg("The extractable number is zero ! ");
        return;
        balance = parseFloat(balance);
        extractTheNumber = parseFloat(extractTheNumber);
        if(extractTheNumber > balance){
            layer.msg("The extracted number shall not be greater than the extractable number !");
            return;
        }
    }
    console.log("extractTheNumber:"+extractTheNumber);
    if(propertyType == "1" || propertyType == "3"){
        openPlayCode(propertyType);
    }
    if(propertyType == "2"){
        withdrawCashLsbToEth(extractTheNumber);
    }
}

function withdrawCashLsbToEth(extractTheNumber){
    if(!extractTheNumber){
       return;
    }
    var url = "/api/base/v1/baseWallet/withdrawCashLsbToEth"
    var dataJson;
    dataJson.push({"num":extractTheNumber});
    walletOperation(url,dataJson);
}

function paymentCodeConfirm(e){
    var div = $(e).parent().parent();
    var operationType = div.find("input[name=playCode]").attr("operationType") || "";
    var playCode = getPlayCode();
    var dataJson = {};
    var url;
    if(operationType == '1'){//ETH
        url = "/api/base/v1/baseWallet/withdrawCashEth";
        var num = $(".wallet_address_self_eth_withdrawCash").find("input").val() || "";
        dataJson = {"num":num,"playCode":playCode};
    }
    if(operationType == '2'){//ETH TO Lsb
        url = "/api/base/v1/baseWallet/withdrawCashEthToLsb";
        var num = $(".wallet_address_self_flat_recharge").find("input").val() || "";
        dataJson = {"num":num,"playCode":playCode};
    }
    console.log(dataJson);
    walletOperation(url,dataJson);
}

function walletOperation(url,dataJson){
    $.ajax({
        url:url,
        type:"get",
        data:dataJson,
        dataType:"json",
        success:function (result){
            console.log(result);
        }
    })
}

function getPlayCode(){
    var payPasswod = "";
    $(".paymentCode").each(function(index,data){
        var paymentCode_1 = $(data).val() || "";
        if(paymentCode_1){
            payPasswod = paymentCode_1;
        }
    });
    return payPasswod;
}