package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (primitiveRouting(req, resp, path, method)) return;
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();

      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private boolean primitiveRouting(HttpServletRequest req, HttpServletResponse resp, String path, String method) throws IOException {
    if (method.equals("GET") && path.equals("/api/posts")) {
      controller.all(resp);
      return true;
    }
    if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
      controller.getById(path, resp);
      return true;
    }
    if (method.equals("POST") && path.equals("/api/posts")) {
      controller.save(req.getReader(), resp);
      return true;
    }
    if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
      controller.removeById(path, resp);
      return true;
    }
    return false;
  }
}

