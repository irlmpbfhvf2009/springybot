package com.glp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.glp.entity.WhiteList;
import com.glp.repository.WhiteListRepository;
import com.glp.service.WhiteListService;

@Service
public class WhiteListServiceImpl implements WhiteListService {

    @Autowired
    private WhiteListRepository whiteListRepository;

    @Override
    public WhiteList findByUserId(Long userId) {
        return whiteListRepository.findByUserId(userId);
    }

    @Override
    public void save(WhiteList whiteList) {
        whiteListRepository.save(whiteList);
    }
    
}
