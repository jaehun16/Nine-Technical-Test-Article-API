package nine.jaehun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nine.jaehun.article.ArticleController;
import nine.jaehun.article.entity.Article;
import nine.jaehun.article.entity.TagArticles;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    private TestRestTemplate template;

    private ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private final String title = "latest science shows that potato chips are better for you than sugar";
    private final String date = "2016-09-22";
    private final String body = "some text, potentially containing simple markup about how potato chips are great";
    private final String[] tags = {"health", "fitness", "science"};

    @Test()
    public void articlesPostRequest() throws Exception {
        final HttpEntity<Article> request = new HttpEntity<>(new Article(title, date, body, tags));
        final ResponseEntity<String> postResponse = template.postForEntity("/articles", request, String.class);
        final long articleId = checkResponseArticle(postResponse, HttpStatus.CREATED);

        final String getPath = String.format("/articles/%s", articleId);
        final ResponseEntity<String> getResponse = template.getForEntity(getPath, String.class);
        checkResponseArticle(getResponse, HttpStatus.OK);

        ResponseEntity<String> response = template
                .postForEntity("/articles"
                        , new HttpEntity<>(new Article(title, date, body, new String[]{"policy", tags[0]}))
                        , String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        response = template
                .postForEntity("/articles"
                        , new HttpEntity<>(new Article(title, date, body, new String[]{"art", tags[1]}))
                        , String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String tagName = tags[0];
        String fullPath = String.format("/tags/%s/%s", tagName, "20160922");
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TagArticles tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(2);
        assertThat(tagArticles.getArticles().size()).isEqualTo(2);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(3);

        tagName = "art";
        fullPath = String.format("/tags/%s/%s", tagName, "20160922");
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(1);
        assertThat(tagArticles.getArticles().size()).isEqualTo(1);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(1);
    }

    @Test()
    public void articlesGetTagAndDateRequest() throws Exception {
        final Date date = new Date();
        final String dateFormat = ArticleController.format.format(date);
        final String dateInFormat = ArticleController.inFormat.format(date);

        ResponseEntity<String> response;
        for(int i = 1; i < 14; i++) {
            response = template
                    .postForEntity("/articles"
                            , new HttpEntity<>(getArticle(i, dateFormat))
                            , String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        String tagName, fullPath;
        TagArticles tagArticles;

        tagName = "art";
        fullPath = String.format("/tags/%s/%s", tagName, dateInFormat);
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(5);
        assertThat(tagArticles.getArticles().size()).isEqualTo(5);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(3);

        tagName = "health";
        fullPath = String.format("/tags/%s/%s", tagName, dateInFormat);
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(3);
        assertThat(tagArticles.getArticles().size()).isEqualTo(3);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(4);

        tagName = "science";
        fullPath = String.format("/tags/%s/%s", tagName, dateInFormat);
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(6);
        assertThat(tagArticles.getArticles().size()).isEqualTo(6);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(4);

        tagName = "fitness";
        fullPath = String.format("/tags/%s/%s", tagName, dateInFormat);
        response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles).isNotNull();
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(6);
        assertThat(tagArticles.getArticles().size()).isEqualTo(6);
        assertThat(tagArticles.getRelated_tags().size()).isEqualTo(4);
    }

    private Article getArticle(final int index, final String date) {
        final String[][] tags = {
                {},
                {"health", "fitness", "science"},
                {"health", "business"},
                {"science", "art"},
                {"health", "policy"},
                {"fitness", "science"},
                {"art"},
                {"policy", "fitness"},
                {"art", "policy", "science"},
                {"policy", "fitness"},
                {"art", "fitness"},
                {"policy", "science"},
                {"art", "science"},
                {"fitness"},
        };
        return new Article("Title_"+index, date, "Body_"+index, tags[index]);
    }

    private long checkResponseArticle(final ResponseEntity<String> response, final HttpStatus httpStatus) throws Exception {
        assertThat(response.getStatusCode()).isEqualTo(httpStatus);

        final Article article = mapper.readValue(response.getBody(), Article.class);
        assertThat(article).isNotNull();
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getDate()).isEqualTo(date);
        assertThat(article.getBody()).isEqualTo(body);

        final Set<String> tagSet = new HashSet<>();
        article.getTags().forEach(tag -> { tagSet.add(tag.getTag()); });
        assertThat(tagSet.size()).isEqualTo(tags.length);
        for (String tag : tags) {
            assertThat(tagSet.contains(tag)).isEqualTo(true);
        }
        return article.getId();
    }
}
