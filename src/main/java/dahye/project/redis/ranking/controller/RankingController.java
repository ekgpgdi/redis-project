package dahye.project.redis.ranking.controller;

import dahye.project.redis.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @PutMapping("")
    public Boolean setScore(@RequestParam String userId, @RequestParam int score) {
        return rankingService.setUserScore(userId, score);
    }

    @GetMapping("/user/{userId}")
    public Long getUserRanking(@PathVariable String userId) {
        return rankingService.getUserRanking(userId);
    }

    @GetMapping("/top")
    public List<String> getTopRanking(@RequestParam int limit) {
        return rankingService.getTopRanking(limit);
    }
}
