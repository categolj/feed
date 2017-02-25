package am.ik.blog;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class FeedApplication {

    @Autowired
    RestTemplate restTemplate;
    @Value("${blog.api.url:http://localhost:8080}")
    String apiUrl;

    @RequestMapping("/")
    @SuppressWarnings("unchecked")
    Channel rss() {
        UriComponents uri = UriComponentsBuilder.fromUriString(apiUrl)
                .replacePath("/api/entries")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("excludeContent", true)
                .build();
        Map<String, Object> entries = restTemplate.getForObject(uri.toUri(), Map.class);

        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("BLOG.IK.AM");
        channel.setDescription("feed for blog.ik.am");
        channel.setLink("https://feed.ik.am");
        channel.setItems(((List<Map<String, Object>>) entries.get("content")).stream()
                .map(entry -> {
                    Item item = new Item();
                    item.setTitle((String) ((Map<String, Object>) entry.get("frontMatter")).get("title"));
                    item.setAuthor((String) ((Map<String, Object>) entry.get("created")).get("name"));
                    item.setLink("https://blog.ik.am/entries/" + entry.get("entryId"));
                    OffsetDateTime created = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse((String) ((Map<String, Object>) entry.get("created")).get("date")).query(OffsetDateTime::from);
                    item.setPubDate(Date.from(created.toInstant()));
                    return item;
                }).collect(Collectors.toList()));
        return channel;
    }

	@LoadBalanced
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.interceptors((request, body, execution) -> {
			request.getHeaders().add(HttpHeaders.USER_AGENT, "Mozilla/5.0");
			return execution.execute(request, body);
		}).build();
	}

    public static void main(String[] args) {
        SpringApplication.run(FeedApplication.class, args);
    }
}
