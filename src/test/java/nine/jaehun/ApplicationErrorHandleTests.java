package nine.jaehun;

import com.fasterxml.jackson.databind.ObjectMapper;
import nine.jaehun.article.entity.TagArticles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationErrorHandleTests {

    @Autowired
    private TestRestTemplate template;
    private ObjectMapper mapper = new ObjectMapper();

    @Test()
    public void articlesBadRequest() {
        final ResponseEntity<String> response = template.getForEntity("/articles", String.class);
        assertThat(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void articlesNotFound() throws Exception {
        final String fullPath = String.format("/articles/%s", "100");
        final ResponseEntity<String> response = template.getForEntity(fullPath, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void tagsExecption() throws Exception {
        final String tagName = "health";
        final String fullPath = String.format("/tags/%s/%s", tagName, "2016-09-22");
        final ResponseEntity<String> response = template.getForEntity(fullPath, String.class);
        final TagArticles tagArticles = mapper.readValue(response.getBody(), TagArticles.class);
        assertThat(tagArticles.getTag()).isEqualTo(tagName);
        assertThat(tagArticles.getCount()).isEqualTo(0);
        assertThat(tagArticles.getArticles().isEmpty()).isEqualTo(true);
        assertThat(tagArticles.getRelated_tags().isEmpty()).isEqualTo(true);
    }
}
