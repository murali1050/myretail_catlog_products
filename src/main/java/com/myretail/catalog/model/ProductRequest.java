package com.myretail.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/** Product request details for Add or Update. */
@ApiModel(description = "Product request details for Add or Update.")
@Validated
public class ProductRequest {
  @JsonProperty("pid")
  private Long pid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("value")
  private Double value = null;

  @JsonProperty("currency_code")
  private String currencyCode = null;

  public ProductRequest pid(Long pid) {
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

  public ProductRequest name(String name) {
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

  public ProductRequest value(Double value) {
    this.value = value;
    return this;
  }

  /**
   * Value of the product.
   *
   * @return value
   */
  @ApiModelProperty(value = "Value of the product.")
  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public ProductRequest currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

  /**
   * Currency format of the product.
   *
   * @return currencyCode
   */
  @ApiModelProperty(value = "Currency format of the product.")
  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  @Override
  public boolean equals(java.lang.Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ProductRequest productRequest = (ProductRequest) obj;
    return Objects.equals(this.pid, productRequest.pid)
        && Objects.equals(this.name, productRequest.name)
        && Objects.equals(this.value, productRequest.value)
        && Objects.equals(this.currencyCode, productRequest.currencyCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pid, name, value, currencyCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductRequest {\n");

    sb.append("    pid: ").append(toIndentedString(pid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
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
