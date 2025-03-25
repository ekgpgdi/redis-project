package dahye.project.redis;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionLoginController {

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
