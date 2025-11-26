package channeling.sse.global.infrastructure.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String key, SseEmitter emitter) {
        emitters.put(key, emitter);
        return emitter;
    }

    public Optional<SseEmitter> get(String key) {
        return Optional.ofNullable(emitters.get(key));
    }

    public void delete(String key) {
        emitters.remove(key);
    }

    public Map<String, SseEmitter> getAll() {
        return emitters;
    }
}
