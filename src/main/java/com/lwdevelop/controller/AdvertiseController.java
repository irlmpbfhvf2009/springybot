package com.lwdevelop.controller;

import com.lwdevelop.dto.AdvertiseDTO;
import com.lwdevelop.service.AdvertiseService;
import com.lwdevelop.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/advertise")
public class AdvertiseController {

    @Resource
    private AdvertiseService advertiseService;


    @GetMapping("/getAdvertise")
    public ResponseEntity<ResponseUtils.ResponseData>getAdvertise(Long advertiseId){
        return advertiseService.getAdvertise(advertiseId);
    }

    public ResponseEntity<ResponseUtils.ResponseData>getAllAdvertise(Long botId){
        return advertiseService.getAllAdvertise(botId);
    }

    @PostMapping("/addAdvertise")
    public ResponseEntity<ResponseUtils.ResponseData> addAdvertise(Long botId , String contact, String path, int deliyTime, Long ... groupId){
        AdvertiseDTO advertiseDTO = new AdvertiseDTO();
        advertiseDTO.setBotId(botId);
        advertiseDTO.setContact(contact);
        advertiseDTO.setDeilyTime(deliyTime);
        advertiseDTO.setPath(path);
        return advertiseService.addAdvertise(advertiseDTO,groupId);
    }

    @DeleteMapping("/deleteAdvertise/{id}")
    public ResponseEntity<ResponseUtils.ResponseData> deleteAdvertise(@PathVariable("id") Long advertiseId){
        return advertiseService.deleteAdvertise(advertiseId);
    }

    @PutMapping("/updateAdvertise")
    public ResponseEntity<ResponseUtils.ResponseData> updateAdvertise(Long advertiseId , String text){
        return advertiseService.updateAdvertise(advertiseId,text);
    }


    @PostMapping("/sendAdvertise")
    public ResponseEntity<ResponseUtils.ResponseData> sendAdvertise(Long botId, Long groupId, Long ... advertiseId){
        return advertiseService.sendAdvertise(botId,groupId,advertiseId);
    }



    @PostMapping("/stopAdvertise")
    public ResponseEntity<ResponseUtils.ResponseData> stopAdvertise(Long botId){
        return advertiseService.stopAdvertise(botId);
    }

}
