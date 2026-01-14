package com.rest_api.apigateway.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RateLimiter extends OncePerRequestFilter {

    private final StringRedisTemplate template;
    private final RedisTemplate<Object, Object> redisTemplate;

    public RateLimiter(StringRedisTemplate stringRedisTemplate, RedisTemplate<Object, Object> redisTemplate) {
        this.template = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

//    simple rate limiter
//    to tell the client that more that 5 attempts to access
//    or hit the api will cause them a 30 secs waiting
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String apiKey=request.getHeader("X-API-KEY");
        log.info(apiKey);
        if(apiKey.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Key");
            return;
        }
        Long cnt=template.opsForValue().increment("rate_limit_"+apiKey);

        log.info("Request Count = {}", cnt);
        log.info("Actual cnt= {} ",template.opsForValue().get("rate_limit_"+apiKey));

        if(cnt!=null && cnt!=1L){
            template.expire("rate_limit_"+apiKey,30, TimeUnit.SECONDS);
        }
        if(cnt>5){
            response.getWriter().write("Too many Requests");
            response.setStatus(429);
            log.info("Response : {}",response);
            return;
        }
        filterChain.doFilter(request,response);

    }



}
