package com.myretail.catalog.service;

import com.myretail.catalog.domain.ProductDetails;
import com.myretail.catalog.model.CurrentPrice;
import com.myretail.catalog.model.Error;
import com.myretail.catalog.model.ErrorResponse;
import com.myretail.catalog.model.Product;
import com.myretail.catalog.model.ProductRequest;
import com.myretail.catalog.repositories.IProductRepo;
import com.myretail.catalog.utils.AppConstants;
import com.myretail.catalog.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl implements IProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired private IProductRepo repository;

  /** Get the product details of the given product ID. */
  @Override
  public Product getProducts(String productId) {
    Product product = new Product();
    try {
      Long pid = Long.valueOf(productId);
      product.setPid(pid);
      ProductDetails prodDomain = repository.getProducts(pid);
      if (prodDomain != null) {
        product = convertDomainToRes(prodDomain);
      } else {
        log.error("Product details not found for the given PID - {}", productId);
        setErrorResponse(product, AppConstants.HTTP_NOT_FOUND, AppConstants.NO_DATA_FOUND);
      }
    } catch (NumberFormatException exp) {
      log.error(
          "Given input (Product ID) is not an numeric - {} - Exp - {}",
          productId,
          exp.getMessage());
      setErrorResponse(product, AppConstants.HTTP_BAD_REQUEST, AppConstants.INVALID_INPUT);
    } catch (Exception exp) {
      log.error(
          "Generic exception occured while executing Mongo - {} - Exp - {}",
          productId,
          exp.getMessage());
      setErrorResponse(product, AppConstants.HTTP_SERVICE_UNAVAIL, AppConstants.SER_UNEXPECT_FAIL);
    }
    return product;
  }

  /** Update or Insert product details of the given product. */
  @Override
  public Product updProducts(ProductRequest productReq) {
    Product product = new Product();
    try {
      ProductDetails prodDomain = convertReqToDomain(productReq);
      if (StringUtils.isEmpty(prodDomain.getProductId())) {
        prodDomain.setProductId(Utilities.getSeqTrackString());
      }
      repository.updateProducts(prodDomain);
      product.setStatus(AppConstants.STATUS_SUCCESS);
      product.setPid(prodDomain.getProductId());
      log.info(
          "Update / Insert successfully completed for the given PID:{}", prodDomain.getProductId());
    } catch (Exception exp) {
      log.error(
          "Generic exception occured while updating / inserting Mongo - Exp-{}",
          exp.getMessage(),
          exp);
      setErrorResponse(product, AppConstants.HTTP_SERVICE_UNAVAIL, AppConstants.SER_UNEXPECT_FAIL);
    }
    return product;
  }

  /** Convert the Domain object to Response Object. */
  private Product convertDomainToRes(ProductDetails prodDomain) {
    Product product = new Product();
    CurrentPrice price = new CurrentPrice();
    product.setPid(prodDomain.getProductId());
    product.setName(prodDomain.getProductName());
    price.setCurrencyCode(prodDomain.getCurrUnit());
    price.setValue(prodDomain.getCurrency());
    product.setCurrentPrice(price);
    product.setStatus(AppConstants.STATUS_SUCCESS);
    return product;
  }

  /** Convert the Response object to Domain Object. */
  private ProductDetails convertReqToDomain(ProductRequest productReq) {
    ProductDetails product = new ProductDetails();
    product.setProductId(productReq.getPid());
    product.setCurrency(productReq.getValue());
    product.setProductName(productReq.getName());
    product.setCurrUnit(productReq.getCurrencyCode());
    return product;
  }

  /** Populate the error response for the given request. */
  private void setErrorResponse(Product product, Integer errCode, String errMsg) {
    product.setStatus(AppConstants.STATUS_FAILURE);
    Error err = new Error();
    err.setCode(errCode);
    err.setMessage(errMsg);
    ErrorResponse res = new ErrorResponse();
    res.setError(err);
    product.setErrorResponse(res);
  }
}
