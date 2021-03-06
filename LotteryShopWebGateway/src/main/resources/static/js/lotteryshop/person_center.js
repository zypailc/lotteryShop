var enTitle = new Array("Sum value","Three same number through selection","Three with a single","Three different no","Three companies through selection","Two identical check","Two on the same sign","The two different");
var zhTitle = new Array("和值","三同号通选","三同号单选","三不同号","三连号通选","二同号复选","二同号单选","二不同号");
$(function(){
    $(".search_lottery_record").click(function(){
        findLotteryRecord(true,"lottery_record");
    });
    $(".search_lottery_record_phone").click(function(){
        findLotteryRecord(true,"lottery_record_phone");
    });
    var languageType = getLanguage();
    if(languageType == 'zh'){
        $(".rules_particulars_div_zh").show();
        $(".rules_particulars_div_en").hide();
    }else {
        $(".rules_particulars_div_zh").hide();
        $(".rules_particulars_div_en").show();
    }

    $(".pm_rule_open_layer").click(function () {
        var content = $(this).attr("content");
        var title = $(this).find(".card-title").find("a").html();
        layer.open({
            type: 1,
            title: false,
            skin: 'ifarmecss',
            shadeClose: true,
            area:["40%","auto"],
            content:$(".wallet_rule_div").html()
        });
    })
    $(".di_rule_open_layer").click(function () {
        var content = $(this).attr("content");
        var title = $(this).find(".card-title").find("a").html();
        layer.open({
            type: 1,
            title: false,
            shadeClose: true,
            area:["40%","auto"],
            content:$(".di_rule").html()
        });
    })

})
// head portrait layer
var img_head_layer;
function changePicture(){
    div_head_images();
    img_head_layer = layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area:['400px','520px'],
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
    var languageType = getLanguage();
    if(!src){
        if(languageType == 'zh'){
            layer.msg("请先选择角色 !");
        }else {
            layer.msg("Please select the avatar first !");
        }
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

//Geemptyt a list of head portrait
function div_head_images(){
    var divImages = $(".div_head_images");
    divImages.empty();
    $.ajax({
        type: "post",
        url: "/api/base/v1/lsimage/imageType?type=1",
        dataType: "json",
        async:false,
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
    var languageType = getLanguage();
    if(bAddress == ""){
        if(languageType == 'zh'){
            layer.msg("请输入您的钱包地址 !");
        }else {
            layer.msg("Please enter your wallet address !");
        }
        return "";
    }
    if(paymentCode == ""){
        if(languageType == 'zh'){
            layer.msg("请输入您的密码 !");
        }else {
            layer.msg("Please enter your password !");
        }
        return;
    }
    if(paymentCodeConfirm == ""){
        if(languageType == 'zh'){
            layer.msg("请输入确认密码 !");
        }else {
            layer.msg("Please enter your confirm password!");
        }
        return;
    }
    if(paymentCode != paymentCodeConfirm){
        if(languageType == 'zh'){
            layer.msg("密码与确认码不一致 !");
        }else {
            layer.msg("The password is inconsistent with the confirmation code !");
        }
        return;
    }
    var index = layer.load();
    $.ajax({
        method: "post",
        url: "/api/base/v1/member/bindWallet",
        data:{"bAddress":bAddress,"paymentCode":paymentCode},
        dataType: "json",
        success:function (data){
            layer.close(index);
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
        layui_open("Please enter your wallet address !");
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
            if(data.code == '500'){
                layui_open(data.msg);
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
    var isRepetition = gengeralizeMember.attr("isRepetition") || "true";
    var ul = gengeralizeMember.find("ul");
    var li = $("."+classProperty).find("ul").find("li:first-child");
    if(flag){
        ul.html("");
        ul.append(li);
        gengeralizeMember.attr("currentPage","1");
        gengeralizeMember.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
    }
    if(isRepetition == 'true'){
        gengeralizeMember.attr("isRepetition",'false');
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
                        new_li.find(".generalizePersonalRecordCreate").html(data.createTime);
                        new_li.show();
                        ul.append(new_li);
                    })
                    gengeralizeMember.attr("isRepetition",'true');
                    gengeralizeMember.attr("currentPage", (parseInt(currentPage) + 1));
                    gengeralizeMember.attr("pageSize", pageSize);
                    if (list.length < pageSize) {
                        ul.append("<li class='li_eth_come' style='text-align: center;' dataTag='noMore'>No More</li>")
                    }
                }
            }
        })
    }
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
                    walletdetail = $(".wallet_address_self_eth_withdrawCash");
                    console.log(walletdetail.find(".ethRatio").parent().html());
                    console.log(dataInfo);
                    console.log(dataInfo.ethRatio);
                    walletdetail.find(".ethRatio").html(dataInfo.ethRatio);
                }
                if(classProperty == 'walletPutMoney'){
                    ethHead = $(".dlb_record");
                    var rule = $(".wallet_rule");
                    rule.find(".day_pm").html(dataInfo.day);
                    rule.find(".day_pm_1").html(dataInfo.day1);
                    rule.find(".eth_pm").html(dataInfo.eth);
                    rule.find(".level_pm").html(dataInfo.level);
                    rule.find(".list_pm").html(dataInfo.list);
                }
                if(classProperty == 'walletFLAT'){
                    ethHead = $(".lsb_record");
                    walletdetail = $(".wallet_address_self_flat_withdrawCash");
                }
                if(classProperty == 'walletGdEth'){
                    ethHead = $(".gdEth_record");
                    var rule = $(".di_rule");
                    rule.find(".day1_di").html(dataInfo.day1);
                    rule.find(".eth_di").html(dataInfo.eth);
                    rule.find(".list_di").html(dataInfo.list);
                }
                ethHead.find(".wallet_span_total").find("input").val(dataInfo.total);
                ethHead.find(".wallet_span_total").attr("dataValue",dataInfo.total);
                ethHead.find(".balance").find("input").val(" : "+dataInfo.balance);
                ethHead.find(".balance").attr("dataValue",dataInfo.balance);
                ethHead.find(".freeze").find("input").val(" : "+dataInfo.freeze);
                ethHead.find(".freeze").attr("dataValue",dataInfo.freeze);
               /* if(classProperty == 'walletETH' || classProperty == 'walletFLAT'){
                    walletdetail.find(".wallet_span_total").find("input").val(dataInfo.total);
                    walletdetail.find(".wallet_span_total").attr("dataValue",dataInfo.total);
                    walletdetail.find(".balance").find("input").val(dataInfo.balance);
                    console.log(walletdetail.find(".balance").find("input"));
                    walletdetail.find(".balance").attr("dataValue",dataInfo.balance);
                    walletdetail.find(".freeze").find("input").val(dataInfo.freeze);
                    walletdetail.find(".freeze").attr("dataValue",dataInfo.freeze);
                }*/
            }
        }
    })
}
function findWalletRecord(flag,classProperty,url){
    var walletETH = $("."+classProperty);
    var currentPage = walletETH.attr("currentPage") || 1;
    var pageSize = walletETH.attr("pageSize") || 20;
    var isFind = walletETH.attr("isFind") || 'true';
    var isRepetition = walletETH.attr("isRepetition") || 'true';
    var ul = walletETH.find("ul");
    var li = $("."+classProperty).find("ul").find("li:first-child");
    var languageType = getLanguage();
    if(languageType != 'zh'){
        languageType = 'en';
    }
    if(flag){
        ul.html("");
        ul.append(li);
        walletETH.attr("currentPage","1")
        walletETH.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
    }
    if(isRepetition == 'true') {
        walletETH.attr("isRepetition","false");
        $.ajax({
            url: url,
            type: 'post',
            data: {"currentPage": currentPage, "pageSize": pageSize, "status": '0,1,2'},
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
                            new_li.removeClass("li_come_0")

                        }
                        if(data.type == '1'){
                            new_li.find(".expend").hide();
                            new_li.find(".recorded").show();
                            new_li.addClass("li_come_0")
                        }
                        var languageStr = 'r_'+languageType+'value';
                        new_li.find(".divType").html(data[languageStr])
                        new_li.find(".statusTime").html(data.statusTime);
                        new_li.find(".expendOrReceipts").html(data.amount);
                        new_li.find(".balance1").html(data.balance);

                        if(languageType == 'en'){
                            new_li.find(".balanceStr").html("balance : ");
                            if(data.status == '0'){
                                new_li.find(".statusStr").html("being processed");
                            }else if(data.status == '1'){
                                new_li.find(".statusStr").html("success");
                            }else if(data.status == '2'){
                                new_li.find(".statusStr").html("failure");
                            }else {
                                new_li.find(".statusStr").html("other");
                            }
                        }else {
                            new_li.find(".balanceStr").html("余额 : ");
                            if(data.status == '0'){
                                new_li.find(".statusStr").html("处理中");
                            }else if(data.status == '1'){
                                new_li.find(".statusStr").html("成功");
                            }else if(data.status == '2'){
                                new_li.find(".statusStr").html("失败");
                            }else {
                                new_li.find(".statusStr").html("其他");
                            }
                        }

                        var gas = typeof(data.gasFee);
                        if(gas && gas != 'undefined'){
                            new_li.find(".ethGasStrInfo").html(data.gasFee);
                            if(languageType == 'en'){
                                new_li.find(".ethGasStr").html("gas : ");
                            }else {
                                new_li.find(".ethGasStr").html("燃气费 : ");
                            }

                        }else {
                            new_li.find(".ethGas").hide();
                        }
                        new_li.show();
                        ul.append( new_li);
                    });
                    walletETH.attr("isRepetition","true");
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
    var isRepetition = walletETH.attr("isRepetition") || 'true';
    var ul = walletETH.find("ul");
    var startTime = $(".startDateInput").val() || "";
    var endTime = $(".endDateInput").val() || "";
    var type = $(".lotteryTypeSelect").val() || "";
    var winning = $(".lotteryWinningSelect").val() || "";
    var languageType = getLanguage();
    if(languageType != 'zh'){
        languageType = 'en';
    }
    var li = $("."+classProperty).find("ul").find("li:first-child");
    if(flag){
        ul.html("");
        ul.append(li);
        walletETH.attr("currentPage","1")
        walletETH.attr("isFind","true");
        isFind = "true";
        currentPage = 1;
    }
    if(isRepetition == 'true') {
        walletETH.attr("isRepetition","false");
        $.ajax({
            url: "/api/base/v1/member/findLotterPurchaseResord",
            type: 'post',
            data: {"currentPage": currentPage, "pageSize": pageSize, "type":type,"startTime":startTime,"endTime":endTime,"winning":winning},
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
                            new_li = $("."+classProperty).find("ul").find("li:first-child").clone();
                        }
                        if(data.lotteryType == '4'){
                            new_li.find(".lotteryType").html("lotteryA")
                        }
                        if(languageType == 'zh'){
                            if(data.lotteryType == '1'){
                                new_li.find(".lotteryType").html("竞猜-B1")
                            }
                            if(data.lotteryType == '2'){
                                new_li.find(".lotteryType").html("竞猜-B2")
                            }
                            if(data.lotteryType == '3'){
                                new_li.find(".lotteryType").html("竞猜-B3")
                            }
                            if(data.isLuck == '1'){
                                new_li.find(".isLuckRecode").html("中奖");
                            }else {
                                new_li.find(".isLuckRecode").html("未中奖");
                            }

                        }else {
                            if(data.lotteryType == '1'){
                                new_li.find(".lotteryType").html("GUESS-B1")
                            }
                            if(data.lotteryType == '2'){
                                new_li.find(".lotteryType").html("GUESS-B2")
                            }
                            if(data.lotteryType == '3'){
                                new_li.find(".lotteryType").html("GUESS-B3")
                            }
                            if(data.isLuck == '1'){
                                new_li.find(".isLuckRecode").html("Winning");
                            }else {
                                new_li.find(".isLuckRecode").html("No winning");
                            }

                        }
                        if(data.type){
                            if(languageType == 'zh'){
                                new_li.find(".luckNumStr").html("["+zhTitle[data.type-1]+"]")
                                new_li.find(".selfLuckNum").html(data.zhTitle);
                            }else {
                                new_li.find(".luckNumStr").html("["+zhTitle[data.type-1]+"]")
                                new_li.find(".selfLuckNum").html(data.enTitle);
                            }

                        }else {
                            if(languageType == 'zh'){
                                new_li.find(".luckNumStr").html("[幸运号]")
                            }else {
                                new_li.find(".luckNumStr").html("[Luck num]")
                            }
                            new_li.find(".selfLuckNum").html(data.selfLuckNum);
                        }

                        new_li.find(".lotteryIssue").html("#"+data.issueNum);
                        new_li.find(".luckNum").html(data.luckNum);
                        new_li.find(".luckTotal").html(data.luck_total || 0);
                        new_li.find(".startTime").html(data.startTime);
                        new_li.find(".endTime").html(data.endTime);
                        new_li.show();
                        ul.append( new_li);
                    });
                    walletETH.attr("isRepetition","true");
                    walletETH.attr("currentPage", (parseInt(currentPage) + 1));
                    walletETH.attr("pageSize", pageSize);
                    if (record.length < pageSize) {
                        if(flag && record.length == 0){
                            li.hide();
                            ul.append(li);

                        }
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
            /*personalWalletCenter.find(".walletTotalEth").html(info.ethTotal);
            personalWalletCenter.find(".ethTotalToUsd").html(info.ethTotalToUsd);
            personalWalletCenter.find(".walletEth").html(info.eth);
            personalWalletCenter.find(".ethToUsd").html(info.ethToUsd);
            personalWalletCenter.find(".walletGold").html(info.lsb);
            personalWalletCenter.find(".goldToEth").html(info.lsbToEth);
            personalWalletCenter.find(".goldToUsd").html(info.lsbToUsd);
            personalWalletCenter.find(".walletPutMoney").html(info.dlb);
            personalWalletCenter.find(".putMoneyToEth").html(info.dlbToEtb);
            personalWalletCenter.find(".putMoneyToUsd").html(info.dlbToUsd);*/
            personalWalletCenter.find(".walletTotalEth").find("input").val(info.ethTotal+"ETH");
            personalWalletCenter.find(".ethTotalToUsd").find("input").val("≈$"+info.ethTotalToUsd);
            personalWalletCenter.find(".walletEth").find("input").val(info.eth);
            personalWalletCenter.find(".ethToUsd").find("input").val("≈$"+info.ethToUsd);
            $(".wallet_span_total_eth_usd").find("input").val("≈$"+info.ethToUsd);
            personalWalletCenter.find(".walletGold").find("input").val(info.lsb);
            personalWalletCenter.find(".goldToEth").find("input").val("≈ETH"+info.lsbToEth);
            personalWalletCenter.find(".goldToUsd").find("input").val("≈$"+info.lsbToUsd);
            $(".wallet_span_total_lsb_usd").find("input").val("≈$"+info.lsbToUsd);
            personalWalletCenter.find(".walletPutMoney").find("input").val(info.dlb);
            personalWalletCenter.find(".putMoneyToEth").find("input").val("≈ETH"+info.dlbToEtb);
            personalWalletCenter.find(".putMoneyToUsd").find("input").val("≈$"+info.dlbToUsd);
            $(".wallet_span_total_dlb_usd").find("input").val("≈$"+info.dlbToUsd);
            personalWalletCenter.find(".walletGdEth").find("input").val(info.gdEth);
            personalWalletCenter.find(".gdEthToUsd").find("input").val("≈$"+info.gdEthToUsd);
            personalWalletCenter.find(".wallet_span_total_gd_usd").find("input").val("≈$"+info.gdEthToUsd);
            $(".wallet_span_total_gd_usd").find("input").val("≈$"+info.gdEthToUsd);
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
    var languageType = getLanguage();
    var  title = "payment code";
    if(languageType == 'zh'){
        title = '支付密碼';
    }
    lay_play_code = layer.open({
        type: 1,
        title:title,
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        content: $("#playPassword").html()
    });
}

function withdrawCashEth(e){
    var obj = $(e);
    var propertyType = obj.attr("propertyType") || "-1";
    var propertyObj = "";
    var propertyObjHead = "";
    var languageType = getLanguage();
    if(propertyType == "-1"){
        if(languageType == 'zh'){
            layer.msg("操作中出现错误，请在操作前刷新 !");
        }else {
            layer.msg("There is an error in the operation, please refresh before operation !");
        }
        return;
    }
    if(propertyType == "1"){//Eth
        propertyObj = "wallet_address_self_eth_withdrawCash";
        propertyObjHead = "eth_record";
    }
    if(propertyType == "2"){//Lsb to Eth
        propertyObj = "wallet_address_self_flat_withdrawCash";
        propertyObjHead = "lsb_record";
    }
    if(propertyType == "3"){// Eth To Lsb
        propertyObj = "wallet_address_self_flat_recharge";
        propertyObjHead = "lsb_record";
    }
    var balance = $("."+propertyObjHead).find(".balance").attr("dataValue") || "";
    var extractTheNumber = $("."+propertyObj).find("input").val() || "0";
    if(!extractTheNumber || extractTheNumber == '0'){
        if(languageType == 'zh'){
            layer.msg("请输入数量 !");
        }else {
            layer.msg("Please enter quantity !");
        }
        return;
    }
    if(!balance){
        if(languageType == 'zh'){
            layer.msg("可提取的数未0 !");
        }else {
            layer.msg("The extractable number is zero!");
        }
        return;
    }
    if((propertyType == "1" || propertyType == "2")){
        balance = parseFloat(balance);
        extractTheNumber = parseFloat(extractTheNumber);
        if(extractTheNumber > balance){
            if(languageType == 'zh'){
                layer.msg("提取的数不得大于可提取的数 !");
            }else {
                layer.msg("The extracted number shall not be greater than the extractable number !");
            }
            return;
        }
    }
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
    var dataJson = {};
    dataJson = {"num":extractTheNumber};
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
    if(operationType == '3'){//ETH TO Lsb
        url = "/api/base/v1/baseWallet/withdrawCashEthToLsb";
        var num = $(".wallet_address_self_flat_recharge").find("input").val() || "";
        dataJson = {"num":num,"playCode":playCode};
    }
    walletOperation(url,dataJson);
}

function walletOperation(url,dataJson){
   var loadIndex =  layer.load();
    $.ajax({
        url:url,
        type:"get",
        data:dataJson,
        dataType:"json",
        success:function (result){
            layer.close(loadIndex);
            if(result.code != 200){
                layui_open(result.msg);
            }else {
                layer.msg(result.msg);
                layer.closeAll();
            }
            if(url == "/api/base/v1/baseWallet/withdrawCashEth"){
                findETHWallet(  'walletETH','/api/base/v1/ethWallet/findEthWallet');
                findWalletRecord(true,'walletETH','/api/base/v1/ethWallet/findEthRecord');
            }
            if(url == "/api/base/v1/baseWallet/withdrawCashEthToLsb" || url == "/api/base/v1/baseWallet/withdrawCashLsbToEth"){
                findETHWallet( 'walletFLAT','/api/base/v1/lsbWallet/findLsbWallet');
                findWalletRecord(true,'walletFLAT','/api/base/v1/lsbWallet/findLsbRecord');//lsb
            }
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

function generalizeInitRegisterUrl(){
    $.ajax({
        url:"/api/base/v1/member/generalizeInitRegisterUrl",
        type:"get",
        dataType:"json",
        success:function (result){
            if(result.code == '200'){
                $(".generalizeEwmUrl").html(result.extend.data);
            }
        }
    })
}

function updatePassword(e,type){
    var obj = $(e).parent().parent();
    var oldPassword = obj.find("input[name=oldPassword]").val() || "";
    var newPassword = obj.find("input[name=newPassword]").val() || "";
    var confirmPassword = obj.find("input[name=confirmPassword]").val() || "";
    var languageType = getLanguage();
    if(languageType != 'zh'){
        languageType = 'en';
    }
    if(!oldPassword){
        if(languageType == 'zh'){
            layui_open("Please enter the original password !");
        }else {
            layui_open("请输入原密码 !");
        }
        return ;
    }
    if(!newPassword){
        if(languageType == 'zh'){
            layui_open("Please enter your new password !");
        }else {
            layui_open("请输入新密码 !");
        }
        return ;
    }
    if(!confirmPassword){
        if(languageType == 'zh'){
            layui_open("Please enter the confirmation code !");
        }else {
            layui_open("请确认输入密码 !");
        }
        return ;
    }
    $.ajax({
        url:"/api/base/v1/member/updatePassword",
        type:"get",
        data:{"oldPassword":oldPassword,"newPassword":newPassword,"confirmPassword":confirmPassword,"type":type},
        dataType:"json",
        success:function (result){
            if(result.code == '200'){
                layer.msg(result.extend.data);
                if(type == '1'){
                    //退出登陆
                }
            }else if(result.code == '500'){
                layui_open(result.msg);
            }
        }
    })
}

function updateMoneyIsView(isView){
    $.ajax({
        url:"/api/base/v1/member/updateMoneyIsView",
        type:"get",
        data:{"isView":isView},
        dataType:"json",
        success:function (result){

        }
    })
}

function infoContentGenerlize(languageType){
    $.ajax({
        url:'/apiauthorization/base/authorization/v1/baseInfo/infoContentGenerlize',
        type:'get',
        data:{"languageType":languageType},
        dataType:"json",
        success:function (result) {
            if(result.length > 0){
                var info = result[0];
                //$(".generalizeContent").html(info.title);
                $(".generalizeContent").html(info.content);
            }
        }
    })
}

function findGeneralizeStatistics() {
    var startTime = $(".generalizeStartTime").val() || "";
    var endTime = $(".generalizeEndTime").val() || "";
    $.ajax({
        url:'/api/base/v1/member/findGeneralizeStatistics',
        type:'get',
        data:{"startTime":startTime,"endTime":endTime},
        dataType:"json",
        success:function (result) {
            var data = result.extend.data;
            var generalize = data.generalize;
            var generalizePerson = data.generalizePerson;
            if(generalize.length == 0){
                $(".generalize_statistics_ul").find(".ztotal").val(0);
                $(".generalize_statistics_ul").find(".gtotal").val(0);
            }
            $.each(generalize,function(index,info){
                var obj = $(".generalize_statistics_"+(index+1));
                obj.find(".ztotal").val(info.zTotal);
                obj.find(".gtotal").val(info.gTotal);
            })
            $.each(generalizePerson,function(index,info){
                var obj = $(".generalize_statistics_"+(index+1));
                obj.find(".personNum").val(info.personNum);
            })
        }
    })
}