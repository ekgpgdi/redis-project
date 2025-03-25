package dahye.project.redis;

import dahye.project.redis.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class RedisProjectApplication implements CommandLineRunner {

    private final ChatService chatService;

    public static void main(String[] args) {
        SpringApplication.run(RedisProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Application started.. ");

        chatService.enterChatRoom("chat1");
    }
}
