package quick.click.advertservice.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import quick.click.advertservice.core.domain.BaseEntity;
import quick.click.advertservice.core.enums.FileType;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "file_references")
public class FileReference extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @NotBlank
    @Column(name = "file_name")
    @Size(max = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    @NotNull
    private FileType fileType;

    @Column(name = "file_url")
    private String fileUrl;

    public FileReference() {
    }

    public FileReference(Long id, UUID uuid, String fileName, FileType fileType) {
        this.id = id;
        this.uuid = uuid;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, fileName, fileType);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var other = (FileReference) o;
        return Objects.equals(this.id, other.id) && this.uuid.equals(other.uuid)
                && this.fileName.equals(other.fileName) && this.fileType == other.fileType;
    }

    @Override
    public String toString() {
        return "FileReference{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fileName='" + fileName + '\'' +
                ", fileType=" + fileType +
                '}';
    }
}