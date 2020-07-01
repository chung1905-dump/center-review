package com.reviewtrungtam.webapp.user.controller;

import com.reviewtrungtam.webapp.user.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {
    @PostMapping(path = "/login_post")
    public String login(User user) {
        int a = 1;
        return "redirect:/";
    }
}
