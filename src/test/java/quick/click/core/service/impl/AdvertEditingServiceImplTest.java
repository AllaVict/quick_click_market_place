package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertDtoFactory.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.UserFactory.createUser;
import static quick.click.core.enums.AdvertStatus.ARCHIVED;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertEditingServiceImpl")
class AdvertEditingServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;
    @Mock

    private UserRepository userRepository;

    @Mock
    private AdvertToAdvertReadDtoConverter typeConverterReadDto;

    @InjectMocks
    AdvertEditingServiceImpl advertEditingService;

    private static final long ADVERT_ID = 101L;

    private Advert advert;
    private AdvertReadDto advertReadDto;

    private AdvertEditingDto advertEditingDto;

    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

    private static final String EMAIL = "test@example.com";

    private User user;

    @BeforeEach
    void setUp() {
        advert = createAdvertOne();
        advertReadDto = createAdvertReadDto();
        advertEditingDto = createAdvertEditingDto();
        user = createUser();
    }

    @Nested
    @DisplayName("When edit an advert")
    class EditAdvertTests {
        @Test
        void testEditAdvert_ShouldReturnAdvertReadDto() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(advertRepository.findAdvertByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(advert));
            when(advertRepository.saveAndFlush(advert)).thenReturn(advert);
            when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertEditingService.editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser);

            assertEquals(advertReadDto, result);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
            assertNotNull(result);
            verify(advertRepository).saveAndFlush(any(Advert.class));
        }

        @Test
        void testUpdateAdvert_ShouldReturnUpdatedAdvertReadDto() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);

            Advert result = advertEditingService.updateAdvertData(advert, advertEditingDto);

            assertNotNull(result);
            assertEquals(advert, result);
            assertThat(result.getTitle()).isEqualTo(advert.getTitle());
        }

        @Test
        void testEditAdvert_ShouldThrowException_WhenAdvertDoesNotExist() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

            assertThrows(ResourceNotFoundException.class,
                    () -> advertEditingService.editAdvert(ADVERT_ID, advertEditingDto, authenticatedUser));

            verify(advertRepository, never()).save(any(Advert.class));
        }

    }

    @Nested
    @DisplayName("When archive an advert")
    class ArchiveAdvertTests {
        @Test
        void testArchiveAdvert_ShouldReturnAdvertReadDto() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            advertReadDto.setStatus(ARCHIVED);
            when(advertRepository.findAdvertByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(advert));
            when(advertRepository.saveAndFlush(any(Advert.class))).thenReturn(advert);
            when(typeConverterReadDto.convert(any(Advert.class))).thenReturn(advertReadDto);

            AdvertReadDto result = advertEditingService.archiveAdvert(ADVERT_ID, authenticatedUser);

            assertEquals(advertReadDto, result);
            assertNotNull(result);
            verify(advertRepository).saveAndFlush(any(Advert.class));
            assertThat(result.getStatus()).isEqualTo(advertReadDto.getStatus());
        }

        @Test
        void testArchiveAdvert_ShouldThrowException_WhenAdvertDoesNotExist() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

            assertThrows(ResourceNotFoundException.class,
                    () -> advertEditingService.archiveAdvert(ADVERT_ID, authenticatedUser));

            verify(advertRepository, never()).save(any(Advert.class));
        }

    }

    @Nested
    @DisplayName("When delete an advert")
    class DeleteAdvertTests {
        @Test
        void testDeleteAdvert_ShouldDeleteAndReturnAdvertReadDto() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(advertRepository.findAdvertByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(advert));
            doNothing().when(advertRepository).delete(any(Advert.class));

            advertEditingService.deleteAdvert(ADVERT_ID, authenticatedUser);

            verify(advertRepository, times(1)).delete(advert);
            verify(userRepository).findUserByEmail(any(String.class));
            verify(advertRepository).findAdvertByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        void testDeleteAdvert_ShouldThrowException_WhenAdvertDoesNotExist() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

            assertThrows(ResourceNotFoundException.class,
                    () -> advertEditingService.deleteAdvert(ADVERT_ID, authenticatedUser));

            verify(advertRepository, never()).delete(any(Advert.class));
        }

    }

}

