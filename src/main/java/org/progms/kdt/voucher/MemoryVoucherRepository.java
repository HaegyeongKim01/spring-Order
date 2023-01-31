package org.progms.kdt.voucher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//Voucher repo와 Order repo를 메모리 기반으로 관리되도록 별도의 클래스로 뺀다.
@Repository
@Profile({"local", "default", "test"})
//@Primary
//@Qualifier("memory")   //다른 용도 Qualifier를 통해 JDBC, 이 클래스 구분
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)  //prototype으로 정의하면 다르게 나오고
    //singleton으로 하면 하나의 객체로 getBean이 된다. //default인 signleton이 좋다.
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)  //prototype으로 정의하면 다르게 나오고
public class MemoryVoucherRepository implements VoucherRepository /*, InitializingBean, DisposableBean*/ {   /*, InitializingBean, DisposableBean*/ //Life cycle 코드 작성시 필요
    //메모리상 관리 Hash map 사용
    private final Map<UUID, Voucher> storage = new ConcurrentHashMap<>();


    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        return Optional.ofNullable(storage.get(voucherId));   //null일 경우 empty되도록 처리
    }

    @Override
    public Voucher insert(Voucher voucher) {
        storage.put(voucher.getVoucherId(), voucher);
        return voucher;
    }

    //Life Cycle-----*-----
    //생명 주기 콜백-*--
//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("postConstruct called!");
//    }
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("afterPostConstruct calles ");
//    }
//
//    //소멸 생명 주기 콜백-*--
//    @PreDestroy
//    public void PreDestroy(){
//        System.out.println("PreDestroy called!!");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("destory called!!");
//    }
}
