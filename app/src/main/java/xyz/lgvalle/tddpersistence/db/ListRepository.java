package xyz.lgvalle.tddpersistence.db;

import xyz.lgvalle.tddpersistence.list.List;
import xyz.lgvalle.tddpersistence.list.ListStorage;

public class ListRepository {

    private final ListStorage listStorage;

    public ListRepository(ListStorage listStorage) {
        this.listStorage = listStorage;
    }

    public void persistList(List list) {
        listStorage.insert(list);
    }
}
