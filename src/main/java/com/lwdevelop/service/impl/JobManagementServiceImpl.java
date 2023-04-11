package com.lwdevelop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.lwdevelop.bot.Custom;
import com.lwdevelop.bot.utils.KeyboardButton;
import com.lwdevelop.dto.JobPostingDTO;
import com.lwdevelop.dto.JobSeekerDTO;
import com.lwdevelop.dto.JobTreeDTO;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.entity.JobPosting;
import com.lwdevelop.entity.JobSeeker;
import com.lwdevelop.entity.SpringyBot;
import com.lwdevelop.repository.JobPostingRepository;
import com.lwdevelop.repository.JobSeekerRepository;
import com.lwdevelop.service.JobManagementService;
import com.lwdevelop.utils.CryptoUtil;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;
import com.lwdevelop.utils.ResponseUtils.ResponseData;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
@Service
public class JobManagementServiceImpl implements JobManagementService {

    @Autowired
    private SpringyBotServiceImpl springyBotServiceImpl;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public JobSeeker findByUserIdWithJobSeeker(String userId) {
        return jobSeekerRepository.findByUserId(userId);
    }
    @Override
    public JobSeeker findByUserIdWithJobSeeker(String userId, String botId) {
        return jobSeekerRepository.findAllByUserIdAndBotId(userId, botId);
    }

    @Override
    public JobPosting findByUserIdWithJobPosting(String userId, String botId) {
        return jobPostingRepository.findAllByUserIdAndBotId(userId, botId);
    }
    
    @Override
    public void saveJobPosting(JobPosting jobPosting) {
        jobPostingRepository.save(jobPosting);
    }

    @Override
    public void saveJobSeeker(JobSeeker jobsSeeker) {
        jobSeekerRepository.save(jobsSeeker);
    }

    @Override
    public JobPosting findByUserIdWithJobPosting(String userId) {
        return jobPostingRepository.findByUserId(userId);
    }

    @Override
    public void deleteByIdWithJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }

    @Override
    public void deleteByUserIdWithJobPosting(String userId) {
        jobPostingRepository.deleteByUserId(userId);
    }

    @Override
    public ResponseEntity<ResponseData> decryptedUbWithJobPosting(JobPostingDTO jobPostingdDTO) {
        String ub = jobPostingdDTO.getUb();
        String decryptedUb = CryptoUtil.decrypt(ub);
        HashMap<Object, Object> data = new HashMap<>();

        String[] ubArray = decryptedUb.split("&");
        for (String param : ubArray) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                data.put(keyValue[0], keyValue[1]);
            }
        }

        for (String key : Arrays.asList("company", "position", "baseSalary", "commission", "workTime",
                "requirements", "location", "flightNumber")) {
            data.putIfAbsent(key, "");
        }

        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> decryptedUbWithJobSeeker(JobSeekerDTO jobSeekerDTO) {
        String ub = jobSeekerDTO.getUb();
        String decryptedUb = CryptoUtil.decrypt(ub);
        HashMap<Object, Object> data = new HashMap<>();

        String[] ubArray = decryptedUb.split("&");
        for (String param : ubArray) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                data.put(keyValue[0], keyValue[1]);
            }
        }

        // If any of the expected keys are not found in the data, set their values to
        // empty strings.
        for (String key : Arrays.asList("name", "gender", "dateOfBirth", "age", "nationality",
                "education", "skills", "targetPosition", "resources",
                "expectedSalary", "workExperience", "selfIntroduction")) {
            data.putIfAbsent(key, "");
        }

        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> addJobPosting(JobPostingDTO jobPostingDTO) {
        String userId = jobPostingDTO.getUserId();
        JobPosting jobPosting = this.findByUserIdWithJobPosting(userId);
        jobPosting.setBotId(jobPostingDTO.getBotId());
        jobPosting.setBaseSalary(jobPostingDTO.getBaseSalary());
        jobPosting.setCommission(jobPostingDTO.getCommission());
        jobPosting.setCompany(jobPostingDTO.getCompany());
        jobPosting.setFlightNumber(jobPostingDTO.getFlightNumber());
        jobPosting.setLocation(jobPostingDTO.getLocation());
        jobPosting.setPosition(jobPostingDTO.getPosition());
        jobPosting.setRequirements(jobPostingDTO.getRequirements());
        jobPosting.setWorkTime(jobPostingDTO.getWorkTime());
        this.saveJobPosting(jobPosting);

        // 修改訊息
        Long id = Long.valueOf(jobPostingDTO.getBotId());
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        SpringyBotDTO springyBotDTO = new SpringyBotDTO();
        springyBotDTO.setToken(springyBot.getToken());
        springyBotDTO.setUsername(springyBot.getUsername());
        Custom custom = new Custom(springyBotDTO);

        Integer messageId = jobPosting.getLastMessageId();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(userId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText("招聘人才\n" +
                "公司：" + jobPostingDTO.getCompany() + "\n" +
                "职位：" + jobPostingDTO.getPosition() + "\n" +
                "底薪：" + jobPostingDTO.getBaseSalary() + "\n" +
                "提成：" + jobPostingDTO.getCommission() + "\n" +
                "上班时间：" + jobPostingDTO.getWorkTime() + "\n" +
                "要求内容：" + jobPostingDTO.getRequirements() + "\n" +
                "🐌 地址：" + jobPostingDTO.getLocation() + "\n" +
                "✈️咨询飞机号： " + jobPostingDTO.getFlightNumber());

        editMessageText.setReplyMarkup(new KeyboardButton().keyboard_jobPosting(jobPostingDTO));
        try {
            custom.executeAsync(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> addJobSeeker(JobSeekerDTO jobSeekerDTO) {
        String userId = jobSeekerDTO.getUserId();
        JobSeeker jobSeeker = this.findByUserIdWithJobSeeker(userId);
        jobSeeker.setBotId(jobSeekerDTO.getBotId());
        jobSeeker.setName(jobSeekerDTO.getName());
        jobSeeker.setGender(jobSeekerDTO.getGender());
        jobSeeker.setDateOfBirth(jobSeekerDTO.getDateOfBirth());
        jobSeeker.setAge(jobSeekerDTO.getAge());
        jobSeeker.setNationality(jobSeekerDTO.getNationality());
        jobSeeker.setEducation(jobSeekerDTO.getEducation());
        jobSeeker.setSkills(jobSeekerDTO.getSkills());
        jobSeeker.setTargetPosition(jobSeekerDTO.getTargetPosition());
        jobSeeker.setResources(jobSeekerDTO.getResources());
        jobSeeker.setExpectedSalary(jobSeekerDTO.getExpectedSalary());
        jobSeeker.setWorkExperience(jobSeekerDTO.getWorkExperience());
        jobSeeker.setSelfIntroduction(jobSeekerDTO.getSelfIntroduction());
        this.saveJobSeeker(jobSeeker);

        // 修改訊息
        Long id = Long.valueOf(jobSeekerDTO.getBotId());
        SpringyBot springyBot = springyBotServiceImpl.findById(id).get();
        SpringyBotDTO springyBotDTO = new SpringyBotDTO();
        springyBotDTO.setToken(springyBot.getToken());
        springyBotDTO.setUsername(springyBot.getUsername());
        Custom custom = new Custom(springyBotDTO);

        Integer messageId = jobSeeker.getLastMessageId();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(userId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText("求职人员\n" +
                "姓名：" + jobSeekerDTO.getName() + "\n" +
                "男女：" + jobSeekerDTO.getGender() + "\n" +
                "出生_年_月_日" + jobSeekerDTO.getDateOfBirth() + "\n" +
                "年龄：" + jobSeekerDTO.getAge() + "\n" +
                "国籍：" + jobSeekerDTO.getNationality() + "\n" +
                "学历：" + jobSeekerDTO.getEducation() + "\n" +
                "技能：" + jobSeekerDTO.getSkills() + "\n" +
                "目标职位： " + jobSeekerDTO.getTargetPosition() + "\n" +
                "手上有什么资源：" + jobSeekerDTO.getResources() + "\n" +
                "期望薪资：" + jobSeekerDTO.getExpectedSalary() + "\n" +
                "工作经历：" + jobSeekerDTO.getWorkExperience() + "\n" +
                "自我介绍：" + jobSeekerDTO.getSelfIntroduction());

        editMessageText.setReplyMarkup(new KeyboardButton().keyboard_jobSeeker(jobSeekerDTO));
        try {
            custom.executeAsync(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> getJobTreeData() {
        List<JobTreeDTO> data = new ArrayList<>();
        List<SpringyBot> springyBots = springyBotServiceImpl.findAll();
    
        for (int i = 0; i < springyBots.size(); i++) {
            JobTreeDTO posting = new JobTreeDTO();
            posting.setLabel("招聘信息");
            posting.setId(0L);
            JobTreeDTO seeker = new JobTreeDTO();
            seeker.setLabel("求職信息");
            seeker.setId(1L);
            for (int j = 0; j < springyBots.get(i).getJobUser().size(); j++) {
                springyBots.get(i).getJobUser().stream().forEach(jobUser -> {
                    JobTreeDTO user = new JobTreeDTO();
                    List<JobTreeDTO> ff = new ArrayList<>();
                    user.setId(jobUser.getId());
                    user.setLabel(jobUser.getUsername());
                    user.setChildren(null);
                    ff.add(user);
                    posting.setChildren(ff);
                    seeker.setChildren(ff);
                });
            }
    
            JobTreeDTO jobTreeDTO = new JobTreeDTO();
            List<JobTreeDTO> children = new ArrayList<>();
            children.add(seeker);
            children.add(posting);
    
            jobTreeDTO.setLabel(springyBots.get(i).getUsername());
            jobTreeDTO.setId((long) i);
            jobTreeDTO.setChildren(children);
            data.add(jobTreeDTO);
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }


}
