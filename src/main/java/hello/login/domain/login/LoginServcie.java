package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServcie {
    private final MemberRepository memberRepository;

    /**
     * @param loginId
     * @param password
     * @return nullㅇㅣ면 로그인 실패
     */
    public Member login(String loginId, String password) {
        /*
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();
        if (member.getPassword().equals(password)) {
            return member;
        } else{
            return null;
        }*/

        /*
            위와 같은 코드
            optional. password가 없다면 Null을 반환한다
         */
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);


    }
}
