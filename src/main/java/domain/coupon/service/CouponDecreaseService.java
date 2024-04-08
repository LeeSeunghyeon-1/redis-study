package domain.coupon.service;

import domain.coupon.entity.Coupon;
import domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.DistributeLock;

@Component
@RequiredArgsConstructor
public class CouponDecreaseService {
    private final CouponRepository couponRepository;

    @Transactional
    @DistributeLock(key = "#lockName")
    public void couponDecrease(String lockName, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);

        coupon.decrease();
    }

//    @Transactional
//    public void couponDecrease(Long couponId) {
//        Coupon coupon = couponRepository.findById(couponId)
//                .orElseThrow(IllegalArgumentException::new);
//
//        coupon.decrease();
//    }
}
