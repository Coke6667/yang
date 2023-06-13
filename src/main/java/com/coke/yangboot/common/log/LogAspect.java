package com.coke.yangboot.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.coke.yangboot.common.log.SysLog)")
    public void SysLog(){}


    @Before("SysLog()")
    public void Before(JoinPoint joinPoint){
        //获取HttpServletRequest对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog operation =method.getAnnotation(SysLog.class);
        log.info("function"+operation.functionName());
        log.info("method"+operation.methodName());
        log.info("==========请求信息==========");
        log.info("请求链接 : "+request.getRequestURL().toString());
        log.info("Http Method : "+request.getMethod());
        log.info("Class Method : "+joinPoint.getSignature().getDeclaringTypeName()+joinPoint.getSignature().getName());
        log.info("Ip : "+request.getRemoteAddr());
        log.info("Args : "+ Arrays.asList(joinPoint.getArgs()));
    }

    @Around("SysLog()")
    public Object Around(ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();//得到方法执行所需的参数
        try {
            log.info("aroundAdvice 前置");
            Object result = proceedingJoinPoint.proceed();//明确调用切入点方法（切入点方法）
            log.info("返回参数 :"+result.toString());
            return result;
        } catch (Throwable e) {
            log.info("aroundAdvice 异常");
            throw new RuntimeException(e);
        } finally {
            System.out.println("aroundAdvice 后置");
        }

    }
}