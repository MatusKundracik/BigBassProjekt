package org.projekt;

import java.util.List;

public interface PovolenieDAO {
    List<Povolenie> getAll();

    void save(Povolenie povolenie);

    void vymazPodlaID(long id);

    List<Povolenie> najdiPodlaRybarID(long rybarId);
}
