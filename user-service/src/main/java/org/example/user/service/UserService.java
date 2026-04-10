package org.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.entity.User;

public interface UserService extends IService<User> {
    
    User getUserByUsername(String username);
}
