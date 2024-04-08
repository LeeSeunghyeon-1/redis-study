package redisstudy.study;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import domain.coupon.service.CouponDecreaseService;
import domain.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
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
class CouponLockTest {
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
        coupon = new Coupon("100개 한정 테스트 진행", 100L);
        couponRepository.save(coupon);
    }
    @Test
    void 쿠폰차감_분산락_적용_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출 (락의 key는 쿠폰의 name으로 설정)
                    couponDecreaseService.couponDecrease(coupon.getName(), coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon persistCoupon = couponRepository.findById(coupon.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(persistCoupon.getAvailableStock()).isZero();
        System.out.println("잔여 쿠폰 개수 = " + persistCoupon.getAvailableStock());
    }
}
