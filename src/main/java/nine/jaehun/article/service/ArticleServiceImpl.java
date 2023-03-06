package nine.jaehun.article.service;

import nine.jaehun.article.entity.Article;
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
        final List<Article> articles = articleRepository.findByDateAndTagsTagOrderById(date, tagName);
        final TagArticles tagArticles = new TagArticles(tagName);
        tagArticles.setCount(articles.size());
        articles.forEach(article -> {
            article.getTags().forEach(tag -> {
                tagArticles.addRelatedTag(tag.getTag());
            });
        });
        tagArticles.removeRelatedTag(tagName);

        final int startIndex = (articles.size() > 10) ? articles.size() - 10 : 0;
        for(int i = startIndex; i < articles.size(); i++ ) {
            tagArticles.getArticles().add(Long.toString(articles.get(i).getId()));
        }
        return tagArticles;
    }
}