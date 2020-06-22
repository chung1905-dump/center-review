package com.reviewtrungtam.webapp.center.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.repository.CenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CenterService {
    @Autowired
    private CenterRepository centerRepository;

    public List<Center> getAll() {
        return centerRepository.findAll();
    }
}
