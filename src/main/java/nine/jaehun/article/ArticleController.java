package nine.jaehun.article;

import nine.jaehun.article.entity.Article;
import nine.jaehun.article.entity.ArticleResponse;
import nine.jaehun.article.entity.TagArticles;
import nine.jaehun.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public static final SimpleDateFormat inFormat = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> createArticles(@RequestBody Article article) {
        final Article _article = articleService.save(article);
        if (_article == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ArticleResponse(_article), HttpStatus.CREATED);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticles(@PathVariable long id) {
        final Article article = articleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not found Articles with id = " + id));
        return new ResponseEntity<>(new ArticleResponse(article), HttpStatus.OK);
    }

    @GetMapping("/tags/{tagName}/{date}")
    public ResponseEntity<TagArticles> findTags(@PathVariable String tagName, @PathVariable String date) {
        try {
            final Date inDate = inFormat.parse(date);
            final String dbDate = format.format(inDate);
            final TagArticles tagArticles = articleService.findByTagnameAndDate(tagName, dbDate);
            return new ResponseEntity<>(tagArticles, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date = " + date);
        }
    }
}