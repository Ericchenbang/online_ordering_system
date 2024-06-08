package com.Test1.orderSystemTest.user;

import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List<Users> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public Users findById(@PathVariable Integer userId){
        Optional <Users> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody Users user){

        userRepository.save(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{userId}")
    public void update(@RequestBody Users user, @PathVariable Integer userId){
        userRepository.save(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    private void deleteById(@PathVariable Integer userId){
        userRepository.deleteById(userId);
    }


}
