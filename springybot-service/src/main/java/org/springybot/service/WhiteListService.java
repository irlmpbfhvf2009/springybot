package org.springybot.service;

import org.springybot.entity.WhiteList;

public interface WhiteListService {


    void save(WhiteList whiteList);

    WhiteList findByUserId(Long userId);
    
}
