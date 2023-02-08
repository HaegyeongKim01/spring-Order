package org.progms.kdt.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {


    @Pointcut("execution(public * org.progms.kdt..*Service(..))")
    public void servicePublicMethodPointcut() {}

    @Pointcut("execution(public * org.progms.kdt..*Repository.*(..))")
    public void repositoryMethodPointcut() {}


    @Pointcut("execution(public * org.progms.kdt..*Repository.insert(..))")
    public void repositoryInsertMethodPointcut() {}


}
