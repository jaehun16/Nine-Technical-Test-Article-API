package nine.jaehun.article.service;

import nine.jaehun.article.entity.Article;
import nine.jaehun.article.entity.TagArticles;

import java.util.Optional;

public interface ArticleService {
    Article save(final Article article);
    Optional<Article> findById(final long id);
    TagArticles findByTagnameAndDate(final String tagName, final String date);
}