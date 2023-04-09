// package com.lwdevelop.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/redis")
// public class RedisController {

//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;

//     @GetMapping("/setRedisValue")
//     public String setRedisValue(@RequestParam(value = "key") String key, @RequestParam(value = "text") String text) {
//         try {
//             redisTemplate.opsForValue().set(key, text);
//             return "success";
//         } catch (Exception e) {
//             return e.toString();
//         }
//     }

//     @GetMapping("/getRedisValue")
//     public String getRedisValue(@RequestParam(value = "key") String key) {
//         try {
//             return (String) redisTemplate.opsForValue().get(key);
//         } catch (Exception e) {
//             return e.toString();
//         }
//     }

// }
