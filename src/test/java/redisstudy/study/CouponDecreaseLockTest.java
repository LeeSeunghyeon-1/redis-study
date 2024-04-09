package redisstudy.study;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import domain.coupon.service.CouponDecreaseService;
import domain.coupon.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class CouponDecreaseLockTest {

    /**
     * 시나리오 : 동시성 테스트를 위한 쿠폰 차감 테스트
     * 방법 : 100명의 사용자가 동시에 쿠폰을 차감하는 테스트
     * 기대 : 100명의 사용자가 동시에 쿠폰을 차감해도 쿠폰 재고가 0이 되어야 한다.
     */

    @Autowired
    private CouponDecreaseService couponDecreaseService;

    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        // 공유할 쿠폰 생성 및 저장
        coupon = couponRepository.save(new Coupon("할인 쿠폰(100명 한정)", 100L));
    }

//    @AfterEach
//    void teardown() {
//        couponRepository.deleteAll();
//    }

    @Test
    public void 쿠폰차감_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    // 각 스레드에서는 동일한 쿠폰을 참조하여 차감
                    couponDecreaseService.couponDecrease("할인 쿠폰(100명 한정)", coupon.getId());
//                    couponDecreaseService.couponDecrease("할인 쿠폰(100명 한정)", 10L);
                    log.info("{}번째 쿠폰의 개수 : {} // ID 확인 : {}", finalI, coupon.getAvailableStock(), coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 모든 스레드가 종료되고 나서 최종 쿠폰 상태를 확인합니다.
        Coupon finalCoupon = couponRepository.findById(coupon.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(finalCoupon.getAvailableStock()).isZero();
    }
}
