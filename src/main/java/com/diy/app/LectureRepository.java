package com.diy.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureRepository {
    private final Map<Long, Lecture> lectures = new HashMap<>();

    public Collection<Lecture> values() {
        return lectures.values();
    }

    public void save(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }
}
