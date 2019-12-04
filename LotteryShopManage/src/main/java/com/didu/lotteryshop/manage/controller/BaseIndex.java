package com.didu.lotteryshop.manage.controller;

import com.didu.lotteryshop.common.entity.MIntro;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class BaseIndex {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/index")
    public String index(Model model){
        return "index";
    }

    @RequestMapping("/icons/fontAwesome")
    public String fontAwesome(Model model){
        return "icons/font-awesome";
    }

    @RequestMapping("/icons/glyphicons")
    public String glyphicons(Model model){
        return "icons/glyphicons";
    }

    @RequestMapping("/table/datatable")
    public String datatable(Model model){
        return "table/datatable";
    }

    @RequestMapping("/table/table")
    public String table(Model model){
        return "table/table";
    }

    @RequestMapping("/license")
    public String license(Model model){
        return "license";
    }

    @RequestMapping("/table/tableImg")
    public String tableHeadImg(Model model){
        return "table/table_headImg";
    }

    @RequestMapping("/table/partner")
    public String partner(Model model){
        return "table/table_partner";
    }

    @RequestMapping("/table/external")
    public String external(Model model){
        return "table/table_external";
    }

    @RequestMapping("/table/characteristicProject")
    public String characteristicProject(Model model){
        model.addAttribute("language", MIntro.LANGUAGES_STR);
        return "table/table_characteristic_project";
    }

    @RequestMapping("/table/allocationFunds")
    public String AllocationFunds(Model model){
        model.addAttribute("language", MIntro.LANGUAGES_STR);
        return "table/table_allocation_funds";
    }

    @RequestMapping("/table/whiteBook")
    public String whiteBook(Model model){
        model.addAttribute("language", MIntro.LANGUAGES_STR);
        return "table/table_white_book";
    }

    @RequestMapping("/table/tableQRBackground")
    public String tableQRBackground(Model model){
        return  "table/table_QR_background";
    }

}
