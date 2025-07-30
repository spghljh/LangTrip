package kr.co.sist.e_learning.mileWallet;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MileWalletDTO {
    private Long walletSeq;
    private Long userSeq;
    private Long totalMiles;
    private Long donation_available;
    private Long donated_miles;
    private Long received_miles;

}
