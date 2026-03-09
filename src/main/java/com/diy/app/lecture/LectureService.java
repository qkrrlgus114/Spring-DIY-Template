package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureService {

    private static final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private static final AtomicLong seq = new AtomicLong();

    /*
     * 모든 강의 리스트 조회
     * */
    public List<Lecture> findAllLecture() {
        return new ArrayList<>(store.values());
    }

    /*
     * 강의 등록하기
     * */
    public void register(Lecture lecture) {
        validate(lecture);

        lecture.assignId(seq.incrementAndGet());
        store.put(lecture.getId(), lecture);
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
        Lecture lecture = store.get(id);
        if (lecture == null) {
            throw new IllegalArgumentException("강의가 존재하지 않습니다.");
        }

        store.remove(id);
    }

    /*
     * 강의 수정하기
     * */
    public void updateLecture(Lecture lecture) {
        if (lecture == null || lecture.getId() == null || store.get(lecture.getId()) == null) {
            throw new IllegalArgumentException("강의가 존재하지 않습니다.");
        }

        validate(lecture);

        store.put(lecture.getId(), lecture);
    }
}
