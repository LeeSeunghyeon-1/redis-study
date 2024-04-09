package domain.coupon.service;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.DistributeLock;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private static final String COUPON_KEY_PREFIX = "COUPON_";

    @Transactional
    public void decrease(Long couponId) {
        String key = COUPON_KEY_PREFIX + couponId;
        couponDecrease(key, couponId);
    }

    //쿠폰 차감 로직
    @DistributeLock(key = "#key")
    public void couponDecrease(String key, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);

        coupon.decrease();
    }
}
