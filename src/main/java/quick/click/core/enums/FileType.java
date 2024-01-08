package quick.click.core.enums;


public enum FileType {

    AVATAR("AVATAR", "avatar-dir"),
    LOGO("LOGO", "logo-dir"),
    PRODUCT_IMAGE("PRODUCT_IMAGE", "product_image-dir"),
    UNSUPPORTED_FILE_TYPE("UNSUPPORTED FILE TYPE", "unsupported-file-type-dir") ;


    private final String value;

    private final String dirName;

    FileType(final String value, final String dirName) {
        this.value = value;
        this.dirName = dirName;
    }
}
