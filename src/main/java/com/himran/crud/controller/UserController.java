package com.himran.crud.controller;

import com.himran.crud.model.User;
import com.himran.crud.model.UserDto;
import com.himran.crud.service.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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
    @GetMapping("/create")
    public String showCreateUser(Model model){
        UserDto dto = new UserDto();
        model.addAttribute("userDto",dto);
        return "users/create_user";
    }
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult){
        if(userDto.getImageFileName().isEmpty()){
            bindingResult.addError(new FieldError("userDto","imageFileName","The Image File Is Required"));
        }
        if(bindingResult.hasErrors()){
            return "users/create_user";
        }

        //save image file
        MultipartFile images = userDto.getImageFileName();
        Date date = new Date();
        String storeImageFileName = date.getTime() + "_" + images.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try(InputStream inputStream = images.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir,storeImageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            
        } catch (IOException e) {
            System.out.println("Exception " +e.getMessage());
        }

        User user = new User();
        user.setUser_name(userDto.getUser_name());
        user.setDesignation(userDto.getDesignation());
        user.setSalary(userDto.getSalary());
        user.setAbout(userDto.getAbout());
        user.setCreatedAt(date);
        user.setImageFileName(storeImageFileName);

        repository.save(user);


        return "redirect:/users";
    }

}
