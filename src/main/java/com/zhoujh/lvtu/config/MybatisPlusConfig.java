package com.zhoujh.lvtu.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zhoujh.lvtu.utils.ListTypeHandler;
import com.zhoujh.lvtu.utils.StringListHandler;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MybatisPlusConfig {
    /**
     * 用于批量注册mp的插件bean
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //构建批量注册插件的对象
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //构建分页插件对象,并指定mysql数据库
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        //配置每页的大小，-1为无上限
        paginationInnerInterceptor.setMaxLimit(-1L);
        //将分页插件注册
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);

        return mybatisPlusInterceptor;
    }

    @Bean
    public StringListHandler stringListHandler() {
        return new StringListHandler();
    }
}
