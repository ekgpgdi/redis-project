package dahye.project.redis.ranking;

import dahye.project.redis.ranking.service.RankingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

@SpringBootTest
public class RankingTest {

    /*
        SortedSet Redis 를 활용하면 얼마나 빨라지는가?
        1000000개의 데이터 중 특정 유저의 순위 가져오는 속도 2ms, 상위 10개 데이터 가져오는 속도 1ms
     */

    @Autowired
    private RankingService rankingService;

    @Test
    @DisplayName("Rank(599409) - Took 2 ms, Range - Took 1 ms")
    void redisUserGetRank() {
        rankingService.getTopRanking(1); // 초기 redis 연결 시간을 고려하지 않기 위해 의미 없는 코드 추가

        Instant before = Instant.now();
        Long userRank = rankingService.getUserRanking("user_100");
        Duration elapsed = Duration.between(before, Instant.now());
        System.out.printf("Rank(%d) - Took %d ms", userRank, elapsed.getNano() / 1000000);

        before = Instant.now();
        rankingService.getTopRanking(10);
        elapsed = Duration.between(before, Instant.now());
        System.out.printf("Range - Took %d ms", elapsed.getNano() / 1000000);
    }

    @Test
    @DisplayName("ArrayList 에 1000000 데이터 추가 : 613ms ")
    void inMemorySortPerformance() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            int score = (int) (Math.random() * 1000000); // 0 ~ 999999
            list.add(score);
        }

        Instant before = Instant.now();
        Collections.sort(list);

        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println(elapsed.getNano() / 1000000 + " ms");
    }

    @Test
    @DisplayName("SortedSet 에 1000000 데이터 추가 ")
    void insertScore() {
        for (int i = 0; i < 1000000; i++) {
            int score = (int) (Math.random() * 1000000); // 0 ~ 999999
            String userId = "user_" + i;
            rankingService.setUserScore(userId, score);
        }

        Instant before = Instant.now();

        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println(elapsed.getNano() / 1000000 + " ms");
    }
}
