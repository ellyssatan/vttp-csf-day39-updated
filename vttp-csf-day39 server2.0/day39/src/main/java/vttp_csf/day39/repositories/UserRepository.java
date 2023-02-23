package vttp_csf.day39.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp_csf.day39.models.User;
import static vttp_csf.day39.repositories.Queries.*;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    // find user
    public Optional<User> findUserByEmail(String email) {
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_EMPLOYEES_BY_EMAIL, email);

        if(!rs.next()) {
            return Optional.empty();
        }

        return Optional.of(User.create(rs));
    }

}
