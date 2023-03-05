package nine.jaehun.article.service;

import nine.jaehun.article.entity.Article;
import nine.jaehun.article.entity.Tag;
import nine.jaehun.article.entity.TagArticles;
import nine.jaehun.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article save(final Article article) {
        if(article.checkValid()) {
            return articleRepository.save(article);
        } else {
            return null;
        }
    }

    @Override
    public Optional<Article> findById(final long id) {
        return articleRepository.findById(id);
    }

    @Override
    public TagArticles findByTagnameAndDate(final String tagName, final String date) {
        final TagArticles tagArticles = new TagArticles(tagName);

        // The related_tags field contains a list of tags that are on the articles
        // that the current tag is on for the same day. It should not contain duplicates.
        // List up tags that are on the articles which has the given tag on the given date.
        final List<Article> articles = articleRepository.findByDateAndTagsTagOrderById(date, tagName);
        articles.forEach(article -> {
            article.getTags().forEach(tag -> {
                tagArticles.addRelatedTag(tag.getTag());
            });
        });
        // To exclude the given tag
        tagArticles.removeRelatedTag(tagName);

        // Query all articles on the given date.
        final List<Article> articlesByDate = articleRepository.findByDateOrderById(date);

        // The count field shows the number of tags for the tag for that day.
        // Count the number of the all tags of all the articles.
        articlesByDate.forEach(article -> {
            tagArticles.addCount(article.getTags().size());
        });

        // The articles field contains a list of ids for the last 10 articles
        // which are entered on the given day and have the given tag.
        // List the most recent 10 articles among the viewed articles
        final int startIndex = (articlesByDate.size() > 10) ? articlesByDate.size() - 10 : 0;
        for(int i = startIndex; i < articlesByDate.size(); i++ ) {
            final Article article = articlesByDate.get(i);
            // Select an article that has the given tag
            if (hasTag(article, tagName)) {
                tagArticles.getArticles().add(Long.toString(article.getId()));
            }
        }
        return tagArticles;
    }

    private boolean hasTag(final Article article, final String tagName) {
        for(Tag tag: article.getTags()){
            if (tagName.equals(tag.getTag())) {
                return true;
            }
        }
        return false;
    }

    public List<Article> list() {
        return articleRepository.findAll();
    }
}