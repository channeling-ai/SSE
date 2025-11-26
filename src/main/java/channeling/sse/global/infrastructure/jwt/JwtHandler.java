package channeling.sse.global.infrastructure.jwt;


import org.springframework.security.core.AuthenticationException;

public class JwtHandler extends AuthenticationException {
    public JwtHandler(String message) {
        super(message);
    }
}
