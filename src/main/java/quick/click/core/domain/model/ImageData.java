package quick.click.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import quick.click.core.domain.BaseEntity;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "images")
public class ImageData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String type;

    @JdbcType(VarbinaryJdbcType.class)
    @Column(name = "image_data")
    @NotEmpty
    private byte[] imageData;
    @JsonIgnore
    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "advert_id")
    private Advert advert;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageData imageData1 = (ImageData) o;
        return Objects.equals(id, imageData1.id) && Objects.equals(name, imageData1.name) && Objects.equals(type, imageData1.type) && Arrays.equals(imageData, imageData1.imageData) && Objects.equals(userId, imageData1.userId) && Objects.equals(advert, imageData1.advert);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, type, userId, advert);
        result = 31 * result + Arrays.hashCode(imageData);
        return result;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                ", userId=" + userId +
                ", advert=" + advert +
                '}';
    }
}
