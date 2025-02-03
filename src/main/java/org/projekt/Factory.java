    package org.projekt;

    import Povolenie.SQLPovolenieDAO;
    import Povolenie.PovolenieDAO;
    import Revir.SQLRevirDAO;
    import Revir.RevirDAO;
    import Rybar.SQLRybarDAO;
    import Rybar.RybarDAO;
    import Ulovok.SQLUlovokDAO;
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
                rybarDAO = new SQLRybarDAO(getJdbcTemplate());
            }
            return rybarDAO;
        }

        public UlovokDAO getUlovokDAO() {
            if (ulovokDAO == null) {
                ulovokDAO = new SQLUlovokDAO(getJdbcTemplate());
            }
            return ulovokDAO;
        }

        public PovolenieDAO getPovolenieDAO() {
            if (povolenieDAO == null) {
                povolenieDAO = new SQLPovolenieDAO(getJdbcTemplate());
            }
            return povolenieDAO;
        }

        public RevirDAO getRevirDAO() {
            if (revirDAO == null) {
                revirDAO = new SQLRevirDAO(getJdbcTemplate());
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

