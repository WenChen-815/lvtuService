package com.zhoujh.lvtu.post.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.post.model.TravelPlan;
import com.zhoujh.lvtu.post.service.TravelPlanService;
import com.zhoujh.lvtu.post.serviceImpl.TravelPlanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/travelPlans")
public class TravelPlanController {

    @Autowired
    private TravelPlanServiceImpl travelPlanServiceImpl;
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

    @GetMapping("/getPlanById")
    public TravelPlan getPlanById(@RequestParam String travelPlanId) {
        return travelPlanServiceImpl.getById(travelPlanId);
    }

}
