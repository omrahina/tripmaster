package com.tripmaster.tourguide.proxies;

import com.tripmaster.tourguide.beans.UserBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "users-microservice", url = "users:8081/users")
public interface UsersMicroserviceProxy {

    @PostMapping("/user")
    UserBean addUser(@RequestBody UserBean user);

}
