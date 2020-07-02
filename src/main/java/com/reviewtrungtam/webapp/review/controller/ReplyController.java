package com.reviewtrungtam.webapp.review.controller;

import com.reviewtrungtam.webapp.general.config.Status;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import com.reviewtrungtam.webapp.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String replyForm(int id, Model model) {
        try {
            Optional<Review> parentReview = reviewRepository.findByIdAndStatus(id, Status.ACTIVE);
            if (parentReview.isEmpty()) {
                throw new AppException("Not found");
            }
            model.addAttribute("review", parentReview.get());
        } catch (AppException e) {
            model.addAttribute("errs", e.getMessages());
        } catch (Exception e) {
            Set<String> errMsgs = new HashSet<>();
            errMsgs.add("Error while adding review");
            System.out.println(e.getMessage());
            model.addAttribute("errs", errMsgs);
        }
        return "views/review/layout/reply.html";
    }

    @PostMapping(path = "/reply/add")
    public String reply(
            @RequestParam("parent-id") int parentId,
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
            reviewService.validateNewReview(review);
            reviewService.preSave(review, parentReview.get().getCenter().getId(), request.getRemoteAddr());
            reviewService.save(review);
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errs", e.getMessages());
        } catch (Exception e) {
            Set<String> errMsgs = new HashSet<>();
            errMsgs.add("Error while reply");
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errs", errMsgs);
        }
        return "views/review/layout/reply.html";
    }
}
