package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    @GetMapping("{username}")
//    public ResponseEntity<?> getAllJournalEntriesofUser(@PathVariable String username){
//        User  user = userService.getUsername(username);
//        List<JournalEntry> all = user.getJournalEntries();
//        if(all != null){
//            return new ResponseEntity<>(all, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }




    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.getUsername(username);

        if (userInDb != null) {
            // username change is optional; be careful: changing username will also change login id
            if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                userInDb.setUsername(user.getUsername());
            }

            // If password provided in request, treat it as RAW and re-hash it
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                userService.updatePassword(userInDb, user.getPassword());
            } else {
                userService.saveUser(userInDb);
            }

            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
