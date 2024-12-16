    package org.projekt;

    import Povolenie.MemoryPovolenieDAO;
    import Povolenie.PovolenieDAO;
    import Revir.MemoryRevirDAO;
    import Revir.RevirDAO;
    import Rybar.MemoryRybarDAO;
    import Rybar.RybarDAO;
    import Ulovok.MemoryUlovokDAO;
    import Ulovok.UlovokDAO;

    public enum Factory {
        INSTANCE;

        private RybarDAO rybarDAO;
        private UlovokDAO ulovokDAO;
        private PovolenieDAO povolenieDAO;
        private RevirDAO revirDAO;

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

        public PovolenieDAO getPovolenieDAO() {
            if (povolenieDAO == null) {
                povolenieDAO = new MemoryPovolenieDAO();
            }
            return povolenieDAO;
        }

        public RevirDAO getRevirDAO() {
            if (revirDAO == null) {
                revirDAO = new MemoryRevirDAO();
            }
            return revirDAO;
        }
    }

