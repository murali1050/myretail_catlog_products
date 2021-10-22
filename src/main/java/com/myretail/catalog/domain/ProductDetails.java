package com.myretail.catalog.domain;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "RET_PRODUCT_DETAILS")
public class ProductDetails {

  @Id private String id;

  @Field("PROD_ID")
  private Long productId;

  @Field("PROD_NAME")
  private String productName;

  @Field("PROD_DESC")
  private String productDesc;

  @Field("CATEGORY")
  private String category;

  @Field("CURRENCY_UNIT")
  private String currUnit;

  @Field("CURRENCY")
  private Double currency;

  @Field("CREATED_DATE")
  private Date createDate;

  @Field("UPDATED_DATE")
  private Date updateDate;

  public ProductDetails() {}
}
