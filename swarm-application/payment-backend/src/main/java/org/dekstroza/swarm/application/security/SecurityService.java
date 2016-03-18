package org.dekstroza.swarm.application.security;

import com.dekstroza.swarm.security.entities.User;

public interface SecurityService {

    User findUserByUsername(final String username);
    void insertUser(final User user);


}
