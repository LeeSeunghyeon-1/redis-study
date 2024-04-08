package domain.coupon.service;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.DistributeLock;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponDecreaseService {
    private final CouponRepository couponRepository;
    private static final String COUPON_KEY_PREFIX = "COUPON_";
    @Transactional
    @DistributeLock(key = "#lockName")
    public void couponDecrease(String lockName, Long couponId) {
        log.info("쿠폰 차감 서비스 시작");
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);

        log.info("쿠폰 차감 서비스 진행 중");
        coupon.decrease();
    }

    @Transactional
    public void decrease(Long couponId) {
        String key = COUPON_KEY_PREFIX + couponId;
        couponDecrease(key, couponId);
    }
}
