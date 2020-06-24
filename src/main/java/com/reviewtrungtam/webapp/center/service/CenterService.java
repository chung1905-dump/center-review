package com.reviewtrungtam.webapp.center.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.repository.CenterRepository;
import com.reviewtrungtam.webapp.general.slugify.SlugifyService;
import com.reviewtrungtam.webapp.general.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Center findBySlug(String slug) {
        return centerRepository.findBySlugNameAndIsActiveIsTrue(slug);
    }

    public List<Center> getAll() {
        return centerRepository.findAll();
    }

    public void preSave(Center center, MultipartFile logo, Set<String> errMsgs) {
        errMsgs.addAll(validateEntity(center));
        center.setLogo(saveLogo(logo, errMsgs));
    }

    private Set<String> validateEntity(Center center) {
//        TODO: check filename
        Set<String> errMsgs = new HashSet<>();
        Set<ConstraintViolation<Center>> errors = validator.validate(center);
        if (errors.size() > 0) {
            for (ConstraintViolation<Center> err : errors) {
                String eMsg = err.getPropertyPath().toString() + " " + err.getMessage();
                errMsgs.add(eMsg);
            }
        }

        return errMsgs;
    }

    private String saveLogo(MultipartFile logo, Set<String> errMsgs) {
        if (logo.getSize() == 0) {
            return null;
        }
        String mimeType = logo.getContentType();
        if (logo.getSize() > 5242880 || (mimeType != null && !mimeType.startsWith("image/"))) {
            errMsgs.add("Logo is invalid");
        }
        return storageService.store(logo, "logo");
    }
}
