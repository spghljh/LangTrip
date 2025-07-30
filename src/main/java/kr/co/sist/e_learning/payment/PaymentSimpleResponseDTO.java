package kr.co.sist.e_learning.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentSimpleResponseDTO {
    private boolean success;
    private String message;
}
