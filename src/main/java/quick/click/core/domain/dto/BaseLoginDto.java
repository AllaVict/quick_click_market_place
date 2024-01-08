package quick.click.core.domain.dto;

public abstract class BaseLoginDto {

    protected LoginType loginType;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(final LoginType loginType) {
        this.loginType = loginType;
    }
}
