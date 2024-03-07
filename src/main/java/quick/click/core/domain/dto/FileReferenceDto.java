package quick.click.core.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import quick.click.core.enums.FileType;

import java.util.Objects;
import java.util.UUID;

public class FileReferenceDto {

    private Long id;

    @NotNull
    private UUID uuid;

    @NotBlank
    private String fileName;

    @NotNull
    private FileType fileType;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileReferenceDto that = (FileReferenceDto) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(fileName, that.fileName) && fileType == that.fileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, fileName, fileType);
    }

    @Override
    public String toString() {
        return "FileReferenceDto{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", fileName='" + fileName + '\'' +
                ", fileType=" + fileType +
                '}';
    }
}
