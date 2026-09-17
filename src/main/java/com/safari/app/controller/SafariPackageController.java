package com.safari.app.controller;

import com.safari.app.model.SafariPackage;
import com.safari.app.service.SafariPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SafariPackageController {

    @Autowired
    private SafariPackageService safariPackageService;

    @GetMapping("/packages")
    public String listPackages(Model model) {
        model.addAttribute("packages", safariPackageService.getAllPackages());
        return "tourist-packages"; // loads tourist-packages.html
    }

    @GetMapping("/admin/packages")
    public String adminPackages(Model model) {
        model.addAttribute("packages", safariPackageService.getAllPackages());
        return "admin-packages"; // loads admin-packages.html
    }

    @PostMapping("/admin/packages/add")
    public String addPackage(@ModelAttribute SafariPackage safariPackage) {
        safariPackageService.savePackage(safariPackage);
        return "redirect:/admin/packages";
    }

    @GetMapping("/admin/packages/delete/{id}")
    public String deletePackage(@PathVariable Long id) {
        safariPackageService.deletePackage(id);
        return "redirect:/admin/packages";
    }
}
