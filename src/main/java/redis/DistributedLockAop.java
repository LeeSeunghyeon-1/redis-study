package redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final String REDISSON_KEY_PREFIX = "RLOCK_";

    //CHECK ) AOP 이용해서 트랜잭션 분리를 위함
    private final AopForTransaction aopForTransaction;
    //CHECK ) Redis 기반의 분산 객체 및 서비스 제공을 위함
    private final RedissonClient redissonClient;

    //CHECK ) DistributeLock 어노테이션을 사용한 메소드에 대한 AOP 설정
    @Around("@annotation(redis.DistributeLock)") //NOTE 1) 어노테이션 명시 방법
//    @Around("execution(* redis.DistributedLockAop.lock(..))") //NOTE 2) 패키지 경로 명시 방법
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //@DistributeLock  annotaion 을 가져옴
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);
        //@DistributeLock 에 전달한 key 를 가져오기 위해 SpringEL 표현식 파싱
        String key = REDISSON_KEY_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributeLock.key());    // (2)
        //Redission 해당 락의  RLock 인터페이스 가져옴
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(), distributeLock.timeUnit());
            if(!available) {
                return false;
            }
            //@DistributeLock 이 선언된 메소드의 로직 수행 (별도 트랜잭션을 분리)
            return aopForTransaction.proceed(joinPoint);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        } finally {
            //종료 혹은 예외 발생시 finally 에서 Lock 을 해제함
            rLock.unlock();
        }
    }



}
