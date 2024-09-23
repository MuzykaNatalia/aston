package ru.aston.user.admin.controller;

import java.util.Collection;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.aston.config.Create;
import ru.aston.config.Update;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.service.AdminService;

import static ru.aston.constant.Constant.PAGE_FROM_DEFAULT;
import static ru.aston.constant.Constant.PAGE_SIZE_DEFAULT;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public String showHomeAdminForm() {
        return "admin-home";
    }

    @GetMapping("/all")
    public String getAllAdmin(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                              @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size,
                              Model model) {
        Collection<AdminDto> admins = adminService.getAllAdmin(from, size);
        model.addAttribute("admins", admins);
        model.addAttribute("admin", new AdminDto());
        return "admins-list";
    }

    @GetMapping("/get")
    public String showGetAdminForm() {
        return "admin-get";
    }

    @GetMapping("/search")
    public String getAdminById(@Positive @NotNull Long adminId, Model model) {
        AdminDto admin = adminService.getAdminById(adminId);
        model.addAttribute("admin", admin);
        return "admin-details";
    }

    @GetMapping("/add")
    public String showAddAdminForm(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "admin-add";
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAdmin(@Validated(Create.class) @ModelAttribute @NotNull AdminDto adminDto,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("admin", adminDto);
            return "admin-add";
        }
        adminService.createAdmin(adminDto);
        model.addAttribute("admin", new AdminDto());
        return "admin-add";
    }

    @GetMapping("/edit-admin")
    public String showEditAdminFormPage() {
        return "admin-edit";
    }

    @GetMapping("/edit")
    public String showAEditAdminForm(@RequestParam("adminId") @Positive @NotNull Long adminId, Model model) {
        model.addAttribute("adminId", adminId);
        model.addAttribute("admin", adminService.getAdminById(adminId));
        return "admin-update";
    }

    @PostMapping("/update")
    public String updateAdmin(@Positive @NotNull Long adminId,
                              @Validated(Update.class) @ModelAttribute AdminDto adminDto, Model model) {
        adminService.updateAdmin(adminId, adminDto);
        model.addAttribute("admin", new AdminDto());
        return "admin-home";
    }

    @GetMapping("/delete")
    public String showDeleteAdminForm() {
        return "admin-delete";
    }

    @PostMapping("/delete")
    public String deleteAdmin(@Positive @NotNull Long adminId, Model model) {
        adminService.deleteAdmin(adminId);
        model.addAttribute(0);
        return "admin-delete";
    }
}
