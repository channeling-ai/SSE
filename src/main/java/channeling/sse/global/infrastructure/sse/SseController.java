package channeling.sse.global.infrastructure.sse;

import channeling.sse.domain.member.Member;
import channeling.sse.global.auth.annotation.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    private final SseService sseService;

    @GetMapping("/connect")
    public SseEmitter connect(@LoginMember Member loginMember) {

        String userId = String.valueOf(loginMember.getId());
        log.info("SSE CONNECT: {}", loginMember.getGoogleId());

        return sseService.connect(userId);
    }
}
