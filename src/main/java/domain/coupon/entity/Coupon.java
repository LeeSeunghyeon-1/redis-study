package domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long availableStock;

    public Coupon(String name, Long availableStock) {
        this.name = name;
        this.availableStock = availableStock;
    }

    public static Coupon of(String name, Long availableStock) {
        return new Coupon(name, availableStock);
    }

    public void decrease() {
        validateStockCount();
        this.availableStock -= 1;
    }

    private void validateStockCount() {
        if (availableStock < 1) {
            throw new IllegalArgumentException();
        }
    }
}