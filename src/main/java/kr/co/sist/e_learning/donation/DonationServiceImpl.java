package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.co.sist.e_learning.mileWallet.MileWalletDao;
import kr.co.sist.e_learning.mileWallet.MileWalletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DonationServiceImpl implements DonationService {

    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);

    @Autowired
    private DonationJpaRepository donationRepository;

    @Autowired
    private MileWalletDao mileWalletDao;

    @Autowired
    private LectureService lectureService;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public DonationDTO donate(Long sponsorUserSeq, DonationRequestDTO dto) {

        logger.debug("Donation process started for sponsorUserSeq: {}", sponsorUserSeq);
        
        // 1. 후원자 지갑 및 잔액 확인
        Long sponsorWalletSeq = getOrCreateSponsorWalletSeq(sponsorUserSeq);
        logger.debug("Retrieved sponsorWalletSeq: {}", sponsorWalletSeq);

        // 2. 후원자의 보유 마일 확인 및 부족 시 예외 처리
        Long currentUserMiles = getUserMiles(sponsorUserSeq);
        if (currentUserMiles < dto.getAmount()) {
            throw new IllegalArgumentException("보유 마일이 부족합니다.");
        }

        // 3. 강의 정보 확인 및 예외 처리
        LectureDetailDTO lecture = lectureService.getLectureDetail(dto.getLectureNo());
        if (lecture == null) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다.");
        }

        Long instructorUserSeq = lecture.getInstructorUserSeq();
        Long instructorWalletSeq = getOrCreateInstructorWalletSeq(instructorUserSeq);

        // 4. 마일 차감 및 적립
        processMiles(sponsorWalletSeq, instructorWalletSeq, dto.getAmount());
        entityManager.flush();

        // 5. 후원 기록 저장
        saveDonationRecord(dto, sponsorWalletSeq, instructorWalletSeq, dto.getAmount());

        // 6. 결과 DTO 반환
        return new DonationDTO(lecture.getCourseTitle(), lecture.getInstructorNickname(), dto.getAmount().intValue(), dto.getMessage());
    }

    /**
     * 후원자 지갑 확인 및 없으면 생성
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Long getOrCreateSponsorWalletSeq(Long userSeq) {
        Long walletSeq = mileWalletDao.selectWalletSeqByUserSeq(userSeq);
        if (walletSeq == null) {
            MileWalletDTO walletDTO = new MileWalletDTO();
            walletDTO.setUserSeq(userSeq);
            walletDTO.setTotalMiles(0L);
            walletDTO.setDonation_available(0L);
            walletDTO.setDonated_miles(0L);
            walletDTO.setReceived_miles(0L);
            mileWalletDao.insertMileWallet(walletDTO);
            walletSeq = mileWalletDao.selectWalletSeqByUserSeq(userSeq);
        }
        return walletSeq;
    }

    /**
     * 강사 지갑 확인 및 없으면 생성
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Long getOrCreateInstructorWalletSeq(Long instructorUserSeq) {
        Long instructorWalletSeq = mileWalletDao.selectWalletSeqByUserSeq(instructorUserSeq);
        if (instructorWalletSeq == null) {
            MileWalletDTO walletDTO = new MileWalletDTO();
            walletDTO.setUserSeq(instructorUserSeq);
            walletDTO.setTotalMiles(0L);
            walletDTO.setDonation_available(0L);
            walletDTO.setDonated_miles(0L);
            walletDTO.setReceived_miles(0L);
            mileWalletDao.insertMileWallet(walletDTO);
            instructorWalletSeq = mileWalletDao.selectWalletSeqByUserSeq(instructorUserSeq);
        }
        return instructorWalletSeq;
    }

    /**
     * 마일 차감 및 적립을 처리하는 메소드 (독립적인 트랜잭션)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processMiles(Long sponsorWalletSeq, Long instructorWalletSeq, long amount) {
        // 후원자 마일 차감
        mileWalletDao.updateDonationAvailable(sponsorWalletSeq, -amount); // donation_available 차감
        mileWalletDao.addDonatedMiles(sponsorWalletSeq, amount); // donated_miles 증가

        // 강사 마일 적립
        mileWalletDao.addReceivedMiles(instructorWalletSeq, amount); // 받은 마일 증가
    }

    /**
     * 후원 기록을 저장하는 메소드
     */
    @Transactional
    public void saveDonationRecord(DonationRequestDTO dto, Long sponsorWalletSeq, Long instructorWalletSeq, long amount) {
        logger.debug("Saving donation record for sponsorWalletSeq: {}, instructorWalletSeq: {}", sponsorWalletSeq, instructorWalletSeq);
        DonationEntity donation = new DonationEntity();
        Long seq = ((Number) entityManager
                .createNativeQuery("SELECT SEQ_donation.NEXTVAL FROM DUAL")
                .getSingleResult()).longValue();
        donation.setDonationSeq(String.valueOf(seq));

        donation.setCourseSeq(dto.getLectureNo());
        donation.setSponsorWalletSeq(sponsorWalletSeq);

        // 추가: 후원자 지갑이 유효한지 다시 확인
        if (!mileWalletDao.checkWalletExists(sponsorWalletSeq)) {
            throw new IllegalArgumentException("후원자 지갑이 유효하지 않습니다.");
        }

        donation.setInstructorWalletSeq(instructorWalletSeq);

        // 추가: 강사 지갑이 유효한지 확인
        if (!mileWalletDao.checkWalletExists(instructorWalletSeq)) {
            throw new IllegalArgumentException("강사 지갑이 유효하지 않습니다.");
        }

        donation.setAmount(amount);
        donation.setMessage(dto.getMessage());

        entityManager.flush();
        entityManager.persist(donation);
    }

    @Override
    public Long getUserMiles(Long userSeq) {
        Long walletSeq = mileWalletDao.selectWalletSeqByUserSeq(userSeq);
        if (walletSeq == null) {
            return 0L;
        }
        Long miles = mileWalletDao.selectCurrentMiles(walletSeq);
        return miles == null ? 0 : miles;
    }
}
