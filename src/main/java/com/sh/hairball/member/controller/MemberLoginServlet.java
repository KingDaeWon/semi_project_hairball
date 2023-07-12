package com.sh.hairball.member.controller;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.sh.hairball.member.model.service.MemberService;
import com.sh.hairball.member.model.vo.Member;

import java.io.IOException;

    @WebServlet("/member/memberLogin")
    public class MemberLoginServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        private final MemberService memberService = new MemberService();

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        	request.getRequestDispatcher("/WEB-INF/views/member/memberLogin.jsp").forward(request, response);
        }
        
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // 0. 인코딩처리
            request.setCharacterEncoding("utf-8");
            // 1. 사용자입력값 (memberId, password)
            String memberId = request.getParameter("memberId");
//            String password = animalUtil.getEncryptedPassword(request.getParameter("password"), memberId);
//            System.out.println(password);
            String saveId = request.getParameter("saveId");
            System.out.println("memberId = " + memberId);
//            System.out.println("password = " + password);
            System.out.println("saveId = " + saveId);

            // 2. 업무로직 - 로그인확인
            // 아이디로 db에서 조회 select * from member where member_id = ?
            // member객체가 null이 아니면서 비밀번호가 일치하면 로그인성공
            // member객체가 null이거나 비밀번호가 일치하지 않으면 로그인실패
            Member member = memberService.findById(memberId);
//		System.out.println("member@servlet = " + member);

            HttpSession session = request.getSession(); // request.getSession(true)와 동일.
//		System.out.println(session.getId());
//            && password.equals(member.getPassword())
            if(member != null ) {
                // 로그인 성공
                session.setAttribute("loginMember", member);
                // session.setAttribute("msg", "로그인에 성공했습니다.");

                // 아이디저장 쿠키처리
                // - Path : 쿠키를 사용할 url. 서버전송시 부모경로만 지정.
                //    - / 설정시 모든 요청에 사용.
                //    - /mvc 설정시 /mvc로 시작하는 모든 요청에 사용
                // - Session Cookie : setMaxAge설정하지 않은 경우. 접속한 동안만 클라이언트에 보관
                // - Persistent Cookie : setMaxAge설정한 경우. 지정한 시각까지만 클라이언트에 보관


                Cookie cookie = new Cookie("saveId", memberId);
                cookie.setPath(request.getContextPath()); // 쿠키를 사용할 url
                if(saveId != null) {
                    cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 유효기간 7일
                }
                else {
                    // 기존의 쿠키 삭제
                    cookie.setMaxAge(0); // 클라이언트 있던 쿠기의 만료기간을 0으로 변경함과 동시에 삭제
                }
                response.addCookie(cookie); // 응답 헤더 Set-Cookie : saveId=honggd

            }
            else {
                // 로그인 실패
//                session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
            }

            // 3. 응답처리
            response.sendRedirect(request.getContextPath() + "/"); // redirect를 통한 url변경

        }
}
