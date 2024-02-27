package quick.click.advertservice.core.domain.dto;

import quick.click.advertservice.core.enums.AdvertStatus;
import quick.click.advertservice.core.enums.Category;
import quick.click.advertservice.core.enums.Currency;

import java.time.LocalDateTime;
import java.util.Objects;

public class AdvertCreateDto {

    private String title;

    private String description;


    private Category category;

    private AdvertStatus status;

    private String phone;


    private Double price;

    private Double firstPrice;

    private boolean firstPriceDisplayed;

    private Currency currency;

    private String address;

    private Long userId;

    private  LocalDateTime createdDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public AdvertStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertStatus status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public boolean isFirstPriceDisplayed() {
        return firstPriceDisplayed;
    }

    public void setFirstPriceDisplayed(boolean firstPriceDisplayed) {
        this.firstPriceDisplayed = firstPriceDisplayed;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertCreateDto that = (AdvertCreateDto) o;
        return firstPriceDisplayed == that.firstPriceDisplayed && Objects.equals(title, that.title) && Objects.equals(description, that.description) && category == that.category && status == that.status && Objects.equals(phone, that.phone) && Objects.equals(price, that.price) && Objects.equals(firstPrice, that.firstPrice) && currency == that.currency && Objects.equals(address, that.address) && Objects.equals(userId, that.userId) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, category, status, phone, price, firstPrice, firstPriceDisplayed, currency, address, userId, createdDate);
    }

    @Override
    public String toString() {
        return "AdvertCreateDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", price=" + price +
                ", first_Price=" + firstPrice +
                ", firstPriceDisplayed=" + firstPriceDisplayed +
                ", currency=" + currency +
                ", address='" + address + '\'' +
                ", userId=" + userId +
                ", createdDate=" + createdDate +
                '}';
    }
}
