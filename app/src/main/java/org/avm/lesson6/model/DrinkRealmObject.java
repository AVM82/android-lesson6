package org.avm.lesson6.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class DrinkRealmObject extends RealmObject {
    @Required
    private String name;
    private long timeLastStart;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeLastStart() {
        return timeLastStart;
    }

    public void setTimeLastStart(long timeLastStart) {
        this.timeLastStart = timeLastStart;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
