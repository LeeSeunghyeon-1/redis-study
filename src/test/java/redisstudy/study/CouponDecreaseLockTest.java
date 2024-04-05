package redisstudy.study;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import domain.coupon.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Redisson Lock 쿠폰 차감 테스트")
@ExtendWith(MockitoExtension.class)
@Slf4j
class CouponDecreaseLockTest {

    /**
     * 시나리오 : 동시성 테스트를 위한 쿠폰 차감 테스트
     * 방법 : 100명의 사용자가 동시에 쿠폰을 차감하는 테스트
     * 기대 : 100명의 사용자가 동시에 쿠폰을 차감해도 쿠폰 재고가 0이 되어야 한다.
     */

    @MockBean
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;


    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon("할인쿠폰(100명 한정)", 100L);
//        couponRepository.save(coupon); //기존 코드
        coupon = couponRepository.save(coupon); //데이터베이스에 저장 후 저장돈 객체 반환
    }

    @AfterEach
    void teardown() {
        couponRepository.deleteAll();
    }

    @Test
    void 쿠폰차감_동시성_100명_테스트() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i=0; i<numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    couponService.decrease(coupon.getId());
                    log.info("쿠폰 잔여 개수 : {}", coupon.getAvailableStock());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon persistCoupon = couponRepository.findById(coupon.getId())
//                .orElseThrow(IllegalArgumentException::new);
                .orElseThrow(() -> new IllegalArgumentException("100개 소진 완료,,,,! 쿠폰이 존재하지 않습니다."));

        assertThat(persistCoupon.getAvailableStock()).isZero();
    }
}
