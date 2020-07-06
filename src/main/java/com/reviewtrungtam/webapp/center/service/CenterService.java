package com.reviewtrungtam.webapp.center.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.repository.CenterRepository;
import com.reviewtrungtam.webapp.general.config.Status;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.general.slugify.SlugifyService;
import com.reviewtrungtam.webapp.general.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CenterService {
    @Autowired
    private CenterRepository centerRepository;

    private final Validator validator;

    private final StorageService storageService;


    @Autowired
    public CenterService(
            Validator validator,
            StorageService storageService
    ) {
        this.validator = validator;
        this.storageService = storageService;
    }

    public Center save(Center entity) {
        entity.setSlugName(SlugifyService.slugify(entity.getName()));
        return centerRepository.save(entity);
    }

    public Center findActiveBySlug(String slug) {
        return centerRepository.findBySlugNameAndStatus(slug, Status.ACTIVE);
    }

    public Center findActiveById(int id) {
        return centerRepository.findByIdAndStatus(id, Status.ACTIVE);
    }

    public List<Center> getNewUpdatedReview(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit, Sort.by("lastReviewTime").descending());
        return centerRepository.findAll(pageRequest).toList();
    }

    public void preSave(Center center, MultipartFile logo) throws AppException {
        center.setStatus(center.getDefaultStatus());
        validateEntity(center);
        center.setLogo(saveLogo(logo));
    }

    private void validateEntity(Center center) throws AppException {
//        TODO: check filename
        Set<String> errMsgs = new HashSet<>();
        Set<ConstraintViolation<Center>> errors = validator.validate(center);
        if (errors.size() == 0) {
            return;
        }
        for (ConstraintViolation<Center> err : errors) {
            String eMsg = err.getPropertyPath().toString() + " " + err.getMessage();
            errMsgs.add(eMsg);
        }
        throw new AppException(errMsgs);
    }

    private String saveLogo(MultipartFile logo) {
        if (logo.getSize() == 0) {
            return null;
        }
        String mimeType = logo.getContentType();
        if (logo.getSize() > 5242880 || (mimeType != null && !mimeType.startsWith("image/"))) {
            throw new AppException("Logo is invalid");
        }
        return storageService.store(logo, "logo");
    }
}
