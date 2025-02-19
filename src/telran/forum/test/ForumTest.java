package telran.forum.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.forum.dao.Forum;
import telran.forum.dao.ForumImpl;
import telran.forum.model.Post;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class ForumTest {
    private Forum forum;
    private Post[] posts;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        forum = new ForumImpl();
        posts = new Post[6];
        posts[0] = new Post(1, "First Post", "Alice", "This is the first post.");
        posts[1] = new Post(2, "Second Post", "Bob", "Some interesting content.");
        posts[2] = new Post(3, "Third Post", "Charlie", "Another test post.");
        posts[3] = new Post(4, "Fourth Post", "Dave", "Yet another post example.");
        posts[4] = new Post(5, "Fifth Post", "Eve", "Testing post data.");
        posts[5] = new Post(6, "Sixth Post", "Frank", "Last test post in the setup.");
        for (int i = 0; i < posts.length; i++) {
            posts[i].setDate(now.minusDays(i + 1));
            posts[i].addLike();
            forum.addPost(posts[i]);
        }
    }

    @Test
    void testAddPost() {
        assertFalse(forum.addPost(null));
        assertFalse(forum.addPost(posts[0]));
        Post newPost = new Post(7, "Seventh Post", "Grace", "Content of the seventh post");
        assertTrue(forum.addPost(newPost));
        assertEquals(7, forum.size());

    }

    @Test
    void testRemovePost() {
        assertTrue(forum.removePost(3));
        assertEquals(5, forum.size());
        assertFalse(forum.removePost(10));
        assertFalse(forum.removePost(-1));
    }

    @Test
    void TestUpdatePost() {
        assertTrue(forum.updatePost(2, "Updated"));
        Post updetedPost = forum.getPostById(2);
        assertNotNull(updetedPost);
        assertEquals("Updated", updetedPost.getContent());
    }

    @Test
    void testGetPostById() {
        Post post = forum.getPostById(3);
        assertNotNull(post);
        assertEquals(3, post.getPostId());
        assertEquals("Third Post", post.getTitle());
        assertNull(forum.getPostById(10));
    }

    @Test
    void testGetPostsByAuthor() {
        Post post1 = new Post(7, "Post 7", "Alice", "Content 7");
        Post post2 = new Post(8, "Post 8", "Alice", "Content 8");
        forum.addPost(post1);
        forum.addPost(post2);
        Post[] alicePosts = forum.getPostsByAuthor("Alice");
        assertNotNull(alicePosts);
        assertEquals(3, alicePosts.length);
        assertEquals(1, alicePosts[0].getPostId());
        assertEquals(7, alicePosts[1].getPostId());
        assertEquals(8, alicePosts[2].getPostId());
        Post[] nonExistentPosts = forum.getPostsByAuthor("NonExistent");
        assertNotNull(nonExistentPosts);
        assertEquals(0, nonExistentPosts.length);
        assertEquals(0, nonExistentPosts.length);

    }

    @Test
    void testGetPostsByAuthorAndDate() {
        LocalDate dateFrom = LocalDate.now().minusDays(3);
        LocalDate dateTo = LocalDate.now().minusDays(1);
        assertEquals(1, forum.getPostsByAuthor("Alice", dateFrom, dateTo).length);
        assertEquals("First Post", forum.getPostsByAuthor("Alice", dateFrom, dateTo)[0].getTitle());
        assertEquals(1, forum.getPostsByAuthor("Bob", dateFrom, dateTo).length);
        assertEquals("Second Post", forum.getPostsByAuthor("Bob", dateFrom, dateTo)[0].getTitle());
        assertEquals(0, forum.getPostsByAuthor("NonExistent", dateFrom, dateTo).length);
        assertEquals(0, forum.getPostsByAuthor("Alice", dateTo.plusDays(1), dateTo.plusDays(2)).length);


    }


}


