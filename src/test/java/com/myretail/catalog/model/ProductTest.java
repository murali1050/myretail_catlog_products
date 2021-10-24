package com.myretail.catalog.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProductTest {

  @Test
  public void productAttriTest() {
    Error err = new Error();
    err.setTitle("title");
    err.setProductId("productId");
    err.productId("productId");
    err.title("title");
    err.message("message");
    err.code(0);
    assertNotNull(err.toString());
    assertNotNull(err.hashCode());
    assertTrue(err.equals(err));

    ErrorResponse res = new ErrorResponse();
    assertNotNull(res.toString());
    assertNotNull(res.hashCode());
    assertTrue(res.equals(res));
    res.error(err);

    CurrentPrice price = new CurrentPrice();
    price.currencyCode("code");
    price.value(0.0);

    assertNotNull(price.toString());
    assertNotNull(price.hashCode());
    assertTrue(price.equals(price));

    Product prod = new Product();
    prod.errorResponse(res);
    prod.currentPrice(price);

    prod.status("status");
    prod.pid(0L);
    prod.name("name");

    assertNotNull(prod.toString());
    assertNotNull(prod.hashCode());
    assertTrue(prod.equals(prod));
    
    Error err1 = new Error();
    assertNotNull(err1.toString());
    assertFalse(err.equals(err1));
    assertFalse(err1.equals(null));
    assertFalse(err1.equals(new Object()));
    err1.setTitle("title");
    assertFalse(err.equals(err1));
    err1.setProductId("productId");
    assertFalse(err.equals(err1));
    err1.productId("productId");
    assertFalse(err.equals(err1));
    err1.title("title");
    assertFalse(err.equals(err1));
    err1.message("message");
    assertFalse(err.equals(err1));
    err1.code(0);
    assertTrue(err.equals(err1));
    
    ErrorResponse res1 = new ErrorResponse();
    assertNotNull(res1.toString());
    assertFalse(res.equals(res1));
    assertFalse(res1.equals(null));
    assertFalse(res1.equals(new Object()));
    res1.error(err1);
    assertTrue(res.equals(res1));
    
    CurrentPrice price1 = new CurrentPrice();
    assertNotNull(price1.toString());
    assertFalse(price.equals(price1));
    assertFalse(price.equals(null));
    assertFalse(price.equals(new Object()));
    price1.currencyCode("code");
    assertFalse(price.equals(price1));
    price1.value(null);
    assertFalse(price.equals(price1));
    price1.value(0.0);
    assertTrue(price.equals(price1));
    
    Product prod1 = new Product();
    assertNotNull(prod1.toString());
    assertFalse(prod.equals(prod1));
    assertFalse(prod.equals(null));
    assertFalse(prod.equals(new Object()));
    
    prod1.setStatus("status");
    assertFalse(prod.equals(prod1));
    prod1.setPid(0L);
    assertFalse(prod.equals(prod1));
    prod1.setName("name");
    assertFalse(prod.equals(prod1));
    prod1.setCurrentPrice(price);
    assertFalse(prod.equals(prod1));
    prod1.setErrorResponse(res);
    assertTrue(prod.equals(prod1));
  }
}
