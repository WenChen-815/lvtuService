package com.zhoujh.lvtu.common.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujh.lvtu.common.model.UserRelationship;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationshipRepository extends IService<UserRelationship> {
    /**
     * 添加关系
     * @param userId 用户ID
     * @param relatedUserId 关联用户ID
     * @param relationshipType 关系类型 (1: 好友, 2: 关注, 3: 拉黑)
     * @return 是否添加成功
     */
    boolean addRelationship(String userId, String relatedUserId, Integer relationshipType);

    /**
     * 修改用户关系
     * @param userId 用户ID
     * @param relatedUserId 关联用户ID
     * @param newRelationshipType 新的关系类型 (1: 好友, 2: 关注, 3: 拉黑)
     * @return 是否修改成功
     */
    int updateRelationship(String userId, String relatedUserId, Integer newRelationshipType);

    UserRelationship findRelationship(String userId, String relatedUserId);

}
