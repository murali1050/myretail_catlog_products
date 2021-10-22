package com.myretail.catalog.repositories;

import com.myretail.catalog.domain.ProductDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

@Repository
public class ProductRepoImpl implements IProductRepo {

  private static final Logger log = LoggerFactory.getLogger(ProductRepoImpl.class);

  @Autowired MongoTemplate mongoTemplate;

  /** Repository class - Get the product details from Mongo (back-end). */
  @Override
  public ProductDetails getProducts(Long pid) {
    StopWatch watch = new StopWatch();
    watch.start("Get product details Starts");
    ProductDetails product =
        mongoTemplate.findOne(Query.query(Criteria.where("PROD_ID").is(pid)), ProductDetails.class);
    watch.stop();
    log.info(
        "Total time taken for retrieving product details of PID:{} is {}s",
        pid,
        watch.getTotalTimeSeconds());
    return product;
  }

  /** Repository class - update / add the product details to Mongo (back-end). */
  @Override
  public void updateProducts(ProductDetails prodDomain) {
    Query query = new Query(new Criteria("PROD_ID").is(prodDomain.getProductId()));
    Update update =
        new Update()
            .set("PROD_ID", prodDomain.getProductId())
            .set("PROD_NAME", prodDomain.getProductName())
            .set("CURRENCY", prodDomain.getCurrency())
            .set("CURRENCY_UNIT", prodDomain.getCurrUnit())
            .setOnInsert("CREATED_DATE", java.time.Clock.systemUTC().instant())
            .set("UPDATED_DATE", java.time.Clock.systemUTC().instant());
    StopWatch watch = new StopWatch();
    watch.start("upsert product details Starts");
    mongoTemplate.upsert(query, update, ProductDetails.class);
    log.info(
        "Total time taken to upd/ins product details of PID:{} is {}s",
        prodDomain.getProductId(),
        watch.getTotalTimeSeconds());
  }
}
