package domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Entity(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //NOTE ) 쿠폰의 잔여개수 (test : 100개)
    private Long availableStock;

    public Coupon(String name, Long availableStock) {
        this.name = name;
        this.availableStock = availableStock;
    }

    @Transactional
    public void decrease() {
        log.info("감소 여부 확인 =======> {}", availableStock);
        validateStockCount();
        this.availableStock -= 1;
    }

    private void validateStockCount() {
        if (availableStock < 1) {
            throw new IllegalArgumentException();
        }
    }
}