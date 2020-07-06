package com.reviewtrungtam.webapp.center.controller;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final CenterService centerService;

    @Autowired
    public SearchController(CenterService centerService) {
        this.centerService = centerService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String query,
                         @RequestParam(value = "p", required = false) String pageS,
                         @RequestParam(value = "l", required = false) String limitS,
                         Model model
    ) {
        if (StringUtils.isEmpty(query)) {
            return "views/center/layout/search.html";
        }
        int defaultLimitPerPage = 30;
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

        Page<Center> centerPage = centerService.findActiveByName(query, limit, page);
        model.addAttribute("total", centerPage.getTotalPages());
        model.addAttribute("currentUrl", "/search?q=" + query);
        model.addAttribute("limit", limit);
        model.addAttribute("page", page);

        model.addAttribute("query", query);
        model.addAttribute("centers", centerPage.toList());

        return "views/center/layout/search-results.html";
    }
}
