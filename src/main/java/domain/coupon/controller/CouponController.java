package domain.coupon.controller;

import domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
//@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/test")
    public void decrease(@RequestParam(name = "id") Long id) {
        log.info("coupon-id-check : {}", id);
        couponService.decrease(id);
    }

}
