package com.myretail.catalog.service;

import com.myretail.catalog.model.Product;
import com.myretail.catalog.model.ProductRequest;

public interface IProductService {

  Product getProducts(String productId);

  Product updProducts(ProductRequest productReq);
}
