package com.myretail.catalog.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

import com.myretail.catalog.model.Product;

public class ProductDetailsTest {

  @Test
  public void testProductSetters() {
    ProductDetails product = new ProductDetails();

    assertNotNull(product.hashCode());
    assertNotNull(product.toString());
    assertTrue(product.equals(product));
    assertTrue(product.canEqual(product));
    product.setId("id");
    product.setProductDesc("desc");
    product.setCategory("category");
    product.setCreateDate(new Date());
    product.setUpdateDate(new Date());
    assertTrue(product.equals(product));
    
    ProductDetails prod1 = new ProductDetails();
    assertNotNull(prod1.toString());
    assertFalse(product.equals(prod1));
    assertFalse(product.equals(null));
    assertFalse(product.equals(new Object()));
    prod1.setId("id");
    assertFalse(product.equals(prod1));
    prod1.setProductDesc("desc");
    assertFalse(product.equals(prod1));
    prod1.setCategory("category");
    assertFalse(product.equals(prod1));
    prod1.setCreateDate(new Date());
    assertFalse(product.equals(prod1));
    prod1.setUpdateDate(new Date());
    assertTrue(product.equals(prod1));
  }
}
