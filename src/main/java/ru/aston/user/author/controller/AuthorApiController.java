package ru.aston.user.author.controller;

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
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.service.AuthorService;
import static ru.aston.constant.Constant.HEADER_USER;
import static ru.aston.constant.Constant.PAGE_FROM_DEFAULT;
import static ru.aston.constant.Constant.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorApiController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseAuthorDto getAuthorById(@RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return authorService.getAuthorById(userId);
    }

    @GetMapping("/all")
    public Collection<ResponseAuthorDto> getAllAuthors(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                                       @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        return authorService.getAllAuthors(from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseAuthorDto createAuthor(@Validated(Create.class) @RequestBody @NotNull RequestAuthorDto user) {
        return authorService.createAuthor(user);
    }

    @PatchMapping
    public ResponseAuthorDto updateAuthor(@Validated(Update.class) @RequestBody @NotNull RequestAuthorDto user,
                                          @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return authorService.updateAuthor(userId, user);
    }

    @DeleteMapping
    public void deleteAuthor(@RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        authorService.deleteAuthor(userId);
    }
}
