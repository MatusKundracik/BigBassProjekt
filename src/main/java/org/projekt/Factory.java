    package org.projekt;

    import Povolenie.MemoryPovolenieDAO;
    import Povolenie.PovolenieDAO;
    import Revir.MemoryRevirDAO;
    import Revir.RevirDAO;
    import Rybar.MemoryRybarDAO;
    import Rybar.RybarDAO;
    import Ulovok.MemoryUlovokDAO;
    import Ulovok.UlovokDAO;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.datasource.DriverManagerDataSource;

    //https://paz1c.ics.upjs.sk/prednasky/
    public enum Factory {

        INSTANCE;
        private JdbcTemplate jdbcTemplate;
        private RybarDAO rybarDAO;
        private UlovokDAO ulovokDAO;
        private PovolenieDAO povolenieDAO;
        private RevirDAO revirDAO;


        public RybarDAO getRybarDAO() {
            if (rybarDAO == null) {
                rybarDAO = new MemoryRybarDAO(getJdbcTemplate());
            }
            return rybarDAO;
        }

        public UlovokDAO getUlovokDAO() {
            if (ulovokDAO == null) {
                ulovokDAO = new MemoryUlovokDAO(getJdbcTemplate());
            }
            return ulovokDAO;
        }

        public PovolenieDAO getPovolenieDAO() {
            if (povolenieDAO == null) {
                povolenieDAO = new MemoryPovolenieDAO(getJdbcTemplate());
            }
            return povolenieDAO;
        }

        public RevirDAO getRevirDAO() {
            if (revirDAO == null) {
                revirDAO = new MemoryRevirDAO(getJdbcTemplate());
            }
            return revirDAO;
        }

        private JdbcTemplate getJdbcTemplate() {
            if (jdbcTemplate == null) {
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("org.sqlite.JDBC");
                    dataSource.setUrl("jdbc:sqlite:bigbass.db");
                jdbcTemplate = new JdbcTemplate(dataSource);
            }
            return jdbcTemplate;
        }
    }

