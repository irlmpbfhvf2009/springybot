package com.lwdevelop.service;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.utils.ResponseUtils;

public interface TestService {

    ResponseEntity<ResponseUtils.ResponseData> addConfig(Long id);
    
}
