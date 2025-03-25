package dahye.project.redis.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalApiService {

    public String getUserName(String userId) {
        // 외부 서비스나 DB 호출이라고 가정 - 지연이 있다고 가정

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        log.info("Getting user name from other service ..");
        return switch (userId) {
            case "A" -> "Adam";
            case "B" -> "Bob";
            default -> "";
        };

    }

    /*
      Spring 의 캐시 추상화
      - CacheManager 를 통해 일반적인 캐시 인터페이스 구현(다양한 캐시 구현체가 존재)
      @CacheAble : 메소드에 캐시 적용 (Cache-Aside 패턴)
      @CachePut : 메소드의 리턴값에 캐시를 설정
      @CacheEvict : 메소드의 키값을 기반으로 캐시를 삭제
   */
    @Cacheable(cacheNames = "userAgeCache", key = "#userId")
    public int getUserAge(String userId) {
        // 외부 서비스나 DB 호출이라고 가정 - 지연이 있다고 가정

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        log.info("Getting user age from other service ..");

        return switch (userId) {
            case "A" -> 28;
            case "B" -> 32;
            default -> 0;
        };
    }
}
