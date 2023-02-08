package org.progms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


class CalculatorImpl implements Calculator{

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}

/**
 * 특정 Business Logic 제공해주는
 */
interface Calculator {
    int add(int a, int b);

}

class LoggingInvocationHandler implements InvocationHandler{
    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);


    private final Object target;

    LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    //실질적으로 호출이 되고 이 method가 실제로 target method를 invoke실행시킨다.
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("{} executed in {}", method.getName(), target.getClass().getCanonicalName());
        return method.invoke(target, args);
    }
}

public class JdkProxyTest {
    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    /**
     * Calculator에서 만들어진 것은 InvocationHandler가 적용이 된 proxy객체가 만들어지고 그것을 proxyInstance에 받아서 실행시킨다.
     * 그리하여 method invoke를 통해 target Object의 add 메소드 실행
     * @param args
     */
    public static void main(String[] args) {
       var calculator = new CalculatorImpl();
       Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(    //proxy객체 생성
                LoggingInvocationHandler.class.getClassLoader(),
                new Class[] {Calculator.class},
                new LoggingInvocationHandler(calculator));  //두 번째 인자로 target class의 interface를 줘야함
        var result = proxyInstance.add(1, 2);  //invoke 실행 -> method invoke를 통해 target Object의 add 메소드 실행
        log.info("Add -> {} ", result);
    }
}
