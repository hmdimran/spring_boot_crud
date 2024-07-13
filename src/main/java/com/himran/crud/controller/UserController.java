package com.himran.crud.controller;

import com.himran.crud.model.User;
import com.himran.crud.model.UserDto;
import com.himran.crud.service.MailService;
import com.himran.crud.service.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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

    @Autowired
    private MailService mailService;

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
        user.setEmail(userDto.getEmail());
        user.setDesignation(userDto.getDesignation());
        user.setSalary(userDto.getSalary());
        user.setAbout(userDto.getAbout());
        user.setCreatedAt(date);
        user.setImageFileName(storeImageFileName);

        repository.save(user);

        if(!userDto.getEmail().isEmpty()){
            try{
                String toMail = userDto.getEmail();
                String subject = "Congratulations";
                String body = "Thanks For Connecting With Out Community";
                mailService.sendMail(toMail,subject,body);
            }catch (Exception e){
                System.out.println("Email Sending Problems in UserController "+e.getMessage());
            }
        }


        return "redirect:/users";
    }

    @GetMapping("/update")
    public String showUpdatePage(Model model, @RequestParam Long id){
        try{
            User user = repository.findById(id).get();
            model.addAttribute("user",user);

            UserDto userDto = new UserDto();

            userDto.setUser_name(user.getUser_name());
            userDto.setEmail(user.getEmail());
            userDto.setDesignation(user.getDesignation());
            userDto.setSalary(user.getSalary());
            userDto.setAbout(user.getAbout());

            model.addAttribute("userDto",userDto);

        }catch (Exception e){
            System.out.println("Update GET function" + e.getMessage());
            return "redirect:/users";
        }

        return "users/update";
    }


    @PostMapping("/update")
    public String updateUser(Model model,Long id ,@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult){
        try{
            User user = repository.findById(id).get();
            model.addAttribute("user",user);

            if(bindingResult.hasErrors()){
                return "users/update";
            }


            if(!userDto.getImageFileName().isEmpty()){
                //delete old image
                String uploadPath = "public/images/";
                //Save New Image

                MultipartFile image = userDto.getImageFileName();
                Date date = new Date();
                String storeImageFileName = date.getTime() + "_" + image.getOriginalFilename();
                try(InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream,Paths.get(uploadPath + storeImageFileName),StandardCopyOption.REPLACE_EXISTING);
                }

                user.setImageFileName(storeImageFileName);
            }

            user.setUser_name(userDto.getUser_name());
            user.setEmail(userDto.getEmail());
            user.setDesignation(userDto.getDesignation());
            user.setSalary(userDto.getSalary());
            user.setAbout(userDto.getAbout());

            repository.save(user);

        }catch (Exception e){
            System.out.println("Update Function Error " + e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String userDelete(@RequestParam Long id, HttpSession session){
        System.out.println(" Deleted ID is "+id);
       try{
           User user = repository.findById(id).get();
           repository.delete(user);
           session.setAttribute("delete","User Delete Done");
       }catch (Exception e){
           System.out.println("User Delete Function Error "+e.getMessage());
       }
       return "redirect:/users";
    }

}
