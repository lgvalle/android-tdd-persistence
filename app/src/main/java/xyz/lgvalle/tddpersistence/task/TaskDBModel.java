package xyz.lgvalle.tddpersistence.task;

public class TaskDBModel {

    private final String name;
    private final long expiration;
    private final String listName;

    public TaskDBModel(String name, long expiration, String listName) {
        this.name = name;
        this.expiration = expiration;
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public long getExpiration() {
        return expiration;
    }

    public String getListName() {
        return listName;
    }
}
