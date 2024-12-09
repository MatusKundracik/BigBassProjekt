package org.projekt;

import java.util.List;

public interface RevirDAO {
    List<Revir> getAll();

    void save(Revir revir);

    void vymazPodlaID(long id);

    List<Revir> najdiPodlaLokalita(String lokalita);
}
