package channeling.sse.global.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtUtilImpl implements  JwtUtil {
    /** JWT 서명에 사용할 비밀키 */
    @Value("${jwt.secret}")
    private String secret;
    private static final String GOOGLE_CLAIM = "googleId";

    /** HTTP Authorization 헤더의 토큰 앞에 붙는 접두사 */
    private static final String BEARER = "Bearer ";

    /** HTTP 요청/응답 헤더에 사용할 액세스 토큰 키 이름 */
    @Value("${jwt.access.header}")
    private String accessHeader;

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .or(() -> {
                    throw new JwtHandler("Access Token 이 존재하지 않습니다.");
                })
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""))
                .or(() -> {
                    throw new JwtHandler("Access Token 형식이 올바르지 않습니다.");
                });
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (TokenExpiredException ex) {
            throw new JwtHandler("만료된 JWT 토큰입니다.");

        } catch (Exception e) {
            throw new JwtHandler("유효하지 않은 Token입니다.");

        }
    }

    @Override
    public Optional<String> extractGoogleId(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(accessToken)
                    .getClaim(GOOGLE_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

}
