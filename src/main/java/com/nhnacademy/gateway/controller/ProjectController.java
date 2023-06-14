package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.SecurityUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class ProjectController {
    @GetMapping("/projects")
    public String getProjects(Principal principal){
        return "project/projectMain";
    }

}
