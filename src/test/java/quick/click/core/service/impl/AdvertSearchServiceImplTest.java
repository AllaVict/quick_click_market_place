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
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.config.factory.AdvertFactory;
import quick.click.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.AdvertReadWithoutAuthDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchServiceImpl")
class AdvertSearchServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdvertToAdvertReadDtoConverter advertToAdvertReadDtoConverter;

    @InjectMocks
    AdvertSearchServiceImpl advertSearchService;

    private Advert advertOne;

    private Advert advertTwo;

    private AdvertReadDto advertReadDtoOne;

    private AdvertReadDto advertReadDtoTwo;

    private List<AdvertReadDto> advertReadDtoList;

    private List<Advert> advertList;

    private User user;

    private static final long ADVERT_ID = 101L;

    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
    private static final String EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        user = createUser();
        advertOne = AdvertFactory.createAdvertOne(user);
        advertTwo = AdvertFactory.createAdvertOne(user);
        advertReadDtoOne = createAdvertReadDto();
        advertSearchService = new AdvertSearchServiceImpl(advertRepository, userRepository, advertToAdvertReadDtoConverter, null);

    }

    @Nested
    @DisplayName("When find an advert by id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_shouldReturnAdvertReadDtoByGivenId() {
            when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.ofNullable(advertOne));
            when(advertToAdvertReadDtoConverter.convert(advertOne)).thenReturn(advertReadDtoOne);

            AdvertReadWithoutAuthDto result = advertSearchService.findAdvertById(advertOne.getId());

            verify(advertRepository).findById(any(Long.class));
            assertNotNull(result);
            assertEquals(result, advertReadDtoOne);
            assertThat(result.getTitle()).isEqualTo(advertReadDtoOne.getTitle());
        }

        @Test
        void testFindAdvertById_AdvertDoesNotExist() {
            when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> advertSearchService.findAdvertById(ADVERT_ID));

            verify(advertRepository).findById(ADVERT_ID);
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

        @Test
        void testFindAdvertById_ShouldThrowException() {

            assertThrows(ResourceNotFoundException.class,
                    () -> advertSearchService.findAdvertById(advertOne.getId()));
        }
    }

    @Nested
    @DisplayName("When find all adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_ShouldReturnAllAdverts() {
            advertList = List.of(advertOne, advertTwo);
            advertReadDtoList = List.of(advertReadDtoOne, advertReadDtoOne);
            when(advertRepository.findAll()).thenReturn(advertList);
            when(advertToAdvertReadDtoConverter.convert(any(Advert.class))).thenReturn(advertReadDtoOne);

            List<AdvertReadWithoutAuthDto> result = advertSearchService.findAllAdverts();

            verify(advertRepository).findAll();
            assertNotNull(result);
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());

        }

        @Test
        void testFindAllAdverts_DatabaseError() {
            when(advertRepository.findAll()).thenThrow(new DataAccessException("Data access exception") {
            });

            Exception exception = assertThrows(DataAccessException.class, advertSearchService::findAllAdverts);

            assertEquals("Data access exception", exception.getMessage());
            verify(advertRepository).findAll();
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

        @Test
        void testFindAllAdverts_ShouldReturnEmptyList() {
            advertRepository.deleteAll();
            advertList = new ArrayList<>();
            advertReadDtoList = new ArrayList<>();
            when(advertRepository.findAll()).thenReturn(advertList);

            List<AdvertReadWithoutAuthDto> result = advertSearchService.findAllAdverts();

            verify(advertRepository).findAll();
            assertTrue(result.isEmpty());
            assertEquals(result, advertReadDtoList);
            assertTrue(result.isEmpty());

        }
    }


    @Nested
    @DisplayName("When find all adverts by created date desc")
    class FindAllByOrderByCreatedDateDescTests {

        @Test
        void testFindAllByOrderByCreatedDateDesc_ShouldReturnAllAdverts() {
            advertList = List.of(advertOne, advertTwo);
            advertReadDtoList = List.of(advertReadDtoOne, advertReadDtoOne);
            when(advertRepository.findAllByOrderByCreatedDateDesc()).thenReturn(advertList);
            when(advertToAdvertReadDtoConverter.convert(any(Advert.class))).thenReturn(advertReadDtoOne);

            List<AdvertReadDto> result = advertSearchService.findAllByOrderByCreatedDateDesc();

            verify(advertRepository).findAllByOrderByCreatedDateDesc();
            assertNotNull(result);
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());

        }

        @Test
        void testFindAllByOrderByCreatedDateDesc_DatabaseError() {
            when(advertRepository.findAllByOrderByCreatedDateDesc()).thenThrow(new DataAccessException("Data access exception") {
            });

            Exception exception = assertThrows(DataAccessException.class, advertSearchService::findAllByOrderByCreatedDateDesc);

            assertEquals("Data access exception", exception.getMessage());
            verify(advertRepository).findAllByOrderByCreatedDateDesc();
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

        @Test
        void testFindAllByOrderByCreatedDateDesc_ShouldReturnEmptyList() {
            advertRepository.deleteAll();
            advertList = new ArrayList<>();
            advertReadDtoList = new ArrayList<>();
            when(advertRepository.findAllByOrderByCreatedDateDesc()).thenReturn(advertList);

            List<AdvertReadDto> result = advertSearchService.findAllByOrderByCreatedDateDesc();

            verify(advertRepository).findAllByOrderByCreatedDateDesc();
            assertTrue(result.isEmpty());
            assertEquals(result, advertReadDtoList);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("When find all adverts by user ")
    class FindAllAdvertsByUserTests {

        @Test
        void testFindAllAdvertsByUser_ShouldReturnAllAdverts() {
            advertList = List.of(advertOne, advertTwo);
            advertReadDtoList = List.of(advertReadDtoOne, advertReadDtoOne);
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(advertRepository.findAllByUserOrderByCreatedDateDesc(any(User.class))).thenReturn(advertList);
            when(advertToAdvertReadDtoConverter.convert(any(Advert.class))).thenReturn(advertReadDtoOne);

            List<AdvertReadDto> result = advertSearchService.findAllAdvertsByUser(authenticatedUser);

            verify(advertRepository).findAllByUserOrderByCreatedDateDesc(user);
            assertNotNull(result);
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());
        }

        @Test
        void testFindAllAdvertsByUser_ShouldReturnNoAdverts() {
            advertList = new ArrayList<>();
            advertReadDtoList = new ArrayList<>();
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(advertRepository.findAllByUserOrderByCreatedDateDesc(any(User.class))).thenReturn(advertList);

            List<AdvertReadDto> result = advertSearchService.findAllAdvertsByUser(authenticatedUser);

            verify(advertRepository).findAllByUserOrderByCreatedDateDesc(user);
            assertTrue(result.isEmpty());
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());
        }

        @Test
        void testFindAllAdvertsByUser_DatabaseError() {
            when(authenticatedUser.getEmail()).thenReturn(EMAIL);
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(advertRepository.findAllByUserOrderByCreatedDateDesc(any(User.class)))
                    .thenThrow(new DataAccessException("Data access exception") {
                    });

            Exception exception = assertThrows(DataAccessException.class,
                    () -> advertSearchService.findAllAdvertsByUser(authenticatedUser));

            assertEquals("Data access exception", exception.getMessage());
            verify(advertRepository).findAllByUserOrderByCreatedDateDesc(any(User.class));
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

    }

}
