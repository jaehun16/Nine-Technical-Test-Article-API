package nine.jaehun.article.repository;

import nine.jaehun.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByDateAndTagsTagOrderById(String date, String tag);

    List<Article> findByDateOrderById(String date);
}
