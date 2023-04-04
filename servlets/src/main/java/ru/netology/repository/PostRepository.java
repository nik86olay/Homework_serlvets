package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


// Stub
@Repository
public class PostRepository {

    AtomicLong counterId = new AtomicLong(0);
    Map<Long, Post> storagePosts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return (!storagePosts.isEmpty()) ? new ArrayList<>(storagePosts.values()) : Collections.emptyList();
    }

    public Optional<Post> getById(long id) {
        return storagePosts.containsKey(id) ? Optional.ofNullable(storagePosts.get(id)) : Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0 || !storagePosts.containsKey(post.getId())) post.setId(counterId.getAndIncrement());
        storagePosts.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        if (storagePosts.containsKey(id)) storagePosts.remove(id);
        else System.out.println("Невозможно удалить, так как указанный id отсутствует");
    }
}
