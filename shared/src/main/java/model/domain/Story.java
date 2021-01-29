package model.domain;

import java.util.ArrayList;
import java.util.List;

public class Story {

    private List<Status> statusList = new ArrayList<>();

    public Story(List<Status> statusList) { this.statusList = statusList; }

    public Story() {}

    public List<Status> getStatusList() {
        return statusList;
    }

    public int getSize() { return statusList.size(); }

    public Status getStatusAt(int i) { return statusList.get(i); }
}
