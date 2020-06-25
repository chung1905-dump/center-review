package com.reviewtrungtam.webapp.review.controller;

import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(path = "/center/review")
    public RedirectView add(Review review, RedirectAttributes redirectAttributes) {
        Set<String> errMsgs = new HashSet<>();
        try {
            reviewService.preSave(review, errMsgs);
            if (errMsgs.size() > 0) {
                throw new Exception();
            }
            reviewService.save(review);
        } catch (Exception e) {
            errMsgs.add("Error while adding review");
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        }
        return new RedirectView("/");
    }
}
