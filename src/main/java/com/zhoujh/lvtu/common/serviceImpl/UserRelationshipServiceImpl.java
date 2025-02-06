package com.zhoujh.lvtu.common.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.common.mapper.UserRelationshipMapper;
import com.zhoujh.lvtu.common.model.UserRelationship;
import com.zhoujh.lvtu.common.service.UserRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserRelationshipServiceImpl extends ServiceImpl<UserRelationshipMapper, UserRelationship>
        implements UserRelationshipService {
    private final int UNFOLLOWED = 0;
    private final int FOLLOWED = 1;
    private final int FOLLOW_EACH_OTHER = 2;
    private final int BLOCKED = 3;
    @Autowired
    private UserRelationshipMapper userRelationshipMapper;

    @Override
    public boolean addRelationship(String userId, String relatedUserId, Integer relationshipType) {
        UserRelationship relationship = new UserRelationship();
        relationship.setId(UUID.randomUUID().toString());
        relationship.setUserId(userId);
        relationship.setRelatedUserId(relatedUserId);
        relationship.setRelationshipType(relationshipType);
        relationship.setCreateTime(LocalDateTime.now());

        return this.save(relationship); // MyBatis Plus 提供的保存方法
    }

    @Override
    public int updateRelationship(String userId, String relatedUserId, Integer newRelationshipType) {
        // 根据 userId 和 relatedUserId 查找关系记录
        UserRelationship existingRelationship = this.lambdaQuery()
                .eq(UserRelationship::getUserId, userId)
                .eq(UserRelationship::getRelatedUserId, relatedUserId)
                .one();
        UserRelationship existingRelationship1 = this.lambdaQuery()
                .eq(UserRelationship::getUserId, relatedUserId)
                .eq(UserRelationship::getRelatedUserId, userId)
                .one();
        switch (newRelationshipType) {
            case 0:
                // 取关 用户的关系为关注或互关
                if (existingRelationship != null) {
                    // 互关状态 把对方的状态改为关注 用户的关系移除
                    if(existingRelationship1 !=null && existingRelationship1.getRelationshipType() == this.FOLLOW_EACH_OTHER){
                        existingRelationship1.setRelationshipType(this.FOLLOWED);
                        this.updateById(existingRelationship1);
                    }
                    this.removeById(existingRelationship.getId());
                    return this.UNFOLLOWED;
                } else {
                    return -1;
                }
            case 1:
                // 用户未关注对方，但对方已关注用户，修改为互关
                if (existingRelationship1 != null && existingRelationship1.getRelationshipType() == this.FOLLOWED) {
                    existingRelationship1.setRelationshipType(this.FOLLOW_EACH_OTHER);
                    this.updateById(existingRelationship1);
                    return this.addRelationship(userId, relatedUserId, this.FOLLOW_EACH_OTHER) ? this.FOLLOW_EACH_OTHER: -1;
                } else { // 用户未关注对方，对方也未关注用户，则添加关系
                    return this.addRelationship(userId, relatedUserId, newRelationshipType) ? this.FOLLOWED: -1;
                }
            default:
                return -1;
        }
    }

    // 查找一条关系记录
    @Override
    public UserRelationship findRelationship(String userId, String relatedUserId) {
        return this.lambdaQuery()
                .eq(UserRelationship::getUserId, userId)
                .eq(UserRelationship::getRelatedUserId, relatedUserId)
                .one();
    }
}
