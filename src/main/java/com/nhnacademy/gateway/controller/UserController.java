package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.domain.user.StateName;
import com.nhnacademy.gateway.domain.user.Status;
import com.nhnacademy.gateway.exception.dataValidException;
import com.nhnacademy.gateway.request.UserRegisterRequest;
import com.nhnacademy.gateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;


    @GetMapping("/join")
    public String goJoinPage(){
        return "user/join";
    }

    @PostMapping("/join")
    public String doJoinUser(@Valid UserRegisterRequest request,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new dataValidException(bindingResult);
        }
        userService.registerUser(request);
        return "redirect:/login";
    }

    @GetMapping("/change/state/{userId}")
    public String modifyStatus(@PathVariable("userId") String id,
                               Model model){
        model.addAttribute("itemType", StateName.values());
        model.addAttribute("inStatus", new Status());
        model.addAttribute("userId", id);
        return "user/modifyForm";
    }

    @PostMapping("/change/state/{userId}")
    public String modifiedStatus(@PathVariable("userId") String id,
                                 Model model,
                                 Status inStatus){
        userService.updateUserStatus(id, inStatus);
        model.addAttribute("userId",id);
        return "user/logout_reLogin";
    }

}
