package quick.click.core.domain.dto;

public class LoginDto extends BaseLoginDto{

    private String username; //login

    private String password;

    public LoginDto() {
        loginType = LoginType.EMAIL_LOGIN;
    }

    public LoginDto(final String username, final String password) {
        this.username = username;
        this.password = password;
        loginType = LoginType.EMAIL_LOGIN;
    }

    public LoginDto(final String username, final String password, final LoginType loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
