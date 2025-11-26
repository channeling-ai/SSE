package channeling.sse.global.auth.application;

import channeling.sse.domain.member.Member;
import channeling.sse.domain.member.repository.MemberRepository;
import channeling.sse.global.auth.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String googleId) throws UsernameNotFoundException {
        Member member = memberRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with googleId: " + googleId ));
        return new CustomUserDetails(member);
    }
}

