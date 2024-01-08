package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.User;
import quick.click.core.enums.Role;
import quick.click.core.enums.Sex;
import quick.click.core.enums.UserStatus;
import quick.click.core.repository.FileReferenceRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final FileReferenceRepository fileReferenceRepository;

    public UserServiceImpl(final UserRepository userRepository,
                           final FileReferenceRepository fileReferenceRepository) {
        this.userRepository = userRepository;
        this.fileReferenceRepository = fileReferenceRepository;
    }

    @Override
    @Transactional
    public User save(final User user) {
        final var savedAvatar = fileReferenceRepository.saveAndFlush(user.getAvatar());
        user.setAvatar(savedAvatar);
        final var savedUser = userRepository.saveAndFlush(user);
        LOG.debug("Successfully saved user: {}", user);
        return savedUser;
    }

    @Override
    @Transactional
    public void update(final User user) {
        findById(user.getId());
        userRepository.saveAndFlush(user);
        LOG.debug("Successfully updated user: {}", user);
    }

    @Override
    public User findById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Cannot find user with id: " + userId));
    }

    @Override
    public boolean completelyDeleteById(final Long userId) {
        final User userForDelete = findById(userId);
        if (userForDelete.getRole().equals(Role.ROLE_USER)) {
            deleteAvatarReferenceIfExists(userForDelete);
            userRepository.delete(userForDelete);
            LOG.debug("Completely deleted user: {}", userForDelete);
            return true;
        }
        LOG.warn("Fail to delete user with id:[{}], email:[{}], role:[{}]. "
                        + "Reason: complete deletion only applied to user with role:[{}]",
                userId, userForDelete.getEmail(), userForDelete.getRole(), Role.ROLE_USER
        );
        return false;
    }

    @Override
    @Transactional
    public void deleteByIdComplete(final Long userId) {
        LOG.debug(
                "In deleteById - Set user status to 'DELETED' and clear information about for user with id:[{}]",
                userId
        );
        final var userForDelete = findById(userId);
        final var clearedUser = clearUserInformation(userForDelete);
        deleteAvatarReferenceIfExists(userForDelete);
        userRepository.saveAndFlush(clearedUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private User clearUserInformation(final User user) {
        final var clearedUser = getEmptyUser();
        clearedUser.setId(user.getId());
        clearedUser.setLocale(user.getLocale());
        clearedUser.setRole(user.getRole());
        clearedUser.setStatus(UserStatus.DELETED);
        return clearedUser;
    }

    private void deleteAvatarReferenceIfExists(final User user) {
        if (user.getAvatar() != null) {
            fileReferenceRepository.deleteById(user.getAvatar().getId());
        }
    }

    protected User getEmptyUser() {
        return new User(
                "Deleted user",
                "Deleted user",
                Sex.FEMALE,
                null,
                "psw"
        );
    }

}

