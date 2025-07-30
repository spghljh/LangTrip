package kr.co.sist.e_learning.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessResponseDTO {
    private boolean success;
    private String message;
    private String redirectUrl;
    private Long paymentAmount;
    private Long mileAmount;
}
