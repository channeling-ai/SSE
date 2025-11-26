package channeling.sse.global.infrastructure.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedisMessage {
    private String userId;
    private String message;
}

