package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContextExtensionsKt;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String HTTP_METH_GET = "GET";
    private static final String HTTP_METH_POST = "POST";
    private static final String HTTP_METH_DEL = "DELETE";
    private static final String HTTP_PATH = "/api/posts";
    private static final String HTTP_PATH_ID = "/api/posts/\\d+";



    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
//        final var repository = context.getBean(PostRepository.class);
//        final var service = context.getBean(PostService.class);
        controller = context.getBean(PostController.class);


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(HTTP_METH_GET)) {
                actionChoiceGet(path, resp);
                return;
            }
            if (method.equals(HTTP_METH_POST) && path.equals(HTTP_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(HTTP_METH_DEL) && path.matches(HTTP_PATH_ID)) {
                controller.removeById(path, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void actionChoiceGet(String path, HttpServletResponse resp) {
        if (path.equals(HTTP_PATH)) {
            controller.all(resp);
        }
        if (path.matches(HTTP_PATH_ID)) {
            controller.getById(path, resp);
        }
    }
}

