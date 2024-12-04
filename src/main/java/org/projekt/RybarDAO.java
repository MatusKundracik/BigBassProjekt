package org.projekt;

import java.util.List;

public interface RybarDAO {
    List<Rybar> getAll();

    void save(Rybar rybar);

    List<Rybar> najdiPodlaMena(String meno);

    void vymazPodlaID(int id);
}
