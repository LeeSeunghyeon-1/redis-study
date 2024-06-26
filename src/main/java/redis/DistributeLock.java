package redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

//락을 위한 커스텀 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    /**
     * 락의 이름
     */
    String key();

    /**
     * 락의 시간 단위 (seconds 설정 상태)
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락을 기다리는 시간 (default - 5s)
     * 락 획득을 위해 waitTime 만큼 대기한다
     */
    long waitTime() default 10L;

    /**
     * 락 임대 시간 (default - 3s)
     * 락을 획득한 이후 leaseTime 이 지나면 락을 해제한다
     */
    long leaseTime() default 6L;
}
