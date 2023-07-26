package com.glp.service;

import com.glp.entity.WhiteList;

public interface WhiteListService {


    void save(WhiteList whiteList);

    WhiteList findByUserId(Long userId);
    
}
