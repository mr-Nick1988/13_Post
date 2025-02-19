package telran.forum.model;

import java.time.LocalDateTime;

public class Post {
    private final int postId;
    private final String title;
    private final String author;
    private String content;
    private LocalDateTime date;
    private int like;

    public Post(int postId, String title, String author, String content) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.content = content;

    }

    public int getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getLike() {
        return like;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int addLike(){
        return  ++like;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", like=" + like +
                '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof Post post)) return false;

        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return postId;
    }
}
