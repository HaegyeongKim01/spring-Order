package org.progms.kdt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.progms.kdt.customer.CustomerNamedJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 여러 Advice를 담고있다. 여러 Advice => methods
 *
 */
@Aspect
@Component  //Component Scan의 대상이 되도록 anotation 등록
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CustomerNamedJDBCRepository.class);

    //Pointcut을 정의해놓은 CoommonPointcut에 의해 동작된다.
//    @Around("org.progms.kdt.aop.CommonPointcut.repositoryInsertMethodPointcut()")    //인자 위치하는 class에 가면 pointcut들이 정의되어있다.
    @Around("@annotation(org.progms.kdt.aop.TrackTime)")        //TrackTime이라는 annotation 사용한 곳에 이 로직이 동작하게 된다.
    public Object log(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.info("Before method called. {}", jointPoint.getSignature().toString());

        var startTime =System.nanoTime();  //1 -> 1,000,000,000

        var result = jointPoint.proceed();
        var endTime = System.nanoTime() - startTime;
        logger.info("After method called with result => {} and time taken {} nanoseconds", result, endTime);

        return result;
    }


}
