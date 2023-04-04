package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) {
        final var data = service.all();
        new Writer<>(data, response).getWriter();
    }

    public void getById(String path, HttpServletResponse response) {
        final var id = parsePath(path);
        final var data = service.getById(id);
        new Writer<>(data, response).getWriter();
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(String path, HttpServletResponse response) {
        final var id = parsePath(path);
        service.removeById(id);
        // это для проверки что удалилось
        final var data = service.all();
        new Writer<>(data, response).getWriter();
    }

    private long parsePath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }

}


class Writer<T> {
    private final T data;
    private final HttpServletResponse response;

    public Writer(T data, HttpServletResponse response) {
        this.data = data;
        this.response = response;
    }

    public void getWriter() {
        response.setContentType(PostController.APPLICATION_JSON);
        final var gson = new Gson();
        try {
            response.getWriter().print(gson.toJson(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
