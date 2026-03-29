package com.smart.community.gateway.filter;

import com.smart.common.constants.JwtConstants;
import com.smart.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 网关统一登录鉴权过滤器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    // 白名单接口
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/user/login",
            "/user/register",
            "/user/test"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 1.白名单放行
        for (String url : WHITE_LIST) {
            if (path.contains(url)) {
                return chain.filter(exchange);
            }
        }

        // 2.获取token
        String token = request.getHeaders().getFirst(JwtConstants.TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            return unauthorized(exchange);
        }

        // 3.校验token
        try {
            Claims claims = JwtUtil.parseToken(token);
            // 可把userId透传到下游header
            String userId = claims.get("userId").toString();
            ServerHttpRequest newReq = request.mutate()
                    .header("userId", userId)
                    .build();
            return chain.filter(exchange.mutate().request(newReq).build());
        } catch (Exception e) {
            return unauthorized(exchange);
        }
    }

    // 401未授权响应
    private Mono<Void> unauthorized(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
