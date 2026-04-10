package org.example.common.feign;

import org.example.common.entity.User;
import org.example.common.feign.fallback.UserFeignFallback;
import org.example.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", path = "/users", fallback = UserFeignFallback.class)
public interface UserFeignClient {
    
    @GetMapping("/{id}")
    Result<User> getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/username/{username}")
    Result<User> getUserByUsername(@PathVariable("username") String username);
    
    @PostMapping
    Result<User> createUser(@RequestBody User user);
    
    @PutMapping("/{id}")
    Result<User> updateUser(@PathVariable("id") Long id, @RequestBody User user);
    
    @DeleteMapping("/{id}")
    Result<Void> deleteUser(@PathVariable("id") Long id);
}
