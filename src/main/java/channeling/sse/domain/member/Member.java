package channeling.sse.domain.member;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false, length = 100)
    private String googleEmail; // 구글 이메일

    @Column
    private String profileImage; // 프로필 이미지

    @Column(length = 100)
    private String instagramLink; // 인스타 링크

    @Column(length = 100)
    private String tiktokLink; // 틱톡 링크

    @Column(length = 100)
    private String facebookLink; // 페이스북 링크

    @Column(length = 100)
    private String twitterLink; // 트위터 링크

    @Column(length = 50)
    private String googleId; // 구글 아이디 (로그인 구별을 위한..)


}

