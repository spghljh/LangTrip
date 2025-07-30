package kr.co.sist.e_learning.donation;

public interface DonationService {
    DonationDTO donate(Long userSeq, DonationRequestDTO dto);
    Long getUserMiles(Long userSeq);
  
  
}

