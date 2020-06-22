package com.reviewtrungtam.webapp.center.controller;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class CenterController {
    @Autowired
    private CenterService centerService;

    @GetMapping(path = "/center/list")
    public String list(Model model) {
        List<Center> list = centerService.getAll();
        model.addAttribute("size", list.size());

        return "views/center/center-list.html";
    }


    @GetMapping(path = "/center/view/{id}")
    public String view(@PathVariable(name = "id") int id, Model model) {
        Center entity = new Center();
        model.addAttribute("article", entity);

        return "views/center/center-view.html";
    }


    @GetMapping(path = "/center/new")
    public String add(Model model) {
        return "views/center/center-new.html";
    }
}
