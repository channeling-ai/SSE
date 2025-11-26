package channeling.sse.global.infrastructure.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

    private final SseEmitterRepository repository;

    public SseEmitter connect(String userId) {
        log.info("📡 SSE Connect Request - userId: {}", userId);


        SseEmitter emitter = new SseEmitter(0L);
        repository.save(userId, emitter);

        emitter.onCompletion(() -> {
            log.debug("SSE Completed - userId: {}", userId);
            removeEmitter(userId);
        });
        emitter.onTimeout(() -> {
            log.warn("⏱️ SSE Timeout - userId: {}", userId);
            removeEmitter(userId);
        });
        emitter.onError(e -> {
            log.error("❌ SSE Error - userId: {}, error: {}", userId, e.getMessage());
            removeEmitter(userId);
        });

        // 최초 연결 이벤트
        send(userId, "CONNECTED: " + System.currentTimeMillis(), "connected");
        log.info("🔗 SSE Connected Successfully - userId: {}", userId);

        return emitter;
    }

    public void send(String userId, String message) {
        send(userId, message, "message");
    }

    public void send(String userId, String message, String eventName) {
        repository.get(userId).ifPresentOrElse(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(message));
                log.info("➡️ SSE Message Sent - userId: {}, event: {}", userId, eventName);

            } catch (Exception e) {
                log.error("⚠️ Failed to send SSE message - userId: {}, error: {}", userId, e.getMessage());

                removeEmitter(userId);
            }
        }, () -> {
            log.warn("❌ No emitter found for userId: {}", userId);

        });
    }

    private void removeEmitter(String userId) {
        repository.delete(userId);
        log.info("🗑 Emitter removed - userId: {}", userId);
    }
}
