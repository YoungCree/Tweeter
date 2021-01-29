package model.domain;

import java.util.ArrayList;
import java.util.List;

public class Feed {

    private List<Status> statusList = new ArrayList<>();

    public Feed(List<Status> statusList) {
        this.statusList = statusList;
    }

    public Feed() {}

    public List<Status> getStatusList() {
        return statusList;
    }

    public int getSize() { return statusList.size(); }

    public Status getStatusAt(int i) {return  statusList.get(i); }
}
