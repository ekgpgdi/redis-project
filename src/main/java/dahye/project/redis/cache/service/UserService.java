package dahye.project.redis.cache.service;

import dahye.project.redis.cache.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserService {
    private final ExternalApiService externalApiService;

    private final StringRedisTemplate redisTemplate;

    /*
        Cache-Aside 방식으로 캐싱 적용
     */

    public UserProfile getUserProfile(String userId) {
        String userName = null;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cacheName = ops.get("nameKey:" + userId);

        if (cacheName != null) {
            userName = cacheName;
        } else {
            userName = externalApiService.getUserName(userId);
            ops.set("nameKey:" + userId, userName, 5, TimeUnit.SECONDS);
        }

        int age = externalApiService.getUserAge(userId);

        return new UserProfile(userName, age);
    }
}
