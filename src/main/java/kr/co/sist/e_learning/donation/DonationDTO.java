package kr.co.sist.e_learning.donation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationDTO {
    private String lectureTitle;
    private String instructorName;
    private int amount;
    private String message;
}
