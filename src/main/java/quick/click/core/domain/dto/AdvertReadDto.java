package quick.click.core.domain.dto;

import quick.click.core.enums.AdvertStatus;
import quick.click.core.enums.Category;
import quick.click.core.enums.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdvertReadDto {

    private Long id;

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

    private boolean favorite;

    protected List<CommentReadDto> comments = new ArrayList<>();

    protected  List<byte[]> images = new ArrayList<>();

    protected UserReadDto user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<CommentReadDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentReadDto> comments) {
        this.comments = comments;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public UserReadDto getUser() {
        return user;
    }

    public void setUser(UserReadDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertReadDto that = (AdvertReadDto) o;
        return firstPriceDisplayed == that.firstPriceDisplayed && favorite == that.favorite && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && category == that.category && status == that.status && Objects.equals(phone, that.phone) && Objects.equals(price, that.price) && Objects.equals(firstPrice, that.firstPrice) && currency == that.currency && Objects.equals(address, that.address) && Objects.equals(comments, that.comments) && Objects.equals(images, that.images) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, status, phone, price, firstPrice, firstPriceDisplayed, currency, address, favorite, comments, images, user);
    }

    @Override
    public String toString() {
        return "AdvertReadDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", price=" + price +
                ", firstPrice=" + firstPrice +
                ", firstPriceDisplayed=" + firstPriceDisplayed +
                ", currency=" + currency +
                ", address='" + address + '\'' +
                ", favorite=" + favorite +
                ", comments=" + comments +
                ", images=" + images +
                ", user=" + user +
                '}';
    }
}
