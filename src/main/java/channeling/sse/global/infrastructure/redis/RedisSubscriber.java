package channeling.sse.global.infrastructure.redis;

import channeling.sse.global.infrastructure.sse.SseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SseService sseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody());
            RedisMessage redisMessage = objectMapper.readValue(body, RedisMessage.class);

            System.out.println("📩 Redis → SSE: " + body);

            sseService.send(redisMessage.getUserId(), redisMessage.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
