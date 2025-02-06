package com.zhoujh.lvtu.post.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhoujh.lvtu.utils.StringListHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("posts")
public class Post implements Serializable {
    @TableId
    private String postId; // 帖子ID
    private String postTitle; // 帖子标题
    private String postContent; // 帖子内容
    private String userId; // 发帖人ID
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间
    @TableField(typeHandler = StringListHandler.class)
    private List<String> picturePath; // 帖子图片路径, MyBatis Plus 会自动将 List<String> 转换为 JSON 格式并存储到 JSON 类型的列中,只需要确保实体类的字段和数据库中的列类型匹配。
    private int pictureCount; // 图片数量
    private int status; // 帖子状态 (1: 发布, 0: 草稿, 2: 已删除)
    @TableField(typeHandler = StringListHandler.class)
    private List<String> tags; // 标签列表
    private int likeCount; // 点赞数
    private int commentCount; // 评论数
    private int starCount; // 收藏数
    private int privacy; // 隐私设置 (1: 公开, 2: 私密, 3: 仅好友可见)

    public Post() {
        // 默认构造函数
    }
    public Post(String postId, String postTitle, String postContent, String userId, LocalDateTime createTime, LocalDateTime updateTime, List<String> picturePath, int pictureCount, int status, List<String> tags, int likeCount, int commentCount, int starCount, int privacy) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.picturePath = picturePath;
        this.pictureCount = pictureCount;
        this.status = status;
        this.tags = tags;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.starCount = starCount;
        this.privacy = privacy;
    }

    // Getters 和 Setters
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public List<String> getPicturePath() { return picturePath; }
    public void setPicturePath(List<String> picturePath) { this.picturePath = picturePath; }

    public int getPictureCount() { return pictureCount; }
    public void setPictureCount(int pictureCount) { this.pictureCount = pictureCount; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public int getStarCount() { return starCount; }
    public void setStarCount(int starCount) { this.starCount = starCount; }

    public int getPrivacy() { return privacy; }
    public void setPrivacy(int privacy) { this.privacy = privacy; }
}

