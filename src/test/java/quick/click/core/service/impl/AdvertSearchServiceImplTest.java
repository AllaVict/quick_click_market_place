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
import quick.click.core.converter.impl.AdvertToAdvertReadDtoConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchServiceImpl")
class AdvertSearchServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private AdvertToAdvertReadDtoConverter advertToAdvertReadDtoConverter;

    @InjectMocks
    AdvertSearchServiceImpl advertSearchService;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    private List<AdvertReadDto> advertReadDtoList;

    private List<Advert> advertList;

    private static final long ADVERT_ID = 101L;

    private static final long USER_ID = 101L;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
        advertSearchService = new AdvertSearchServiceImpl(advertRepository, userRepository, advertToAdvertReadDtoConverter);
    }

    @Nested
    @DisplayName("When find an advert by id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_shouldReturnAdvertReadDtoByGivenId() {
            when(advertRepository.findById(advert.getId())).thenReturn(Optional.ofNullable(advert));
            when(advertToAdvertReadDtoConverter.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertSearchService.findAdvertById(advert.getId());

            verify(advertRepository).findById(any(Long.class));
            assertNotNull(result);
            assertEquals(result, advertReadDto);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
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
                    () -> advertSearchService.findAdvertById(advert.getId()));
        }
    }

    @Nested
    @DisplayName("When find all adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_ShouldReturnAllAdverts() {
            advertList = List.of(advert, advert);
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            when(advertRepository.findAll()).thenReturn(advertList);
            when(advertToAdvertReadDtoConverter.convert(any(Advert.class))).thenReturn(advertReadDto);

            List<AdvertReadDto> result = advertSearchService.findAllAdverts();

            verify(advertRepository).findAll();
            assertNotNull(result);
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());

        }
        @Test
        void testFindAllAdverts_DatabaseError() {
            when(advertRepository.findAll()).thenThrow(new DataAccessException("Data access exception") {});

            Exception exception = assertThrows(DataAccessException.class,
                    advertSearchService::findAllAdverts);

            assertEquals("Data access exception", exception.getMessage());
            verify(advertRepository).findAll();
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

        @Test
        void testFindAllAdverts_ShouldReturnNoAdverts() {
            advertRepository.deleteAll();
            advertList = new ArrayList<>();
            advertReadDtoList = new ArrayList<>();
            when(advertRepository.findAll()).thenReturn(advertList);

            List<AdvertReadDto> result = advertSearchService.findAllAdverts();

            verify(advertRepository).findAll();
            assertTrue(result.isEmpty());
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());
        }
    }

    @Nested
    @DisplayName("When find all adverts by user id ")
    class FindAllAdvertsByUserIdTests {

        @Test
        void testFindAllAdvertsByUserId_ShouldReturnAllAdverts() {
            advertList = List.of(advert, advert);
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            when(advertRepository.findAllAdvertsByUserId(USER_ID)).thenReturn(advertList);
            when(advertToAdvertReadDtoConverter.convert(any(Advert.class))).thenReturn(advertReadDto);

           List<AdvertReadDto> result = advertSearchService.findAllAdvertsByUserId(USER_ID);

            verify(advertRepository).findAllAdvertsByUserId(USER_ID);
            assertNotNull(result);
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());
        }
        @Test
        void testFindAllAdvertsByUserId_ShouldReturnNoAdverts() {
            advertList = new ArrayList<>();
            advertReadDtoList = new ArrayList<>();
            when(advertRepository.findAllAdvertsByUserId(USER_ID)).thenReturn(advertList);

            List<AdvertReadDto> result = advertSearchService.findAllAdvertsByUserId(USER_ID);

            verify(advertRepository).findAllAdvertsByUserId(USER_ID);
            assertTrue(result.isEmpty());
            assertEquals(result, advertReadDtoList);
            assertThat(result.size()).isEqualTo(advertReadDtoList.size());
        }

        @Test
        void testFindAllAdvertsByUserId_DatabaseError() {
            when(advertRepository.findAllAdvertsByUserId(USER_ID)).thenThrow(new DataAccessException("Data access exception") {});

            Exception exception = assertThrows(DataAccessException.class,
                    () -> advertSearchService.findAllAdvertsByUserId(USER_ID));

            assertEquals("Data access exception", exception.getMessage());
            verify(advertRepository).findAllAdvertsByUserId(USER_ID);
            verify(advertToAdvertReadDtoConverter, never()).convert(any(Advert.class));
        }

    }

}
