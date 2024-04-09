package redisstudy.study;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import domain.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Redisson Lock 쿠폰 차감 테스트")
@SpringBootTest
class CouponDecreaseLockTest {

    /**
     * 시나리오 : 동시성 테스트를 위한 쿠폰 차감 테스트
     * 방법 : 100명의 사용자가 동시에 쿠폰을 차감하는 테스트
     * 기대 : 100명의 사용자가 동시에 쿠폰을 차감해도 쿠폰 재고가 0이 되어야 한다.
     */

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon("C0001", 100L);
        couponRepository.save(coupon);
    }

    @AfterEach
    void teardown() {
        couponRepository.deleteAll();
    }

    @Test
    void 쿠폰차감_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i=0; i<numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    couponService.decrease(coupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon persistCoupon = couponRepository.findById(coupon.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(persistCoupon.getAvailableStock()).isZero();
    }
}