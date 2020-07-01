package com.reviewtrungtam.webapp.review.controller;

import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
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
    public RedirectView add(
            @RequestParam("center-id") int centerId,
            Review review,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        Set<String> errMsgs = new HashSet<>();
        try {
            reviewService.validateNewReview(review);
            reviewService.preSave(review, centerId, request.getRemoteAddr());
            reviewService.save(review);
        } catch (AppException e) {
            errMsgs.add(e.getMessage());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        } catch (Exception e) {
            errMsgs.add("Error while adding review");
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        }
        return new RedirectView("/");
    }
}
