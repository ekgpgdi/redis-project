package dahye.project.redis.ranking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingService {
    private final StringRedisTemplate redisTemplate;

    private static final String LEADERBOARD_KEY = "leaderBoard";

    public boolean setUserScore(String userId, int score) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(LEADERBOARD_KEY, userId, score);

        return true;
    }

    public Long getUserRanking(String userId) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Long userRank = zSetOperations.reverseRank(LEADERBOARD_KEY, userId);

        return userRank == null ? 0L : userRank + 1L;
    }

    public List<String> getTopRanking(int limit) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.reverseRange(LEADERBOARD_KEY, 0, limit - 1).stream().toList();
    }
}
