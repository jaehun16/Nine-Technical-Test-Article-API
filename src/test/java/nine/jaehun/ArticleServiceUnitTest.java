package nine.jaehun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nine.jaehun.article.entity.TagArticles;
import nine.jaehun.article.entity.Article;
import nine.jaehun.article.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleServiceUnitTest {
    @Autowired
    private ArticleService articleService;

    @Test
    public void HibernateCreatesInitialRecords() throws ParseException {
        final String title = "latest science shows that potato chips are better for you than sugar";
        final String date = "2020-09-22";
        final String body = "some text, potentially containing simple markup about how potato chips are great";
        final String[] tags = {"health", "fitness", "science"};
        final Article newArticle = articleService.save(new Article(title, date, body, tags));
        assertThat(newArticle.getTitle()).isEqualTo(title);

        final Article article = articleService.findById(newArticle.getId()).orElse(new Article());
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getDate()).isEqualTo(date);
        assertThat(article.getBody()).isEqualTo(body);

        final Set<String> tagSet = new HashSet<>();
        article.getTags().forEach(tag -> { tagSet.add(tag.getTag()); });
        assertThat(tagSet.size()).isEqualTo(tags.length);
        for (String tag : tags) {
            assertThat(tagSet.contains(tag)).isEqualTo(true);
        }

        articleService.save(new Article(title, date, body, new String[]{"policy", "health"}));
        articleService.save(new Article(title, date, body, new String[]{"art", "science"}));

        final TagArticles tagArticles = articleService.findByTagnameAndDate(tags[2], date);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tags[2]);
        assertThat(tagArticles.getCount()).isEqualTo(7);
        assertThat(tagArticles.getArticles().size()).isEqualTo(2);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(3);

        try {
            final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            System.out.printf(objectWriter.writeValueAsString(tagArticles));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}