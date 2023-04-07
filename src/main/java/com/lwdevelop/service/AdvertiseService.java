package com.lwdevelop.service;

import com.lwdevelop.dto.AdvertiseDTO;
import com.lwdevelop.entity.Advertise;
import com.lwdevelop.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;


public interface AdvertiseService {
    void save(Advertise advertise);



    public ResponseEntity<ResponseUtils.ResponseData> addAdvertise(AdvertiseDTO advertiseDTO ,Long ... groupId);

    ResponseEntity<ResponseUtils.ResponseData> deleteAdvertise(Long advertiseId);

    public ResponseEntity<ResponseUtils.ResponseData> stopAdvertise(Long botId);

    ResponseEntity<ResponseUtils.ResponseData> sendAdvertise(Long botId,Long groupId , Long ... id);

    ResponseEntity<ResponseUtils.ResponseData> updateAdvertise(Long advertiseId, String text);

    ResponseEntity<ResponseUtils.ResponseData> getAdvertise(Long advertiseId);

    ResponseEntity<ResponseUtils.ResponseData> getAllAdvertise(Long botId);
}
