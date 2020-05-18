package com.zagalabs.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home Web Application Controller
 * @author jgarrido
 */
@Controller
public class HomeController {

	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("test", "Joaquin");
        return "home";
    }
}