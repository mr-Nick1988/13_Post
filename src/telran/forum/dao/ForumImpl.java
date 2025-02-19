package telran.forum.dao;

import telran.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

public class ForumImpl implements Forum {

    private final Comparator<Post> comparator = (p1, p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res != 0 ? res : Integer.compare(p1.getPostId(), p2.getPostId());
    };
    private Post[] posts;
    private int size;

    public ForumImpl() {
        this.posts = new Post[20];
        this.size = 0;
    }

    @Override
    public boolean addPost(Post post) {
        if (post == null || size == posts.length || getPostById(post.getPostId()) != null) {
            return false;
        }
        if (size == posts.length) {
            posts = Arrays.copyOf(posts, posts.length + 10);
        }
        int index = Arrays.binarySearch(posts, 0, size, post, comparator);
        index = index >= 0 ? index : -index - 1;
        System.arraycopy(posts, index, posts, index + 1, size - index);
        posts[index] = post;
        size++;
        return true;
    }

    @Override
    public boolean removePost(int postId) {
        int index = findPostIndex(postId);
        if (index == -1) {
            return false;
        }
        System.arraycopy(posts, index + 1, posts, index, size - index - 1);
        posts[--size] = null;
        Arrays.sort(posts, 0, size, comparator);
        return true;

    }

    @Override
    public boolean updatePost(int postId, String content) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        post.setContent(content);
        return true;
    }

    @Override
    public Post getPostById(int postId) {
        int index = findPostIndex(postId);
        return index == -1 ? null : posts[index];
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        return getFilteredPosts(author, null, null);
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
        return getFilteredPosts(author, dateFrom, dateTo);
    }

    @Override
    public int size() {
        return size;
    }

    private int findPostIndex(int postId) {
        for (int i = 0; i < size; i++) {
            if (posts[i] != null && posts[i].getPostId() == postId) {
                return i;
            }
        }
        return -1;
    }

    private Post[] getFilteredPosts(String author, LocalDate dateFrom, LocalDate dateTo) {
        Post[] res = new Post[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (posts[i] != null) {
                boolean matchesAuthor = posts[i].getAuthor().equals(author);
                LocalDateTime postDateTime = posts[i].getDate();
                boolean matchesDate = false;

                if (postDateTime != null) {
                    LocalDate postDate = postDateTime.toLocalDate();
                    matchesDate = (dateFrom == null || !postDate.isBefore(dateFrom)) &&
                            (dateTo == null || !postDate.isAfter(dateTo));
                } else if (dateFrom == null && dateTo == null) {
                    matchesDate = true;
                }
                if (matchesAuthor && matchesDate) {
                    res[count++] = posts[i];
                }
            }
        }
        return Arrays.copyOf(res, count);
    }
}