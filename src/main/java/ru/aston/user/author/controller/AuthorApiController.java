package ru.aston.user.author.controller;

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
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.service.AuthorService;

import static ru.aston.constant.Constant.*;
import static ru.aston.constant.Constant.REASON_INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
@Api(value = "/api/authors", tags = "Authors Controller")
public class AuthorApiController {
    private final AuthorService authorService;

    @GetMapping
    @ApiOperation(value = "Get an author by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseAuthorDto getAuthorById(@RequestHeader(HEADER_USER) @Positive @NotNull
                                               @ApiParam(name = "ID of the author to retrieve", required = true)
                                               Long userId) {
        return authorService.getAuthorById(userId);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public Collection<ResponseAuthorDto> getAllAuthors(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                                       @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        return authorService.getAllAuthors(from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = REASON_CREATED),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseAuthorDto createAuthor(@Validated(Create.class) @RequestBody @NotNull
                                              @ApiParam(name = "Author object to be created", required = true)
                                              RequestAuthorDto user) {
        return authorService.createAuthor(user);
    }

    @PatchMapping
    @ApiOperation(value = "Update an existing author")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseAuthorDto updateAuthor(@Validated(Update.class) @RequestBody @NotNull
                                              @ApiParam(name = "Updated author object", required = true)
                                              RequestAuthorDto user,
                                          @RequestHeader(HEADER_USER) @Positive @NotNull
                                          @ApiParam(name = "ID of the author to update", required = true)
                                          Long userId) {
        return authorService.updateAuthor(userId, user);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete an existing author")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public void deleteAuthor(@RequestHeader(HEADER_USER) @Positive @NotNull
                                 @ApiParam(name = "ID of the author to delete", required = true)
                                 Long userId) {
        authorService.deleteAuthor(userId);
    }
}
