package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        //세션생성 테스트

        //1세션 생성
        //response는 인터페이스 구현체.. 톰캣에서 주는 객체라 테스트가 어려움 >> 스프링에서 제공해주는 mockrespon사용
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        //2요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId=1322reafdasfdagd...

        //세션조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);


        //세션만료
        sessionManager.expire(request);
        Object expire = sessionManager.getSession(request);
        Assertions.assertThat(expire).isNull();
    }





}
