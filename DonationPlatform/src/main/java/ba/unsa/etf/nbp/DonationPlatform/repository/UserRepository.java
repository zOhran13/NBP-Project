package ba.unsa.etf.nbp.DonationPlatform.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private static final String TABLE_NAME = "nbp.nbp_user";

    private final JdbcTemplate jdbcTemplate;
    public UserRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    // SQL queries as formatted strings
    private String getSelectQuery() {
        return String.format("SELECT * FROM %s", TABLE_NAME);
    }
}
