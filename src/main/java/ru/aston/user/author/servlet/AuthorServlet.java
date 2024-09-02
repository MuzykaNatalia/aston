package ru.aston.user.author.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.repository.AuthorRepository;
import ru.aston.user.author.repository.AuthorRepositoryImpl;
import ru.aston.user.author.service.AuthorService;
import ru.aston.user.author.service.AuthorServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class AuthorServlet extends AbstractServlet {
    private final AuthorRepository authorRepository = new AuthorRepositoryImpl();
    private final AuthorService authorService = new AuthorServiceImpl(authorRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        if (headerId == null) {
            Collection<ResponseAuthorDto> authors = authorService.getAllAuthors();
            sendJsonResponse(resp, HttpServletResponse.SC_OK, authors);
        } else {
            long authorId = parseId(headerId, resp);
            if (authorId < 1) {
                return;
            }

            ResponseAuthorDto author = authorService.getAuthorById(authorId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, author);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        RequestAuthorDto author = super.readJsonRequest(req, RequestAuthorDto.class);
        ResponseAuthorDto addedAuthor = authorService.createAuthor(author);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, addedAuthor);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);
        RequestAuthorDto author = readJsonRequest(req, RequestAuthorDto.class);

        long authorId = parseId(headerId, resp);
        if (authorId < 1) {
            return;
        }

        ResponseAuthorDto updatedAuthor = authorService.updateAuthor(authorId, author);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedAuthor);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long authorId = parseId(headerId, resp);
        if (authorId < 1) {
            return;
        }

        authorService.deleteAuthor(authorId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, "User deleted successfully");
    }
}
