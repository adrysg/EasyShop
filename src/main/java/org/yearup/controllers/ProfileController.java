package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;



    @GetMapping("")
    public Profile getProfileById(Principal principal){

        String username = principal.getName();
        User user = userDao.getByUserName(username);

        Profile profile = profileDao.getByUserId(user.getId());
        return profile;
    }

    @PutMapping("")
    public Profile updateProfile(@RequestBody Profile profile){
        profileDao.updateProfile(profile);
        return profile;
    }

}
