package com.example.apiserver.security.filter;

import com.example.apiserver.security.AccessTokenException;
import com.example.apiserver.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Map;

//클라이언트의 요청이 하나 오면 한 번만 동작하는 필터
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    //토큰의 유효성 검사를 위한 메서드
    private Map<String, Object> validateAccessToken(HttpServletRequest request){
        //헤더에서 토큰을 읽기
        String headerStr = request.getHeader("Authorization");

        //토큰에 Bearer 라는 문자열을 포함시켜서 전송하기 때문에 토큰이 있으면 8자 이상이므로
        //이 조건을 만족하지 않으면 예외 발생
        if(headerStr == null || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }
        //실제 토큰 가져오기
        String tokenType = headerStr.substring(0,6); //Bearer
        String tokenStr = headerStr.substring(7); //실제 토큰 문자열

        //타입 검사
        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            //토큰의 유효성 검사
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch(MalformedJwtException malformedJwtException){
            log.error("---MalformedJwtException--");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("---SignatureException--");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("---ExpiredJwtException--");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

    //필터로 동작하는 메서드
    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)
    throws IOException, ServletException {
        //클라이언트의 URI 확인
        String path = request.getRequestURI();
        ///api로 시작하지 않는 요청의 경우는 다음 필터로 넘기는데
        //다음 필터로 넘길 때 반드시 return을 해야 합니다.
        //return을 만날 때 까지 무조건 수행
        if(! path.startsWith("/api")){
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Token Check Filter...............");
        log.info("JWTUtil:" + jwtUtil);

        try{
            //토큰의 유효성 검사
            validateAccessToken(request);
            //다음 필터에게 처리를 넘김 - AOP - Filter(Interceptor, AOP)
            filterChain.doFilter(request, response);
        }catch(AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }

    }
}
