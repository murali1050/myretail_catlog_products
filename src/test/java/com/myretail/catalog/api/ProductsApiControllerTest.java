package com.myretail.catalog.api;

import static org.junit.Assert.assertEquals;

import com.myretail.catalog.domain.ProductDetails;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.utils.AppConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductsApiControllerTest extends AbstractControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void validateProducts() {
    HttpHeaders headers = getHeaders(true);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Product> response =
        restTemplate.exchange("/product/1635461544767", HttpMethod.GET, entity, Product.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void validateProductsNotFound() {
    HttpHeaders headers = getHeaders(true);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Product> response =
        restTemplate.exchange("/product/12345", HttpMethod.GET, entity, Product.class);
    assertEquals(
        AppConstants.HTTP_NOT_FOUND, response.getBody().getErrorResponse().getError().getCode());
  }

  @Test
  public void validateProductsBadRequest() {
    HttpHeaders headers = getHeaders(true);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Product> response =
        restTemplate.exchange("/product/abcdef", HttpMethod.GET, entity, Product.class);

    assertEquals(
        AppConstants.HTTP_BAD_REQUEST, response.getBody().getErrorResponse().getError().getCode());
  }

  @Test
  public void validateUpdateProduct() {
    ProductDetails product = new ProductDetails();
    product.setProductId(68732863234L);
    product.setCurrency(650.00);
    product.setProductName("Cologne Tufted Track Arm Sofa Emerald Green - Project");
    product.setCurrUnit("USD");
    HttpHeaders headers = getHeaders(true);
    HttpEntity<ProductDetails> entity = new HttpEntity<>(product, headers);
    ResponseEntity<Product> response =
        restTemplate.exchange("/product", HttpMethod.PUT, entity, Product.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void validateAddProduct() {
    ProductDetails product = new ProductDetails();
    product.setCurrency(100.00);
    product.setProductName("Delavan Metal Tripod Floor Lamp - Project 62");
    product.setCurrUnit("USD");
    HttpHeaders headers = getHeaders(true);
    HttpEntity<ProductDetails> entity = new HttpEntity<>(product, headers);
    ResponseEntity<Product> response =
        restTemplate.exchange("/product", HttpMethod.PUT, entity, Product.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
