package renan.springsecurity.auth;

import java.util.Optional;

public interface UserDAO {

     Optional<User> selectUserByUsername(String username);
}
