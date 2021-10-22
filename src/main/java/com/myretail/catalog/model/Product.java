package com.myretail.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/** Product details based on product ID. */
@ApiModel(description = "Product details based on product ID.")
@Validated
public class Product {
  @JsonProperty("status")
  private String status = null;

  @JsonProperty("pid")
  private Long pid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("current_price")
  private CurrentPrice currentPrice = null;

  @JsonProperty("error_response")
  private ErrorResponse errorResponse = null;

  public Product status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Returns the success or failure.
   *
   * @return status
   */
  @ApiModelProperty(value = "Returns the success or failure.")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Product pid(Long pid) {
    this.pid = pid;
    return this;
  }

  /**
   * Product ID of the product (unique).
   *
   * @return pid
   */
  @ApiModelProperty(value = "Product ID of the product (unique).")
  public Long getPid() {
    return pid;
  }

  public void setPid(Long pid) {
    this.pid = pid;
  }

  public Product name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the Product.
   *
   * @return name
   */
  @ApiModelProperty(value = "Name of the Product.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Product currentPrice(CurrentPrice currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice.
   *
   * @return currentPrice
   */
  @ApiModelProperty(value = "")
  @Valid
  public CurrentPrice getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(CurrentPrice currentPrice) {
    this.currentPrice = currentPrice;
  }

  public Product errorResponse(ErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
    return this;
  }

  /**
   * Get errorResponse.
   *
   * @return errorResponse
   */
  @ApiModelProperty(value = "")
  @Valid
  public ErrorResponse getErrorResponse() {
    return errorResponse;
  }

  public void setErrorResponse(ErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

  @Override
  public boolean equals(java.lang.Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Product product = (Product) obj;
    return Objects.equals(this.status, product.status)
        && Objects.equals(this.pid, product.pid)
        && Objects.equals(this.name, product.name)
        && Objects.equals(this.currentPrice, product.currentPrice)
        && Objects.equals(this.errorResponse, product.errorResponse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, pid, name, currentPrice, errorResponse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Product {\n");

    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    pid: ").append(toIndentedString(pid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    errorResponse: ").append(toIndentedString(errorResponse)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
