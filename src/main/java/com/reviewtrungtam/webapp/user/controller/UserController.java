package com.reviewtrungtam.webapp.user.controller;

import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.user.entity.User;
import com.reviewtrungtam.webapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/register")
    public String register() {
        return "views/user/layout/register.html";
    }

    @PostMapping(path = "/register")
    public RedirectView registerPost(User user, RedirectAttributes redirectAttributes) {
        Set<String> errMsgs = new HashSet<>();
        try {
            if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
                throw new AppException("Invalid Request.");
            }
            Optional<User> u = userService.findUserByUserName(user.getUserName());
            if (u.isPresent()) {
                throw new AppException("Username has been taken.");
            }
            userService.preSave(user);
            userService.save(user);
        } catch (AppException e) {
            errMsgs.addAll(e.getMessages());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        } catch (Exception e) {
            errMsgs.add(e.getMessage());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        }

        return new RedirectView("/register");
    }
}
