package hello.login.web.login;

import hello.login.domain.login.LoginServcie;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginServcie loginServcie;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

   // @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginServcie.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        /*
            DB의 결과값을 봐야만 알 수 있는 오류임
            따라서 이런 것들을 코드로 해주는게 낫다..
         */
        if (loginMember == null) {
            //reject : 글로벌 오류..
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }


        //로그인 성공 처리 TODO
        //쿠키에 시간 정보를 주지 않으면 세션쿠키(브라우저 종료시 모
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";
    }


    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginServcie.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        /*
            DB의 결과값을 봐야만 알 수 있는 오류임
            따라서 이런 것들을 코드로 해주는게 낫다..
         */
        if (loginMember == null) {
            //reject : 글로벌 오류..
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }


        //로그인 성공 처리 TODO
        //세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        sessionManager.createSession(loginMember, response);


        return "redirect:/";
    }


    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
      sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginServcie.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        /*
            DB의 결과값을 봐야만 알 수 있는 오류임
            따라서 이런 것들을 코드로 해주는게 낫다..
         */
        if (loginMember == null) {
            //reject : 글로벌 오류..
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }


        //로그인 성공 처리 TODO

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성 >> param default : true
        HttpSession session = request.getSession(); //false 를 넣는 경우 세션 없으면 null 반환
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);


        //세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        //sessionManager.createSession(loginMember, response);


        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }



}
