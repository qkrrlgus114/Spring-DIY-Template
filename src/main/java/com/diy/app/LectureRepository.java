package com.diy.app;

import java.util.Collection;
import java.util.Collections;
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

    public Long nextId() {
        return lectures.isEmpty() ? 1L : Collections.max(lectures.keySet()) + 1;
    }

    public Lecture findById(Long id) {
        return lectures.get(id);
    }

    public void delete(Long id) {
        lectures.remove(id);
    }
}
