$(function(){
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
})