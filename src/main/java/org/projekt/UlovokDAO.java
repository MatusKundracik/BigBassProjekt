package org.projekt;

import java.util.List;

public interface UlovokDAO {
    List<Ulovok> getAll();

    void save(Ulovok ulovok);

    void vymazPodlaID(int id);
}
