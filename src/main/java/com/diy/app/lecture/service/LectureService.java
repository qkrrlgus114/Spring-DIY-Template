package com.diy.app.lecture.service;

import com.diy.app.lecture.model.Lecture;
import com.diy.framework.web.server.bean.Autowired;
import com.diy.framework.web.server.bean.Component;
import com.diy.framework.web.server.repository.Repository;

import java.util.List;

@Component
public class LectureService {

    private final Repository<Lecture> lectureRepository;

    @Autowired
    public LectureService(Repository<Lecture> lectureRepository) {
        this.lectureRepository = lectureRepository;
    }
    /*
     * 모든 강의 리스트 조회
     * */

    public List<Lecture> findAllLecture() {
        return lectureRepository.findAll();
    }

    /*
     * 강의 등록하기
     * */
    public void register(Lecture lecture) {
        validate(lecture);
        lectureRepository.save(lecture);
    }

    private void validate(Lecture lecture) {
        if (lecture.getName() == null || lecture.getName().isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (lecture.getPrice() == null) {
            throw new IllegalArgumentException("가격은 필수입니다.");
        }
    }

    /*
     * 강의 삭제하기
     * */
    public void deleteLecture(long id) {
        Lecture lecture = lectureRepository.findById(id);
        if (lecture == null) {
            throw new IllegalArgumentException("강의가 존재하지 않습니다.");
        }

        lectureRepository.deleteById(id);
    }

    /*
     * 강의 수정하기
     * */
    public void updateLecture(Lecture lecture) {
        if (lecture == null || lecture.getId() == null || lectureRepository.findById(lecture.getId()) == null) {
            throw new IllegalArgumentException("강의가 존재하지 않습니다.");
        }

        validate(lecture);
        lectureRepository.update(lecture);
    }
}
