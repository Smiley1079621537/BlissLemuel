package me.lemuel.adore.bean.movie;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by lemuel on 2017/2/24.
 */


public class Movie extends RealmObject {
    private int count;
    private int start;
    private int total;
    private String title;
    private RealmList<SubjectsBean> subjects;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(RealmList<SubjectsBean> subjects) {
        this.subjects = subjects;
    }
}
