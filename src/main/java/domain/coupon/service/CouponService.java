package domain.coupon.service;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.DistributeLock;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private static final String COUPON_KEY_PREFIX = "COUPON_";
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
