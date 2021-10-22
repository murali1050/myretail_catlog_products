package com.myretail.catalog.repositories;

import com.myretail.catalog.domain.ProductDetails;

public interface IProductRepo {

  ProductDetails getProducts(Long pid);

  void updateProducts(ProductDetails prodDomain);
}
