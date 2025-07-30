package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;

public interface AdminDonationService {
    PageResponseDTO_donation<DonationVO> getDonationList(DonationSearchDTO searchDTO, PageRequestDTO_donation pageRequestDTO);
    boolean deleteDonationMessage(String donationId);
}
