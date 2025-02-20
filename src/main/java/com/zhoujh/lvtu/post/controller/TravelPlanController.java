package com.zhoujh.lvtu.post.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.serviceImpl.HmsPushTokenServiceImpl;
import com.zhoujh.lvtu.common.serviceImpl.UserRelationshipServiceImpl;
import com.zhoujh.lvtu.common.serviceImpl.UserServiceImpl;
import com.zhoujh.lvtu.post.model.PlanParticipant;
import com.zhoujh.lvtu.post.model.TravelPlan;
import com.zhoujh.lvtu.post.serviceImpl.PlanParticipantServiceImpl;
import com.zhoujh.lvtu.post.serviceImpl.TravelPlanServiceImpl;
import com.zhoujh.lvtu.utils.HuaweiPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/travelPlans")
public class TravelPlanController {

    @Autowired
    private TravelPlanServiceImpl travelPlanServiceImpl;
    @Autowired
    private PlanParticipantServiceImpl planParticipantServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private HmsPushTokenServiceImpl hmsPushTokenServiceImpl;
    @Autowired
    private HuaweiPushService huaweiPushService;
    @Autowired
    private UserRelationshipServiceImpl userRelationshipServiceImpl;
    @Value("${spring.resources.static-locations}")
    private String staticPath;

    // 创建行程
    @PostMapping("/createPlan")
    public TravelPlan createPlan(@RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam String userId,
                             @RequestParam Integer status,
                             @RequestParam String title,
                             @RequestParam String content,
                             @RequestParam Integer maxParticipants,
                             @RequestParam Integer currentParticipants,
                             @RequestParam Double budget,
                             @RequestParam String address,
                             @RequestParam Double addressLatitude,
                             @RequestParam Double addressLongitude,
                             @RequestParam Integer travelMode,
                             @RequestParam String startTime,
                             @RequestParam String endTime) {
        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setTravelPlanId(UUID.randomUUID().toString());
        travelPlan.setUserId(userId);
        travelPlan.setStatus(status);
        travelPlan.setTitle(title);
        travelPlan.setContent(content);
        travelPlan.setMaxParticipants(maxParticipants);
        travelPlan.setCurrentParticipants(currentParticipants);
        travelPlan.setBudget(budget);
        travelPlan.setAddress(address);
        travelPlan.setLatitude(addressLatitude);
        travelPlan.setLongitude(addressLongitude);
        travelPlan.setTravelMode(travelMode);
        travelPlan.setCreateTime(LocalDateTime.now());
        travelPlan.setUpdateTime(LocalDateTime.now());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.PRC);
        try {
            travelPlan.setStartTime(dateFormatter.parse(startTime));
            travelPlan.setEndTime(dateFormatter.parse(endTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        travelPlan.setImageUrl(handleImage(file, travelPlan));
        return travelPlanServiceImpl.createPlan(travelPlan);
    }

    private String handleImage(MultipartFile file, TravelPlan travelPlan) {
        // 校验是否存在
        TravelPlan travelPlan1 = travelPlanServiceImpl.getById(travelPlan.getUserId());
        if (file != null && !file.isEmpty()) {
            try {
                // 构造存储路径
                String basePath = staticPath.replace("file:", ""); // 去掉前缀
                File directory = new File(basePath, "planImage");
                if (!directory.exists()) {
                    directory.mkdirs(); // 确保目录存在
                }
                // 构造文件名
                String fileName = travelPlan.getUserId() + "_" + file.getOriginalFilename();
                File destination = new File(directory, fileName);
                file.transferTo(destination); // 保存文件
                return "/lvtu/planImage/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("图片上传失败", e);
            }
        } else {
            if (travelPlan1 == null) {
                return null;
            }
            return travelPlan1.getImageUrl();
        }
    }

    // 分页查询
    @GetMapping("/getPlans")
    public Page<TravelPlan> getPlans(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return travelPlanServiceImpl.getAllPlans(pageNum, pageSize);
    }

    @GetMapping("/getFollowPlans")
    public Page<TravelPlan> getFollowPlans(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String userId
    ) {
        List<UserRelationship> userRelationships = userRelationshipServiceImpl.getFollowList(userId);
        return travelPlanServiceImpl.getFollowPlans(pageNum, pageSize, userRelationships);
    }

    @GetMapping("/getPlanById")
    public TravelPlan getPlanById(@RequestParam String travelPlanId) {
        return travelPlanServiceImpl.getById(travelPlanId);
    }

    @GetMapping("/getPlansByUserId")
    public List<TravelPlan> getPlansByUserId(@RequestParam String userId) {
        return travelPlanServiceImpl.getPlansByUserId(userId);
    }

    @PostMapping("/addParticipants")
    public String addParticipants(@RequestParam String travelPlanId, @RequestParam String userId, @RequestParam String creatorId) {
        TravelPlan travelPlan = travelPlanServiceImpl.getById(travelPlanId);
        if (travelPlan == null) {
            return "fail";
        }
        travelPlan.setCurrentParticipants(travelPlan.getCurrentParticipants() + 1);
        PlanParticipant participant = new PlanParticipant();
        participant.setPlanId(travelPlanId);
        participant.setUserId(userId);
        boolean isSuccess1 = planParticipantServiceImpl.addParticipant(participant);
        boolean isSuccess2 = travelPlanServiceImpl.updateById(travelPlan);

        // 通知创建者
        String tokens = hmsPushTokenServiceImpl.createTokens(creatorId);
        String json = "{\n" +
                "    \"validate_only\": false,\n" +
                "    \"message\": {\n" +
                "        \"data\": \"{'title':'旅兔通知'," +
                "'body':'有旅友参加了你的旅行计划，请及时联系哦~'," +
                "'messageTag':'joinPlan'," +
                "'travelPlanId':'"+travelPlan.getTravelPlanId()+"'," +
                "'tagUserId':'"+ creatorId +"'}\",\n" +
                "        \"token\": ["+ tokens +"]\n" +
                "    }\n" +
                "}";
        System.out.println(json);
        try {
            huaweiPushService.sendPushMessage(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (isSuccess1 && isSuccess2){
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("/isParticipant")
    public boolean isParticipants(@RequestParam String travelPlanId, @RequestParam String userId) {
        PlanParticipant participant = planParticipantServiceImpl.findByPlanIdAndUserId(travelPlanId, userId);
        return participant != null;
    }

    @PostMapping("/finishPlan")
    public String finishPlan(@RequestParam String travelPlanId) {
        TravelPlan travelPlan = travelPlanServiceImpl.getById(travelPlanId);
        if (travelPlan == null) {
            return "fail";
        }
        travelPlan.setStatus(3);
        boolean isSuccess = travelPlanServiceImpl.updateById(travelPlan);
        return isSuccess ? "success" : "fail";
    }
    @PostMapping("/exitPlan")
    public String exitPlan(@RequestParam String travelPlanId, @RequestParam String userId) {
        TravelPlan travelPlan = travelPlanServiceImpl.getById(travelPlanId);
        if (travelPlan == null) {
            return "fail";
        }
        travelPlan.setCurrentParticipants(travelPlan.getCurrentParticipants() - 1);
        boolean isSuccess1 = planParticipantServiceImpl.deleteByPlanIdAndUserId(travelPlanId, userId);
        boolean isSuccess2 =travelPlanServiceImpl.updateById(travelPlan);
        if (isSuccess1 && isSuccess2){
            return "success";
        } else {
            return "fail";
        }
    }
    @GetMapping("/getParticipants")
    public List<UserInfo> getParticipants(@RequestParam String travelPlanId) {
        List<PlanParticipant> participants = planParticipantServiceImpl.getParticipants(travelPlanId);
        List<UserInfo> userInfos = new ArrayList<>();
        for(PlanParticipant participant : participants){
            User user = userServiceImpl.getUserById(participant.getUserId());
            UserInfo userInfo = new UserInfo(user.getUserId(), user.getUserName(), user.getStatus(), user.getGender(), user.getAge(), user.getBirth(), user.getAvatarUrl(), 0);
            userInfos.add(userInfo);
        }
        return userInfos;
    }
}
