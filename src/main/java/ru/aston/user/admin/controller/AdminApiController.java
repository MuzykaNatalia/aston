package ru.aston.user.admin.controller;

import java.util.Collection;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.annotations.*;
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
@RequestMapping("/api/admins")
@Api(value = "/api/admins", tags = "Admins Controller")
public class AdminApiController {
    private final AdminService adminService;

    @GetMapping("/all")
    @ApiOperation(value = "Get all admins")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of admins"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Collection<AdminDto> getAllAdmin(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        return adminService.getAllAdmin(from, size);
    }

    @GetMapping
    @ApiOperation(value = "Get an admin by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved admin"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public AdminDto getAdminById(@RequestHeader(HEADER_USER) @Positive @NotNull
                                     @ApiParam(name = "ID of the admin to retrieve", required = true)
                                     Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new admin")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Admin created successfully"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public AdminDto createAdmin(@Validated(Create.class) @RequestBody @NotNull
                                    @ApiParam(name = "Admin object to be created", required = true)
                                    AdminDto admin) {
        return adminService.createAdmin(admin);
    }

    @PatchMapping
    @ApiOperation(value = "Update an existing admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Admin updated successfully"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public AdminDto updateAdmin(@RequestHeader(HEADER_USER) @Positive @NotNull
                                    @ApiParam(name = "ID of the admin to update", required = true)
                                    Long adminId,
                                @Validated(Update.class) @RequestBody @NotNull
                                @ApiParam(name = "Updated admin object", required = true)
                                AdminDto admin) {
        return adminService.updateAdmin(adminId, admin);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete an existing admin")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Admin deleted successfully"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void deleteAdmin(@RequestHeader(HEADER_USER) @Positive @NotNull
                                @ApiParam(name = "ID of the admin to delete", required = true)
                                Long adminId) {
        adminService.deleteAdmin(adminId);
    }
}