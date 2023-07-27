package org.springybot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springybot.entity.WhiteList;
import org.springybot.repository.WhiteListRepository;
import org.springybot.service.WhiteListService;

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
