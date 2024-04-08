package domain.coupon;

import domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

//    @PostMapping("/decrease")
//    public void decrease(Long couponId) {
//        couponService.decrease(couponId);
//    }

}
