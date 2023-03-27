package com.lwdevelop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.utils.ResponseUtils;

public interface SpringyBotService {
        // DB CRUD For SpringyBot
        SpringyBot findByUsername(String username);

        SpringyBot findByToken(String token);

        Optional<SpringyBot> findById(Long id);

        void save(SpringyBot springyBot);

        List<SpringyBot> findAll(int page, int pageSize);

        void deleteById(Long id);

        // custom
        ResponseEntity<ResponseUtils.ResponseData> addBot(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> getAllBot(int page, int pageSize);

        ResponseEntity<ResponseUtils.ResponseData> updateBot(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> deleteBot(Map<String, String> requestData);

        ResponseEntity<ResponseUtils.ResponseData> start(SpringyBotDTO springyBotDTO);

        ResponseEntity<ResponseUtils.ResponseData> stop(SpringyBotDTO springyBotDTO);
}
