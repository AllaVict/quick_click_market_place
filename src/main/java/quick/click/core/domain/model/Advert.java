package quick.click.core.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import quick.click.core.domain.BaseEntity;
import quick.click.core.enums.AdvertStatus;
import quick.click.core.enums.Category;
import quick.click.core.enums.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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

    @Size(max = 300)
    @Column(name = "address")
    private String address;

    @Column(name = "favorite")
    private boolean favorite;

    @Column(name = "viewing_quantity")
    private int viewingQuantity;

    @Column(name = "promoted")
    private boolean promoted;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "viewer_id")
    private User viewer;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "advert", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ImageData> images;

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

    public int getViewingQuantity() {return viewingQuantity;}

    public void setViewingQuantity(int viewingQuantity) {this.viewingQuantity = viewingQuantity;}

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getViewer() {
        return viewer;
    }

    public void setViewer(User viewer) {
        this.viewer = viewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advert advert = (Advert) o;
        return firstPriceDisplayed == advert.firstPriceDisplayed && favorite == advert.favorite && Objects.equals(id, advert.id) && Objects.equals(title, advert.title) && Objects.equals(description, advert.description) && category == advert.category && status == advert.status && Objects.equals(phone, advert.phone) && Objects.equals(price, advert.price) && Objects.equals(firstPrice, advert.firstPrice) && currency == advert.currency && Objects.equals(address, advert.address) && Objects.equals(user, advert.user) && Objects.equals(comments, advert.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, status, phone, price, firstPrice, firstPriceDisplayed, currency, address, favorite, user, comments);
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
                ", user=" + user +
                ", comments=" + comments +
                '}';
    }
}