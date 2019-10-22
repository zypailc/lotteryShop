//谷歌语言切换插件初始化
function googleTranslateElementInit() {

    new google.translate.TranslateElement(
        {
            //这个是当前页面的原语言，便于插件精确翻译
            pageLanguage: 'en',
            //这个是你需要翻译的语言，比如你只需要翻译成越南和英语，这里就只写en,vi
            //includedLanguages: 'en,zh-TW,hr,cs,da,nl,fr,de,el,iw,hu,ga,it,ja,ko,pt,ro,ru,sr,es,th,vi',
            includedLanguages: 'en,zh-TW,ja',
            //选择语言的样式，这个是面板，还有下拉框的样式，具体的记不到了，找不到api~~
            layout: 2,
            //自动显示翻译横幅，就是翻译后顶部出现的那个，有点丑，这个属性没有用的话，请看文章底部的其他方法
            autoDisplay: false,
            //还有些其他参数，由于原插件不再维护，找不到详细api了，将就了，实在不行直接上dom操作
        },
        'google_translate_element'//触发按钮的id
    );
}