package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import quick.click.core.converter.impl.AdvertCreateDtoToAdvertConverter;
import quick.click.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.FileReferenceRepository;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertDtoFactory.*;
import static quick.click.config.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertRegistrationServiceImpl")
class AdvertRegistrationServiceImplTest {

    private AdvertRepository advertRepository;

    private UserRepository userRepository;

    private AdvertToAdvertReadDtoConverter typeConverterReadDto = mock(AdvertToAdvertReadDtoConverter.class);

    private AdvertCreateDtoToAdvertConverter typeConverterCreateDto = mock(AdvertCreateDtoToAdvertConverter.class);

    private AdvertRegistrationServiceImpl advertRegistrationService;
    private Advert advert;
    private AdvertReadDto advertReadDto;
    private AdvertCreateDto advertCreateDto;
    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertCreateDto = createAdvertCreateDto();
        advertRegistrationService = new AdvertRegistrationServiceImpl(advertRepository,
                userRepository, typeConverterReadDto, typeConverterCreateDto);

    }

    @Nested
    @DisplayName("When register an advert")
    class RegisterAdvertTests {
        @Test
        void registerAdvert_ShouldReturnAdvertReadDto() {
            when(typeConverterCreateDto.convert(advertCreateDto)).thenReturn(advert);
            when(advertRepository.saveAndFlush(advert)).thenReturn(advert);
            when(typeConverterReadDto.convert(any(Advert.class))).thenReturn(advertReadDto);

            AdvertReadDto result = advertRegistrationService.registerAdvert(advertCreateDto, authenticatedUser);

            assertEquals(advertReadDto, result);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
            verify(advertRepository).saveAndFlush(any(Advert.class));
            assertNotNull(result);
        }

        @Test
        void testRegisterAdvert_ConversionFailure() {
            when(typeConverterCreateDto.convert(advertCreateDto)).thenThrow(new IllegalArgumentException("Invalid advert data"));

            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> advertRegistrationService.registerAdvert(advertCreateDto, authenticatedUser));
            assertEquals("Invalid advert data", exception.getMessage());

            verify(advertRepository, never()).saveAndFlush(any(Advert.class));
            verify(typeConverterReadDto, never()).convert(any(Advert.class));
        }

        @Test
        void testRegisterAdvert_RepositorySaveFailure() {
            when(typeConverterCreateDto.convert(advertCreateDto)).thenReturn(advert);
            when(advertRepository.saveAndFlush(advert)).thenThrow(new DataAccessException("Database error") {
            });

            assertThrows(DataAccessException.class,
                    () -> advertRegistrationService.registerAdvert(advertCreateDto, authenticatedUser),
                    "Expected DataAccessException to be thrown");

            verify(advertRepository).saveAndFlush(advert);
            verify(typeConverterReadDto, never()).convert(any(Advert.class));
        }

        @Test
        void testRegisterAdvert_ShouldThrowException() {

            assertThrows(NoSuchElementException.class,
                    () -> advertRegistrationService.registerAdvert(advertCreateDto, authenticatedUser));

        }

    }

}


