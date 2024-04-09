package domain.coupon.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class CouponDTO {
    private Long id;
    private String name;
    private Long availableStock;

    public CouponDTO(Long id, String name, Long availableStock) {
        this.id = id;
        this.name = name;
        this.availableStock = availableStock;
    }
}
