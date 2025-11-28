package channeling.sse.domain.member.repository;

import channeling.sse.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGoogleId(String googleId);

}
