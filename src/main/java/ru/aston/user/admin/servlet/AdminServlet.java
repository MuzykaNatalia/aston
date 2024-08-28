package ru.aston.user.admin.servlet;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.user.admin.dto.AdminDto;
import ru.aston.user.admin.repository.AdminRepositoryImpl;
import ru.aston.user.admin.service.AdminService;
import ru.aston.user.admin.service.AdminServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class AdminServlet extends AbstractServlet {
    private final AdminService adminService = new AdminServiceImpl(new AdminRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        if (headerId == null) {
            Collection<AdminDto> admins = adminService.getAllAdmin();
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, admins);
        } else {
            try {
                long adminId = Long.parseLong(headerId);
                AdminDto admin = adminService.getAdminById(adminId);
                resp.setStatus(HttpServletResponse.SC_OK);
                sendJsonResponse(resp, admin);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try {
                    resp.getWriter().write("Invalid admin ID format");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                try {
                    resp.getWriter().write(e.getMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AdminDto admin = readJsonRequest(req, AdminDto.class);
        AdminDto addedAdmin = adminService.createAdmin(admin);
        resp.setStatus(HttpServletResponse.SC_OK);
        sendJsonResponse(resp, addedAdmin);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);
        AdminDto admin = readJsonRequest(req, AdminDto.class);

        try {
            long adminId = Long.parseLong(headerId);
            AdminDto updatedAdmin = adminService.updateAdmin(adminId, admin);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, updatedAdmin);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long adminId = Long.parseLong(headerId);
            adminService.deleteAdmin(adminId);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, "Admin deleted successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }
}
