package quick.click.core.enums;

public enum AuthProvider {
    LOCAL,
    GOOGLE;

    public static AuthProvider getNameById(int id) {
        for (AuthProvider authProvider : AuthProvider.values()) {
            if (authProvider.ordinal() == id) {
                return authProvider;
            }
        }
        throw new IllegalArgumentException("Invalid AuthProvider ID: " + id);
    }
}
