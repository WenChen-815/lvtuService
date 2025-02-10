package com.zhoujh.lvtu.common.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujh.lvtu.common.mapper.HmsPushTokenMapper;
import com.zhoujh.lvtu.common.model.HmsPushToken;
import com.zhoujh.lvtu.common.service.HmsPushTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HmsPushTokenServiceImpl
        extends ServiceImpl<HmsPushTokenMapper, HmsPushToken>
        implements HmsPushTokenService {

    @Override
    public boolean saveOrUpdateToken(String userId, String hmsToken) {
        // 检查是否存在相同记录
        HmsPushToken exist = baseMapper.selectByUserAndToken(userId, hmsToken);

        if (exist == null) {
            // 新增记录
            HmsPushToken newToken = new HmsPushToken();
            newToken.setUserId(userId);
            newToken.setHmsToken(hmsToken);
            return this.save(newToken);
        } else {
            // 更新时间为最新（依靠数据库自动更新）
            return this.updateById(exist);
        }
    }

    @Override
    public List<String> getTokensByUserId(String userId) {
        List<HmsPushToken> tokens = baseMapper.selectByUserId(userId);
        List<String> tokensList = new ArrayList<>();
        for (HmsPushToken token : tokens) {
            tokensList.add(token.getHmsToken());
        }
        return tokensList;
    }

    @Override
    public boolean removeToken(String userId, String hmsToken) {
        LambdaQueryWrapper<HmsPushToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HmsPushToken::getUserId, userId)
                .eq(HmsPushToken::getHmsToken, hmsToken);
        return this.remove(wrapper);
    }


    public String createTokens(String userId){
        List<String> tokens = getTokensByUserId(userId);
        StringBuilder sb = new StringBuilder();
        for(int i = 0, len = tokens.size(); i < len; i++){
            if(i == len - 1){
                sb.append("\"").append(tokens.get(i)).append("\"");
            }else{
                sb.append("\"").append(tokens.get(i)).append("\",");
            }
        }
        return sb.toString();
    }
}
