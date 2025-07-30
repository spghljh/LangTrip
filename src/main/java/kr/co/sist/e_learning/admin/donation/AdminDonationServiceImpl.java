package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminDonationServiceImpl implements AdminDonationService {

    private final AdminDonationMapper adminDonationMapper;

    @Override
    public PageResponseDTO_donation<DonationVO> getDonationList(DonationSearchDTO searchDTO, PageRequestDTO_donation pageRequestDTO) {
       
        int totalCount = adminDonationMapper.countDonations(searchDTO);
        
        List<DonationVO> donations = adminDonationMapper.selectDonations(searchDTO, pageRequestDTO);
      

        return PageResponseDTO_donation.<DonationVO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .list(donations)
                .totalCnt(totalCount)
                .build();
    }

    @Override
    public boolean deleteDonationMessage(String donationId) {
        log.info("Attempting to delete message for donationId: {}", donationId);
        int affectedRows = adminDonationMapper.updateDonationMessageDeleted(donationId);
        log.info("deleteDonationMessage affectedRows: {}", affectedRows);
        boolean success = affectedRows == 1;
        return success;
    }
}