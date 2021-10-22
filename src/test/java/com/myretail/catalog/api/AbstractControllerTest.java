package com.myretail.catalog.api;

import static org.mockito.Mockito.validateMockitoUsage;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@TestConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractControllerTest {

  @After
  public void validate() {
    validateMockitoUsage();
  }

  protected HttpHeaders getHeaders(boolean hasPayload) {
    HttpHeaders headers =
        new HttpHeaders() {
          private static final long serialVersionUID = 1L;

          {
            set("Accept", "application/json");
          }
        };

    if (hasPayload) {
      headers.set("Content-Type", "application/json");
    }

    return headers;
  }
}
