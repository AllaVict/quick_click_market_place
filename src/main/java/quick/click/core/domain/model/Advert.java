package quick.click.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import quick.click.core.domain.BaseEntity;
import quick.click.core.enums.*;

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

    //@Size(max = 50)
    @Column(name = "price")
    private Float price;

   // @Size(max = 50)
    @Column(name = "first_price")
    private Float first_Price;

    @Column(name = "first_price_displayed")
    private boolean firstPriceDisplayed;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Size(max = 100)
    @Column(name = "address")
    private String address;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    protected FileReference image;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    protected User user;

    public Advert() {

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getFirst_Price() {
        return first_Price;
    }

    public void setFirst_Price(Float first_Price) {
        this.first_Price = first_Price;
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

    public FileReference getImage() {
        return image;
    }

    public void setImage(FileReference image) {
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
        return firstPriceDisplayed == advert.firstPriceDisplayed && Objects.equals(id, advert.id) && Objects.equals(title, advert.title) && Objects.equals(description, advert.description) && category == advert.category && status == advert.status && Objects.equals(phone, advert.phone) && Objects.equals(price, advert.price) && Objects.equals(first_Price, advert.first_Price) && currency == advert.currency && Objects.equals(address, advert.address) && Objects.equals(image, advert.image) && Objects.equals(user, advert.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, status, phone, price, first_Price, firstPriceDisplayed, currency, address, image, user);
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
                ", first_Price=" + first_Price +
                ", firstPriceDisplayed=" + firstPriceDisplayed +
                ", currency=" + currency +
                ", address='" + address + '\'' +
                ", image=" + image +
                ", user=" + user +
                '}';
    }
}