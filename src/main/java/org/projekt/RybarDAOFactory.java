package org.projekt;

public enum RybarDAOFactory {
    INSTANCE;

    private RybarDAO rybarDAO;

    public RybarDAO getRybarDAO() {
        if (rybarDAO == null) {
            rybarDAO = new MemoryRybarDAO();
        }
        return rybarDAO;
    }
}

