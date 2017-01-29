package xyz.lgvalle.tddpersistence.task;

import java.util.Date;

public class Task {
    private final String name;
    private final Date expiration;
    private final String listName;

    public Task(String name, Date expiration, String listName) {
        this.name = name;
        this.expiration = expiration;
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getListName() {
        return listName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        if (expiration != null ? !expiration.equals(task.expiration) : task.expiration != null)
            return false;
        return listName != null ? listName.equals(task.listName) : task.listName == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        result = 31 * result + (listName != null ? listName.hashCode() : 0);
        return result;
    }
}
