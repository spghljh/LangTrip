package kr.co.sist.e_learning.donation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    private final LectureMapper lectureMapper;

    @Override
    public LectureDetailDTO getLectureDetail(String courseSeq) {
        return lectureMapper.selectLectureDetailByCourseSeq(courseSeq);
    }
}
