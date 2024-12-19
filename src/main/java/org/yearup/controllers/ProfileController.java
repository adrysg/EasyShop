package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao)
    {
        this.profileDao = profileDao;
    }

    @GetMapping("")
    public Profile getProfile(@RequestParam int userId){
        return profileDao.getByUserId(userId);
    }

    @PutMapping("")
    public Profile updateProfile(@RequestBody Profile profile){
        profileDao.updateProfile(profile);
        return profile;
    }

}
