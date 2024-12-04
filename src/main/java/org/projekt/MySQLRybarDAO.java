package org.projekt;


import java.util.Collections;
import java.util.List;

public class MySQLRybarDAO implements RybarDAO {

    @Override
    public List<Rybar> getAll() {
        return Collections.emptyList();
    }

    @Override
    public void save(Rybar rybar) {

    }

    @Override
    public List<Rybar> najdiPodlaMena(String meno) {
        return Collections.emptyList();
    }

    @Override
    public void vymazPodlaID(int id) {

    }
}
