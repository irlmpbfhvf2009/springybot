package com.lwdevelop.service;

import com.lwdevelop.entity.WhiteList;

public interface WhiteListService {


    void save(WhiteList whiteList);

    WhiteList findByUserId(Long userId);
    
}
