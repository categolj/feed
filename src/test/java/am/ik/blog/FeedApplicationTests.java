package am.ik.blog;

import com.rometools.rome.feed.rss.Channel;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeedApplication.class)
@WebIntegrationTest(randomPort = true)
@Ignore
public class FeedApplicationTests {
    @Value("${local.server.port}")
    int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testFeeds() {
        Channel channel = restTemplate.getForObject("http://localhost:" + port, Channel.class);
        assertThat(channel).isNotNull();
        assertThat(channel.getFeedType()).isEqualTo("rss_2.0");
        assertThat(channel.getTitle()).isEqualTo("BLOG.IK.AM");
        assertThat(channel.getDescription()).isEqualTo("feed for blog.ik.am");
        assertThat(channel.getLink()).isEqualTo("https://feed.ik.am");
        assertThat(channel.getItems()).hasSize(2);
        assertThat(channel.getItems().get(0).getTitle()).isEqualTo("Hello Spring Boot");
        assertThat(channel.getItems().get(0).getAuthor()).isEqualTo("making");
        assertThat(channel.getItems().get(0).getLink()).isEqualTo("https://blog.ik.am/entries/2");
        assertThat(channel.getItems().get(0).getPubDate()).isEqualTo("2016-05-04T19:09:28");
        assertThat(channel.getItems().get(1).getTitle()).isEqualTo("Hello Java8");
        assertThat(channel.getItems().get(1).getAuthor()).isEqualTo("making");
        assertThat(channel.getItems().get(1).getLink()).isEqualTo("https://blog.ik.am/entries/1");
        assertThat(channel.getItems().get(1).getPubDate()).isEqualTo("2016-05-04T19:09:28");
    }

}
