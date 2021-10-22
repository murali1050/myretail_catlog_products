package com.myretail.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/** Current Price of the Product. */
@ApiModel(description = "Current Price of the Product.")
@Validated
public class CurrentPrice {
  @JsonProperty("value")
  private Double value = null;

  @JsonProperty("currency_code")
  private String currencyCode = null;

  public CurrentPrice value(Double value) {
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

  public CurrentPrice currencyCode(String currencyCode) {
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
    CurrentPrice currentPrice = (CurrentPrice) obj;
    return Objects.equals(this.value, currentPrice.value)
        && Objects.equals(this.currencyCode, currentPrice.currencyCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, currencyCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrentPrice {\n");

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
