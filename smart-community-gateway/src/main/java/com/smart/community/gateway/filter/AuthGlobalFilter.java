package com.smart.community.gateway.filter;

import com.smart.common.constants.JwtConstants;
import com.smart.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 网关统一登录鉴权过滤器
 */
@Component
@Slf4j
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

        // 调试日志：打印当前请求路径
        log.info("=== AuthGlobalFilter 执行 ===");
        log.info("请求路径: {}", path);
        log.info("白名单列表: {}", WHITE_LIST);

        // 1.白名单放行
        for (String url : WHITE_LIST) {
            if (path.equals(url) || path.startsWith(url + "/")) {
                log.info(" 放行：【{}】 ", url);
                return chain.filter(exchange);
            }
        }
        log.info("需要Token认证：【{}】 ", path);
        // 2.获取token
        String token = request.getHeaders().getFirst(JwtConstants.TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            return unauthorized(exchange, "未携带Token，请登录");
        }

        // 3.校验token
        try {
            Claims claims = JwtUtil.parseToken(token);
            String userId = claims.get("userId").toString();

            // 把userId传到下游服务
            ServerHttpRequest newReq = request.mutate()
                    .header("userId", userId)
                    .build();

            return chain.filter(exchange.mutate().request(newReq).build());
        } catch (Exception e) {
            return unauthorized(exchange, "Token无效或已过期");
        }
    }

    /**
     * 统一返回 401 + JSON 错误信息（标准规范）
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 统一JSON返回体（和你的项目Result结构一致）
        String result = String.format("""
                {
                    "code": 401,
                    "msg": "%s",
                    "data": null
                }
                """, msg);

        DataBuffer buffer = response.bufferFactory()
                .wrap(result.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
