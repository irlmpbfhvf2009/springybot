package org.springybot.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springybot.entity.Admin;
import org.springybot.entity.RobotChannelManagement;
import org.springybot.entity.RobotGroupManagement;
import org.springybot.entity.SpringyBot;
import org.springybot.repository.RobotChannelManagementRepository;
import org.springybot.repository.RobotGroupManagementRepository;
import org.springybot.service.impl.AdminServiceImpl;
import org.springybot.service.impl.SpringyBotServiceImpl;
import org.springybot.utils.RedisUtils;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private RobotGroupManagementRepository robotGroupManagementRepository;

    @Autowired
    private RobotChannelManagementRepository robotChannelManagementRepository;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void run(String... args) throws Exception {

        try {

            redisUtils.clearAllData();
            
            List<Long> rgmIds = new ArrayList<>();
            List<Long> rcmIds = new ArrayList<>();

            List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
            springyBots.forEach(springyBot -> {
                springyBot.setState(false);
                springyBotServiceImpl.save(springyBot);

                Long id = springyBot.getId();

                List<RobotGroupManagement> rgm = springyBotServiceImpl.findRobotGroupManagementBySpringyBotId(id);
                rgm.stream().forEach(r->{
                    rgmIds.add(r.getId());
                });
                List<RobotChannelManagement> rcm = springyBotServiceImpl.findRobotChannelManagementBySpringyBotId(id);
                rcm.stream().forEach(r->{
                    rcmIds.add(r.getId());
                });

            });
            
            
            List<RobotGroupManagement> rgms = robotGroupManagementRepository.findAll();
            List<RobotChannelManagement> rcms = robotChannelManagementRepository.findAll();
            rgms.stream().forEach(r->{
                if(!rgmIds.contains(r.getId())){
                    robotGroupManagementRepository.deleteById(r.getId());
                }
            });
            rcms.stream().forEach(r->{
                if(!rcmIds.contains(r.getId())){
                    robotChannelManagementRepository.deleteById(r.getId());
                }
            });
            

            Admin admin = adminServiceImpl.findByUsername("admin");
            Admin test = adminServiceImpl.findByUsername("test");
            if (admin == null) {
                admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setEnabled(true);
                admin.setRoles(Arrays.asList(new String[] { "ADMIN" }));
                adminServiceImpl.saveAdmin(admin);
            }

            if (test == null) {
                test = new Admin();
                test.setUsername("test");
                test.setPassword("123456");
                test.setEnabled(true);
                test.setRoles(Arrays.asList(new String[] { "ADMIN", "TEST" }));
                adminServiceImpl.saveAdmin(test);
            }

        } catch (Exception e) {
            throw e;
        }

    }

}