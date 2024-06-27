package com.utcn.service;

import com.utcn.model.Question;
import com.utcn.model.User;
import com.utcn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> getAllUsers(){
        return (List<User>) this.userRepository.findAll();
    }

    public User getUserById(Long id){
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    public User getUserByName(String username) {
        return userRepository.findByName(username);
    }


    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

        existingUser.setName(updatedUser.getName());

        return userRepository.save(existingUser);
    }

    public String deleteUser(Long id){
        try{
            this.userRepository.deleteById(id);
            return "User deleted";
        } catch (EmptyResultDataAccessException ex) {
            return "User with id " + id + " not found.";
        } catch (Exception e) {
            return "Failed to delete user " + id + " due to an error: " + e.getMessage();
        }
    }
}
