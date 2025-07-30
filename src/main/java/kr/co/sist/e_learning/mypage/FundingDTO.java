package kr.co.sist.e_learning.mypage;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingDTO {
    // 공통: 사용자 지갑 정보
    private Long walletSeq;
    private Integer totalMiles;
    private Integer donationAvailable;
    private Integer donatedMiles;
    private Integer receivedMiles;

    // 계좌 정보
    private String accountNum;
    private String holderName;
    private String bankCode;

    // 후원 내역 / 받은 내역
    private String donationSeq;
    private String courseSeq;
    private String courseTitle;
    private Integer donationAmount;
    private String message;
    private Timestamp createdAt;

    // 결제 내역
    private String paymentSeq;
    private String method;
    private Integer paymentAmount;
    private Timestamp paidAt;
    private String paymentStatus;
    private String name;
    private String receiptUrl;
}
