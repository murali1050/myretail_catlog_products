package com.myretail.catalog.api;

import com.myretail.catalog.model.Product;
import com.myretail.catalog.model.ProductRequest;
import com.myretail.catalog.service.IProductService;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProductsApiController implements ProductsApi {

  @Autowired private IProductService service;

  /** Get the product details of the given product ID. */
  public ResponseEntity<Product> retreiveProduct(
      @ApiParam(value = "", required = true) @PathVariable("productId") String productId) {
    Product product = service.getProducts(productId);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  /** Update or Insert product details of the given product. */
  public ResponseEntity<Product> updateProduct(
      @ApiParam(value = "Request Body", required = true) @Valid @RequestBody
          ProductRequest productReq) {
    Product product = service.updProducts(productReq);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }
}
