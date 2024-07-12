package com.himran.crud.controller;

import com.himran.crud.model.User;
import com.himran.crud.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping({"","/"})
    public String showProductList(Model model){
        List<User> allUsers = repository.findAll();
        model.addAttribute("all_users",allUsers);
        return "users/index";
    }
}
