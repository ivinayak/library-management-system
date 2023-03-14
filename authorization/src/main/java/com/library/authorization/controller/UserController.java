package com.library.authorization.controller;

import com.library.authorization.model.MessageResponse;
import com.library.authorization.model.Role;
import com.library.authorization.model.User;
import com.library.authorization.repository.IUserRepository;
import com.library.authorization.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam(name = "id") int id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No user found!"));
    }

    @GetMapping("/get/non-admin-users")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> getNonAdminUsers(){
        Optional<Role> role = roleRepository.findById(2);
        Optional<List<User>> users = userRepository.findByRoleIdNot(role.get());
        if(users.isPresent()){
            return ResponseEntity.ok(users.get());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No user found!"));
    }

    @GetMapping("/email")
    public ResponseEntity<?> findByEmail(@RequestParam(name = "email") String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No user found!"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") int id){
        try{
            userRepository.deleteById(id);
            return ResponseEntity.ok().body(new MessageResponse("User removed successfully!"));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("Error while deleting user"));
        }
    }
}
