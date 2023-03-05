package nine.jaehun.article.entity;

import jakarta.persistence.*;
import nine.jaehun.article.ArticleController;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_generator")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private String date;
    @Column(name = "body")
    private String body;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn(name = "article_id")
    private Set<Tag> tags = new HashSet<>();

    public Article() {}

    public Article(final String title, final String date, final String body, final String[] tags) {
        this.title = title;
        this.date = date;
        this.body = body;
        if (tags != null) {
            for(String tag : tags) {
                this.tags.add(new Tag(tag));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = (tags == null)? new HashSet<>() : tags;
    }

    public boolean checkValid() {
        if (isEmpty(getTitle())) { return false; }

        if (isEmpty(getBody())) { return false; }

        if (getTags() == null || getTags().isEmpty()) {
            return false;
        }

        if (isEmpty(getDate())) {
            setDate(ArticleController.format.format(new Date()));
        }
        return true;
    }

    private boolean isEmpty(final String value) {
        return (value == null || value.trim().isEmpty());
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append("ID: ").append(getId()).append("\n")
                .append("Title: ").append(getTitle()).append("\n")
                .append("Date: ").append(getDate()).append("\n")
                .append("Body: ").append(getBody()).append("\n")
                .append("Tags: ").append(getTags().toString()).append("\n")
                .toString();
    }
}
