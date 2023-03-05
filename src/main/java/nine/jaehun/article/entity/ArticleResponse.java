package nine.jaehun.article.entity;

import java.util.HashSet;
import java.util.Set;

public class ArticleResponse {

    private Long id;
    private String title;
    private String date;
    private String body;
    private Set<String> tags = new HashSet<>();

    public ArticleResponse(final Article article) {
        id = article.getId();
        title = article.getTitle();
        date = article.getDate();
        body = article.getBody();
        article.getTags().forEach(tag -> {
            tags.add(tag.getTag());
        });
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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
