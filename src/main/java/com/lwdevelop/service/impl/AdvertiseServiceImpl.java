package com.lwdevelop.service.impl;

import com.lwdevelop.bot.handler.ScheduleTask;
import com.lwdevelop.dto.AdvertiseDTO;
import com.lwdevelop.entity.Advertise;
import com.lwdevelop.entity.RobotGroupManagement;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.AdvertiseRepository;
import com.lwdevelop.repository.RobotGroupManagementRepository;
import com.lwdevelop.service.AdvertiseService;
import com.lwdevelop.service.SpringyBotService;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdvertiseServiceImpl implements AdvertiseService {

    @Autowired
    private AdvertiseRepository advertiseRepository;

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;

    @Autowired
    private SpringyBotService springyBotService;
    @Autowired
    private ScheduleTask scheduleTask;


    @Override
    public void save(Advertise advertise) {
        advertiseRepository.save(advertise);
    }

    /**
     * 新增广告
     * @param advertiseDTO
     * @param groupId
     * @return
     */
    public ResponseEntity<ResponseUtils.ResponseData> addAdvertise(AdvertiseDTO advertiseDTO,Long ... groupId ){
        Advertise advertise = new Advertise();
        Set<RobotGroupManagement> robotGroupManagements = new HashSet<>();
        advertise.setContact(advertiseDTO.getContact());
        advertise.setDeilyTime(advertiseDTO.getDeilyTime());
        advertise.setPath(advertiseDTO.getPath());
        advertise.setBotId(advertiseDTO.getBotId());
        for (Long id : groupId){
            RobotGroupManagement robotGroupManagement = robotGroupManagementRepository.findById(id).get();
            robotGroupManagements.add(robotGroupManagement);
            advertise.setRobotGroupManagement(robotGroupManagements);
        }
        System.out.println(advertise);
        save(advertise);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增成功");
    }

    /**
     * 删除广告
     * @param advertiseId
     * @return
     */
    @Override
    public ResponseEntity<ResponseUtils.ResponseData> deleteAdvertise(Long advertiseId) {

        advertiseRepository.deleteById(advertiseId);

        return ResponseUtils.response(RetEnum.RET_SUCCESS, "删除成功");

    }

    /**
     * 发送广告
     * @param botId
     * @param groupId
     * @param advertiseId
     * @return
     */
    @Override
    public ResponseEntity<ResponseUtils.ResponseData> sendAdvertise(Long botId , Long groupId, Long ... advertiseId) {
        SpringyBot springyBot = springyBotService.findById(botId).get();
        RobotGroupManagement robotGroupManagement = robotGroupManagementRepository.findById(groupId).get();
        robotGroupManagementRepository.save(robotGroupManagement);
        if (springyBot.getState()){
            scheduleTask.setRun(true);
            scheduleTask.setAddBot(true);
            scheduleTask.setSpringyBot(springyBot);
            scheduleTask.setGroupId(robotGroupManagement.getGroupId());
            ArrayList<Advertise> advertises = new ArrayList<>();
            for (Long id :advertiseId){
                Advertise advertise = advertiseRepository.findAdvertiseById(id);
                advertises.add(advertise);
            }

            scheduleTask.setAdvertises(advertises);
            log.info("Common Telegram send advertise started.");
            return ResponseUtils.response(RetEnum.RET_SUCCESS,"发送成功");
        }else {
            return ResponseUtils.response(RetEnum.RET_START_FAIL,"发送失败");
        }

    }

    /**
     * 更新广告
     * @param advertiseId
     * @param text
     * @return
     */
    @Override
    public ResponseEntity<ResponseUtils.ResponseData> updateAdvertise(Long advertiseId, String text) {
        Advertise advertise = advertiseRepository.findAdvertiseById(advertiseId);
        advertise.setContact(text);
        save(advertise);
        return ResponseUtils.response(RetEnum.RET_SUCCESS,"更新成功");
    }

    /**
     * 取得单一广告
     * @param advertiseId
     * @return
     */
    @Override
    public ResponseEntity<ResponseUtils.ResponseData> getAdvertise(Long advertiseId) {
        Advertise advertise = advertiseRepository.findAdvertiseById(advertiseId);
        HashMap<String,Object> data = new HashMap<>();
        data.put("advertise",advertise);
        return ResponseUtils.response(RetEnum.RET_SUCCESS,data);
    }

    /**
     * 获得bot中所有广告
     * @param botId
     * @return
     */
    @Override
    public ResponseEntity<ResponseUtils.ResponseData> getAllAdvertise(Long botId) {
        List<Advertise> advertises = advertiseRepository.findByBotIdLike(botId);
        HashMap<String,Object> data = new HashMap<>();
        data.put("advertises",advertises);
        return  ResponseUtils.response(RetEnum.RET_SUCCESS,data);
    }

    /**
     * 停止发送广告
     * @param botId
     * @return
     */
    public ResponseEntity<ResponseUtils.ResponseData> stopAdvertise(Long botId){
        scheduleTask.setBotId(botId);
        log.info("Common Telegram send advertise stoped.");
        return ResponseUtils.response(RetEnum.RET_SUCCESS,"已停止发送");

    }
}
