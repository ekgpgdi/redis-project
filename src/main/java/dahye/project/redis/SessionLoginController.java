package dahye.project.redis;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionLoginController {

    /*
        java -Dserver.port=8081 -jar redis-project-0.0.1-SNAPSHOT.jar
        java -Dserver.port=8080 -jar redis-project-0.0.1-SNAPSHOT.jar

        서로 다른 포트로 어플리케이션을 실행하고 요청을 진행해도 같은 세션 ID 로 값을 받아올 수 있음
     */

    @GetMapping("/login")
    public String login(HttpSession session,
                        @RequestParam String name) {
        session.setAttribute("name", name);
        return "saved";
    }

    @GetMapping("/myName")
    public String myName(HttpSession session) {
        return (String) session.getAttribute("name");
    }
}
