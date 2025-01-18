package com.zhoujh.lvtu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhoujh.lvtu.common.model.User;
import com.zhoujh.lvtu.common.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * QueryWrapper常用API
 * eq( ) :  等于 =
 * ne( ) :  不等于 <> 或者 !=
 * gt( ) :  大于 >
 * ge( ) :  大于等于  >=
 * lt( ) :  小于 <
 * le( ) :  小于等于 <=
 * between ( ) :  BETWEEN 值1 AND 值2
 * notBetween ( ) :  NOT BETWEEN 值1 AND 值2
 * in( ) :  in
 * notIn( ) ：not in
 * like() : like 模糊查询，左右都加%
 * RightLike()  :只有右边加%
 * LeftLike():只有左边加%
 */
@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;
//    @Test
//    public void test02() {
//        User user = User.builder()
//                .username("hhh")
//                .email("153@qq.com")
//                .password("123")
//                .build();
//        int insert = userMapper.insert(user);
//        System.out.println(insert);
//    }
//
//    @Test
//    public void test01() {
//        User user = userMapper.selectById(1);
//        System.out.println(user);
//    }

    @Test
    public void test05(){
        int page=2;
        int pageSize=4;
        //构建分页对象
        IPage<User> pageInfo =  new Page<>(page, pageSize);
        //pageInfo==userIPage-->true，这两个对象是同一个,地址相同
        //null为无条件查询
//        IPage<User> userIPage = userMapper.selectPage(pageInfo, null);
        //TODO:因为这两是同一个对象，所以可以直接不定义一个新对象
        userMapper.selectPage(pageInfo,null);
        //获取分页参数
        long total =pageInfo.getTotal();//获取总记录数
        long pages =pageInfo.getPages();//获取总页数
        long size = pageInfo.getSize();//获取当前页大小
        long current= pageInfo.getCurrent();//获取当前的页数
        List<User> users =pageInfo.getRecords();//获取当前页的对象集合

    }
    @Test
    public void test06(){
        //要求：查询用户中姓名包含"伤"，密码为"123456",且年龄为19或者25或者29，查询结果按照年龄降序排序；
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //设置查询条件
        //TODO:条件之间默认使用and关键字
        //第一个参数是数据库字段名
        wrapper.like("username","jj")
                .eq("password","123");
//                .in("age",Arrays.asList(19,25,29))
//                .orderByDesc("age");
        List<User> users = userMapper.selectList(wrapper);

    }
//    @Test
//    public void test07(){
//        //"lisi"或者年龄大于23的用户信息；
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_name","lisi")
//                .or()//使用or关键字关联条件，默认失and
//                .gt("age",23);
//        List<User> users = userMapper.selectList(wrapper);
//    }

    /**
     * 模糊查询常用方法
     * like("表列名","条件值"); 作用：查询包含关键字的信息，底层会自动添加匹配关键字，比如:%条件值%
     * likeLeft("表列名","条件值"); 作用：左侧模糊搜索，也就是查询以指定条件值结尾的数据，比如:%条件值
     * likeRight("表列名","条件值");作用：右侧模糊搜索，也就是查询以指定条件值开头的数据，比如:条件值%
     */
    @Test
    public void test08(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("username","jj");
        List<User> users = userMapper.selectList(wrapper);
    }
}
