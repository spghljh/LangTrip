package kr.co.sist.e_learning.payment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentRequestDTO {

    private String impUid;           // PortOne 결제 고유 ID
    private String merchantUid;      // 가맹점 주문번호
    private Long walletSeq;          // FK → mile_wallet
    private String method;           // 결제 수단 (card, vbank, etc.)
    private String provider;         // PG사 코드
    private Long paymentAmount;      // 결제 금액
    private String paymentStatus;    // SUCCESS, FAIL 등
    private String currency;         // KRW 등
    private String channel;          // PortOne 채널 구분
    private String name;             // 상품명 (예: 마일 충전)
    private String receiptUrl;       // 영수증 URL
    private Long mileAmount;         // 적립될 마일
    private Double feeRate;          // 수수료율
    private Long userSeq;
    private Long paymentSeq;
    private String created_at;
}
