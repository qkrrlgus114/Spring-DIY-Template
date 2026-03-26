package com.diy.app.lecture.repository;

import com.diy.app.lecture.model.Lecture;
import com.diy.framework.web.server.bean.Component;
import com.diy.framework.web.server.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LectureRepository implements Repository<Lecture> {

    private static final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private static final AtomicLong seq = new AtomicLong();

    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    public void save(Lecture lecture) {
        lecture.assignId(seq.incrementAndGet());
        store.put(lecture.getId(), lecture);
    }

    public Lecture findById(long id) {
        return store.get(id);
    }

    public void deleteById(long id) {
        store.remove(id);
    }

    public void update(Lecture lecture) {
        store.put(lecture.getId(), lecture);
    }
}
