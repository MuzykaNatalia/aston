package ru.aston.user.admin.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.repository.AdminRepository;
import ru.aston.user.admin.repository.AdminRepositoryImpl;
import ru.aston.user.admin.service.AdminService;
import ru.aston.user.admin.service.AdminServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class AdminServlet extends AbstractServlet {
    private final AdminRepository adminRepository = new AdminRepositoryImpl();
    private final AdminService adminService = new AdminServiceImpl(adminRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        if (headerId == null) {
            Collection<AdminDto> admins = adminService.getAllAdmin();
            sendJsonResponse(resp, HttpServletResponse.SC_OK, admins);
        } else {
            long adminId = parseId(headerId, resp);
            if (adminId < 1) {
                return;
            }

            AdminDto admin = adminService.getAdminById(adminId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, admin);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AdminDto admin = readJsonRequest(req, AdminDto.class);
        AdminDto addedAdmin = adminService.createAdmin(admin);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, addedAdmin);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);
        AdminDto admin = readJsonRequest(req, AdminDto.class);

        long adminId = parseId(headerId, resp);
        if (adminId < 1) {
            return;
        }

        AdminDto updatedAdmin = adminService.updateAdmin(adminId, admin);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedAdmin);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long adminId = Long.parseLong(headerId);
        if (adminId < 1) {
            return;
        }

        adminService.deleteAdmin(adminId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, "Admin deleted successfully");
    }
}
