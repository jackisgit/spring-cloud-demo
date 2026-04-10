package org.example.common.feign.fallback;

import org.example.common.entity.User;
import org.example.common.feign.UserFeignClient;
import org.example.common.result.Result;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements UserFeignClient {
    
    @Override
    public Result<User> getUserById(Long id) {
        return Result.error("用户服务暂时不可用");
    }
    
    @Override
    public Result<User> getUserByUsername(String username) {
        return Result.error("用户服务暂时不可用");
    }
    
    @Override
    public Result<User> createUser(User user) {
        return Result.error("用户服务暂时不可用");
    }
    
    @Override
    public Result<User> updateUser(Long id, User user) {
        return Result.error("用户服务暂时不可用");
    }
    
    @Override
    public Result<Void> deleteUser(Long id) {
        return Result.error("用户服务暂时不可用");
    }
}
