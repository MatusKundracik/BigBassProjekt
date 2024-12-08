package org.projekt;

public enum Factory {
    INSTANCE;

    private RybarDAO rybarDAO;
    private UlovokDAO ulovokDAO;

    public RybarDAO getRybarDAO() {
        if (rybarDAO == null) {
            rybarDAO = new MemoryRybarDAO();
        }
        return rybarDAO;
    }

    public UlovokDAO getUlovokDAO() {
        if (ulovokDAO == null) {
            ulovokDAO = new MemoryUlovokDAO();
        }
        return ulovokDAO;
    }
}

