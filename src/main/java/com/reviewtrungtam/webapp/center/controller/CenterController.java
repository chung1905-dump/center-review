package com.reviewtrungtam.webapp.center.controller;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.service.CenterService;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private final int defaultLimitPerPage = 30;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    // TODO: p=text will return 400
    @GetMapping(path = "/")
    public String list(@RequestParam(value = "p", required = false) String pageS,
                       @RequestParam(value = "l", required = false) String limitS,
                       Model model) {
        int page = 0, limit = defaultLimitPerPage;
        try {
            page = Integer.parseInt(pageS);
            if (page < 0) {
                page = 0;
            }
        } catch (NumberFormatException ignored) {
        }
        try {
            limit = Integer.parseInt(limitS);
            if (limit < 1) {
                limit = defaultLimitPerPage;
            }
        } catch (NumberFormatException ignored) {
        }

        Page<Center> centerPage = centerService.getNewUpdatedReview(limit, page);
        model.addAttribute("title", "Latest Updated");
        model.addAttribute("centers", centerPage.toList());
        model.addAttribute("total", centerPage.getTotalPages());
        model.addAttribute("currentUrl", "/");
        model.addAttribute("limit", limit);
        model.addAttribute("page", page);

        return "views/center/layout/center-list.html";
    }

    @GetMapping(path = "/c/{slug}")
    public String view(@PathVariable(name = "slug") String slug, Model model) {
        Center center = centerService.findActiveBySlug(slug);
        model.addAttribute("center", center);

        return "views/center/layout/center-view.html";
    }

    @GetMapping(path = "/center/add")
    public String add() {
        return "views/center/layout/center-add.html";
    }

    @PostMapping(path = "/center/add")
    public RedirectView addPost(@RequestParam("logo-image") MultipartFile file, Center center, RedirectAttributes redirectAttributes) {
        Set<String> errMsgs = new HashSet<>();
        try {
            center.setPoint(0);
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
