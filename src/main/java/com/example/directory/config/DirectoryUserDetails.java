package com.example.directory.config;

import com.example.directory.model.UserAccount;
import com.example.directory.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryUserDetails implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public DirectoryUserDetails(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserAccount> users = userAccountRepository.findByEmail(username);
        if(users.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user: " + username);
        } else {
            UserAccount user = users.get(0);
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), new ArrayList<>()
            );
        }
    }
}
