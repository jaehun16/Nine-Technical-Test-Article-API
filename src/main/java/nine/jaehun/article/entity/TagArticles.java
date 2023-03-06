package nine.jaehun.article.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TagArticles {
    private String tag;

    // The count field shows the number of tags for the tag for that day.
    private int count = 0;

    // The articles field contains a list of ids for the last 10 articles entered for that day.
    private List<String> articles = new LinkedList<>();

    // The related_tags field contains a list of tags that are on the articles
    // that the current tag is on for the same day. It should not contain duplicates.
    private Set<String> related_tags = new HashSet<>();

    public TagArticles() {}
    public TagArticles(final String tagName) {
        this.tag = tagName;
    }

    public String getTag() {
        return tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getArticles() {
        return articles;
    }

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

    public Set<String> getRelated_tags() {
        return related_tags;
    }

    public void setRelated_tags(Set<String> related_tags) {
        this.related_tags = related_tags;
    }

    public void addRelatedTag(String related_tag) {
        this.related_tags.add(related_tag);
    }

    public void removeRelatedTag(String related_tag) {
        this.related_tags.remove(related_tag);
    }
}
