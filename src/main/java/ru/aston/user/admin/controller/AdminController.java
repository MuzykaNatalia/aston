package ru.aston.user.admin.controller;

import java.util.Collection;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.config.Create;
import ru.aston.config.Update;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.service.AdminService;
import static ru.aston.constant.Constant.HEADER_USER;
import static ru.aston.constant.Constant.PAGE_FROM_DEFAULT;
import static ru.aston.constant.Constant.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/all")
    public Collection<AdminDto> getAllAdmin(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        return adminService.getAllAdmin(from, size);
    }

    @GetMapping
    public AdminDto getAdminById(@RequestHeader(HEADER_USER) @Positive @NotNull Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDto createAdmin(@Validated(Create.class) @RequestBody @NotNull AdminDto admin) {
        return adminService.createAdmin(admin);
    }

    @PatchMapping
    public AdminDto updateAdmin(@RequestHeader(HEADER_USER) @Positive @NotNull Long adminId,
                                @Validated(Update.class) @RequestBody @NotNull AdminDto admin) {
        return adminService.updateAdmin(adminId, admin);
    }

    @DeleteMapping
    public void deleteAdmin(@RequestHeader(HEADER_USER) @Positive @NotNull Long adminId) {
        adminService.deleteAdmin(adminId);
    }
}
