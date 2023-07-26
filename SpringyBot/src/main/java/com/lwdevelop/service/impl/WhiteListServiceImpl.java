package com.lwdevelop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lwdevelop.entity.WhiteList;
import com.lwdevelop.repository.WhiteListRepository;
import com.lwdevelop.service.WhiteListService;

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
