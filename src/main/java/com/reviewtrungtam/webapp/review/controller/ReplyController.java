package com.reviewtrungtam.webapp.review.controller;

import com.reviewtrungtam.webapp.general.config.Status;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import com.reviewtrungtam.webapp.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping
public class ReplyController {
    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    @Autowired
    ReplyController(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping(path = "/reply/{id}")
    public String replyForm(@PathVariable Integer id, Model model) {
        try {
            Optional<Review> parentReview = reviewRepository.findByIdAndStatus(id, Status.ACTIVE);
            if (parentReview.isEmpty()) {
                throw new AppException("Not found");
            }
            model.addAttribute("review", parentReview.get());
        } catch (AppException e) {
            model.addAttribute("errs", e.getMessages());
            return "redirect:/";
        } catch (Exception e) {
            Set<String> errMsgs = new HashSet<>();
            errMsgs.add("Review not found");
            System.out.println(e.getMessage());
            model.addAttribute("errs", errMsgs);
            return "redirect:/";
        }
        return "views/review/layout/reply.html";
    }

    @PostMapping(path = "/reply/add")
    public RedirectView reply(
            @RequestParam("parent-id") Integer parentId,
            Review review,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        try {
            Optional<Review> parentReview = reviewRepository.findByIdAndStatus(parentId, Status.ACTIVE);
            if (parentReview.isEmpty()) {
                throw new AppException("Parent not found");
            }
            review.setParent(parentReview.get());
            review.setCenter(null);
            reviewService.validateNewReview(review);
            reviewService.preSave(review, 0, request.getRemoteAddr());
            reviewService.save(review);
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errs", e.getMessages());
        } catch (Exception e) {
            Set<String> errMsgs = new HashSet<>();
            errMsgs.add("Error while reply");
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        }

        return new RedirectView("/c/" + reviewService.getCenter(review).getSlugName());
    }
}
