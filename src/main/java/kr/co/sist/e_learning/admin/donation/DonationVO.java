package kr.co.sist.e_learning.admin.donation;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DonationVO {
    private String donationId;
    private String senderId;
    private String recipientId;
    private String lectureTitle;
    private int amount;
    private String message;
    private LocalDateTime donationDate;
    private boolean messageDeleted;
}
