package channeling.sse.global.infrastructure.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private final SseEmitterRepository repository;
    private final SseService sseService;

    @Scheduled(fixedRate = 10000) // 10초 주기
    public void sendHeartbeat() {
        Map<String, SseEmitter> emitters = repository.getAll();

        emitters.forEach((userId, emitter) -> {
            try {
                sseService.send(userId, "ping", "heartbeat");
            } catch (Exception e) {
                emitter.complete();
                repository.delete(userId);
            }
        });
    }
}
