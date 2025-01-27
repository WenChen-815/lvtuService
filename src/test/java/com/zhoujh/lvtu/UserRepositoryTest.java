package com.zhoujh.lvtu;

import com.zhoujh.lvtu.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserService userService;
//    @Test
//    // 测试新增
//    public void testSave() {
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("123456");
//        user.setEmail("test@example.com");
//        User savedUser = userRepository.save(user);
//        System.out.println("Saved user id: " + savedUser.getId());
//    }
//    @Test
//    // 测试查询所有
//    public void testFindAll() {
//        List<User> userList = userRepository.findAll();
//        for (User user : userList) {
//            System.out.println("User: " + user.getUsername());
//        }
//    }
//    @Test
//    // 测试根据ID查询
//    public void testFindById() {
//        Integer userId = 1; // 假设存在ID为1的用户，根据实际情况调整
//        User user = userRepository.findById(userId).orElse(null);
//        if (user!= null) {
//            System.out.println("Found user: " + user.getUsername());
//        }
//    }
//    @Test
//    // 测试删除
//    public void testDelete() {
//        Integer userId = 2; // 假设要删除ID为1的用户，根据实际情况调整
//        userRepository.deleteById(userId);
//        System.out.println("Deleted user with id: " + userId);
//    }
}
