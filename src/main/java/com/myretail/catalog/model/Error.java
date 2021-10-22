package com.myretail.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/** Error content. */
@ApiModel(description = "Error content")
@Validated
public class Error {
  @JsonProperty("code")
  private Integer code = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("productId")
  private String productId = null;

  public Error code(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Error Code.
   *
   * @return code
   */
  @ApiModelProperty(value = "Error Code")
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Error message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Error Message.
   *
   * @return message
   */
  @ApiModelProperty(value = "Error Message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Error title.
   *
   * @return title
   */
  @ApiModelProperty(value = "Error title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Error productId(String productId) {
    this.productId = productId;
    return this;
  }

  /**
   * productId.
   *
   * @return productId
   */
  @ApiModelProperty(value = "productId")
  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  @Override
  public boolean equals(java.lang.Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Error error = (Error) obj;
    return Objects.equals(this.code, error.code)
        && Objects.equals(this.message, error.message)
        && Objects.equals(this.title, error.title)
        && Objects.equals(this.productId, error.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, title, productId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");

    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
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
