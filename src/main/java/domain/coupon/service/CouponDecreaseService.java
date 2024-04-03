package domain.coupon.service;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.DistributeLock;

@Service
@RequiredArgsConstructor
public class CouponDecreaseService {
    private final CouponRepository couponRepository;

    @DistributeLock(key = "#key")
    public void couponDecrease(String key, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);

        coupon.decrease();
    }
}
