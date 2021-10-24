package com.myretail.catalog.api;

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.myretail.catalog.config.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HomeControllerTest {
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private HomeController homeController;

  @Test
  public void testIndex() {
    restTemplate.exchange("/", HttpMethod.GET, getEntity(), String.class);
  }

  private HttpEntity<String> getEntity() {
    return new HttpEntity<>(
        new HttpHeaders() {
          private static final long serialVersionUID = 1L;

          {
            set("Accept", "application/json");
          }
        });
  }

  @Test
  public void testIndexHome() {
    String home = null;
    home = homeController.index();
    assertNotNull(home);
  }
}
