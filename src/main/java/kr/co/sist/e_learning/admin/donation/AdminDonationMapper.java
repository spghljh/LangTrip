package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDonationMapper {
    List<DonationVO> selectDonations(@Param("search") DonationSearchDTO searchDTO, @Param("page") PageRequestDTO_donation pageRequestDTO);
    int countDonations(@Param("search") DonationSearchDTO searchDTO);
    int updateDonationMessageDeleted(String donationId);
}
