package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static quick.click.config.factory.AdvertDtoFactory.createAdvertReadDto;
import static quick.click.config.factory.AdvertFactory.createAdvert;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdvertSearchServiceImpl")
class AdvertSearchServiceImplTest {

    @Mock
    private AdvertRepository advertRepository;

    @Mock
    private TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;


    @InjectMocks
    AdvertSearchServiceImpl advertSearchService;

    private Advert advert;

    private AdvertReadDto advertReadDto;

    private List<AdvertReadDto> advertReadDtoList;

    @BeforeEach
    void setUp() {
        advert = createAdvert();
        advertReadDto = createAdvertReadDto();
    }

    @Nested
    @DisplayName("When Find Advert By Id")
    class FindAdvertByIdTests {
        @Test
        void testFindAdvertById_shouldReturnAdvertReadDtoByGivenId() {
            when(advertRepository.findById(advert.getId())).thenReturn(Optional.ofNullable(advert));
            when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertSearchService.findAdvertById(advert.getId());

            verify(advertRepository).findById(any(Long.class));
            assertNotNull(result);
            assertEquals(result, advertReadDto);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
        }

        @Test
        void testFindAdvertById_shouldThrowException() {

            assertThrows(NoSuchElementException.class,
                    () -> advertSearchService.findAdvertById(advert.getId()));
        }
    }

    @Nested
    @DisplayName("When Find All Adverts")
    class FindAllAdvertsTests {

        @Test
        void testFindAllAdverts_shouldReturnAllAdverts() {
            advertReadDtoList = List.of(advertReadDto, advertReadDto);
            when(advertRepository.findById(advert.getId())).thenReturn(Optional.ofNullable(advert));
            when(typeConverterReadDto.convert(advert)).thenReturn(advertReadDto);

            AdvertReadDto result = advertSearchService.findAdvertById(advert.getId());

            verify(advertRepository).findById(any(Long.class));
            assertNotNull(result);
            assertEquals(result, advertReadDto);
            assertThat(result.getTitle()).isEqualTo(advertReadDto.getTitle());
        }

        @Test
        void testFindAllAdverts_shouldThrowException() {

            assertThrows(NoSuchElementException.class,
                    () -> advertSearchService.findAdvertById(advert.getId()));
        }
    }

}
