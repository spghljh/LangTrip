package kr.co.sist.e_learning.admin.donation;

import lombok.Data;

@Data
public class DonationSearchDTO {
    private String searchType;
    private String searchKeyword;
    private String startDate;
    private String endDate;
}
