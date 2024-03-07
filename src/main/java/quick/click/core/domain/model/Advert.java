package quick.click.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import quick.click.core.domain.BaseEntity;
import quick.click.core.enums.AdvertStatus;
import quick.click.core.enums.Category;
import quick.click.core.enums.Currency;

import java.util.Objects;

@Entity
@Table(name = "adverts")
public class Advert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    @Size(max = 100)
    private String title;
    @Column(name = "description")
    @Size(max = 500)
    private String description;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AdvertStatus status;

    @Size(max = 50)
    @Column(name = "phone")
    private String phone;

    @Column(name = "price")
    private Double price;

    @Column(name = "first_price")
    private Double firstPrice;

    @Column(name = "first_price_displayed")
    private boolean firstPriceDisplayed;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Size(max = 100)
    @Column(name = "address")
    private String address;

    @Column(name = "favorite")
    private boolean favorite;

   // @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
 //   @JoinColumn(name = "image_id")
    @Column(name = "image_id")
    protected Long image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Advert() {
       //Empty
    }


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

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advert advert = (Advert) o;
        return firstPriceDisplayed == advert.firstPriceDisplayed && favorite == advert.favorite && Objects.equals(id, advert.id) && Objects.equals(title, advert.title) && Objects.equals(description, advert.description) && category == advert.category && status == advert.status && Objects.equals(phone, advert.phone) && Objects.equals(price, advert.price) && Objects.equals(firstPrice, advert.firstPrice) && currency == advert.currency && Objects.equals(address, advert.address) && Objects.equals(image, advert.image) && Objects.equals(user, advert.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, status, phone, price, firstPrice, firstPriceDisplayed, currency, address, favorite, image, user);
    }

    @Override
    public String toString() {
        return "Advert{" +
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
                ", image=" + image +
                ", user=" + user +
                '}';
    }
}