package com.studentmanage.bigbang.aspect;

import com.studentmanage.bigbang.model.entity.OperationLog;
import com.studentmanage.bigbang.repository.OperationLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * 操作日志 AOP 切面
 * 自动拦截 Controller 层方法，记录操作人、IP、时间
 * 对比宇宙版手动调用 logService.log()，此方式零侵入
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final OperationLogRepository logRepo;

    public LogAspect(OperationLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @Pointcut("execution(* com.studentmanage.bigbang.controller.*.*(..)) " +
              "&& !execution(* com.studentmanage.bigbang.controller.AuthController.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;

        try {
            String method = joinPoint.getSignature().getName();
            String username = extractUsername(joinPoint);
            String ip = getClientIp();

            OperationLog logEntry = new OperationLog();
            logEntry.setUsername(username != null ? username : "anonymous");
            logEntry.setOperation(method);
            logEntry.setDetail(String.format("%s (耗时 %dms)", method, elapsed));
            logEntry.setIpAddress(ip);
            logEntry.setCreatedAt(LocalDateTime.now());
            logRepo.save(logEntry);
        } catch (Exception e) {
            log.warn("日志记录失败: {}", e.getMessage());
        }
        return result;
    }

    private String extractUsername(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Principal) {
                return ((Principal) arg).getName();
            }
        }
        return null;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) ip = request.getHeader("X-Real-IP");
                if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
                return ip;
            }
        } catch (Exception ignored) {}
        return "unknown";
    }
}
