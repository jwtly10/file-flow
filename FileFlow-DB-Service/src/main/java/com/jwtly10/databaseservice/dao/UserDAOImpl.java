package com.jwtly10.databaseservice.dao;

import com.jwtly10.common.models.User;
import com.jwtly10.databaseservice.service.SupabaseService;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO<User> {

    final Logger log = org.slf4j.LoggerFactory.getLogger(UserDAOImpl.class);

    private final SupabaseService supabaseService;

    public UserDAOImpl(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @Override
    public List<User> list() {
        return null;
    }

    @Override
    public void create(User user) {
        supabaseService.createUser(user);
    }

    @Override
    public Optional<User> get(String email) {
        User user = null;
        try {
            user = supabaseService.getUser(email);
        } catch (Exception e) {
            log.error("Failed to get user: " + e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public int update(User user, int id) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
