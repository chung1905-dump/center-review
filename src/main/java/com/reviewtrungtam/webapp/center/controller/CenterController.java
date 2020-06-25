package com.reviewtrungtam.webapp.center.controller;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.service.CenterService;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@RequestMapping
public class CenterController {
    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    @GetMapping(path = "/")
    public String list(Model model) {
        List<Center> list = centerService.getAll();
        model.addAttribute("size", list.size());

        return "views/center/layout/center-list.html";
    }


    @GetMapping(path = "/center/view/{slug}")
    public String view(@PathVariable(name = "slug") String slug, Model model) {
        Center center = centerService.findActiveBySlug(slug);
        model.addAttribute("center", center);

        return "views/center/layout/center-view.html";
    }

    @GetMapping(path = "/{slug}")
    public String shortView(@PathVariable(name = "slug") String slug, Model model) {
        return view(slug, model);
    }

    @GetMapping(path = "/center/add")
    public String add() {
        return "views/center/layout/center-add.html";
    }

    @PostMapping(path = "/center/add")
    public RedirectView addPost(@RequestParam("logo-image") MultipartFile file, Center center, RedirectAttributes redirectAttributes) {
        Set<String> errMsgs = new HashSet<>();
        try {
            centerService.preSave(center, file);
            centerService.save(center);
        } catch (AppException e) {
            errMsgs.addAll(e.getMessages());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
            return new RedirectView("/center/add");
        } catch (Exception e) {
            errMsgs.add("Error while submitting center");
            redirectAttributes.addFlashAttribute("errs", errMsgs);
            return new RedirectView("/center/add");
        }
        return new RedirectView("/");
    }
}
