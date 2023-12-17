package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence = 0L; //static 사용

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        /*
        List<Member> all = findAll();
        for (Member m : all) {
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m);
            }
        }
        return Optional.empty(); //값이 없을 때 null로 반환하기보다 요즘에는 Optional이라는 "통"을 이용한다.
        */

        /*
            위 로직이랑 똑같은 로직
            1. list를 stream으로 가져옴
            2. filter >> for와 같은 기능, 같은 경우에만 다음으로 넘어가고 아니면 패스
            3. 일치하는 값의 첫번째를 찾음
      \  */
        return findAll().stream()
                    .filter(m -> m.getLoginId().equals(loginId))
                    .findFirst();

    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }


    public void clearStore(){
        store.clear();
    }

}
