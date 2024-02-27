package quick.click.advertservice.core.converter.impl;

import org.springframework.stereotype.Component;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.domain.model.User;

@Component
public class UserToUserReadDtoConverter implements TypeConverter<User, UserReadDto> {

    @Override
    public Class<User> getSourceClass() {
        return User.class;
    }

    @Override
    public Class<UserReadDto> getTargetClass() {
        return UserReadDto.class;
    }

    @Override
    public UserReadDto convert(final User user) {
        final UserReadDto userReadDto = new UserReadDto();
        userReadDto.setId(user.getId());
        userReadDto.setFirstName(user.getFirstName());
        userReadDto.setEmail(user.getEmail());
        userReadDto.setRole(user.getRole());
        userReadDto.setStatus(user.getStatus());
        userReadDto.setProvider(user.getProvider());

        return userReadDto;
    }
}
