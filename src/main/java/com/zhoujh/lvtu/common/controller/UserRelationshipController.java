package com.zhoujh.lvtu.common.controller;

import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.model.UserInfo;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.service.UserRelationshipService;
import com.zhoujh.lvtu.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relationship")
public class UserRelationshipController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRelationshipService userRelationshipService;

    /**
     * 添加关系
     * @param userId 用户ID
     * @param relatedUserId 关联用户ID
     * @param relationshipType 关系类型 (1: 关注, 2: 互关, 3: 拉黑)
     * @return 操作结果
     */
    @PostMapping("/add")
    public String addRelationship(@RequestParam String userId,
                                  @RequestParam String relatedUserId,
                                  @RequestParam Integer relationshipType) {
        boolean result = userRelationshipService.addRelationship(userId, relatedUserId, relationshipType);
        return result ? "添加关系成功" : "添加关系失败";
    }

    /**
     * 修改关系
     * @param userId 用户ID
     * @param relatedUserId 关联用户ID
     * @param relationshipType 新的关系类型 (1: 关注, 2: 互关, 3: 拉黑)
     * @return 操作结果
     */
    @PostMapping("/update")
    public int updateRelationship(@RequestParam String userId,
                                     @RequestParam String relatedUserId,
                                     @RequestParam Integer relationshipType) {
        return userRelationshipService.updateRelationship(userId, relatedUserId, relationshipType);
    }


    @GetMapping("/getCreatorInfo")
    public UserInfo getCreatorInfo(@RequestParam String userId, @RequestParam String creatorId) {
        User user = userService.getById(userId);
        UserRelationship userRelationship = userRelationshipService.findRelationship(userId, creatorId);
        return new UserInfo(
                user.getUserId(),
                user.getUserName(),
                user.getStatus(),
                user.getGender(),
                user.getAge(),
                user.getBirth(),
                user.getAvatarUrl(),
                userRelationship==null?0:userRelationship.getRelationshipType()
        );
    }
}
