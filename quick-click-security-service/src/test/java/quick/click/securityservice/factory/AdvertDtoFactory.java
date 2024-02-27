package quick.click.securityservice.factory;

import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.enums.AdvertStatus;
import quick.click.advertservice.core.enums.Category;
import quick.click.advertservice.core.enums.Currency;

import java.time.LocalDateTime;

public class AdvertDtoFactory {
    private static final long ADVERT_ID = 101L;

    private static final String TITLE = "Black cat";

    private static final String DESCRIPTION = "description a toy Black Cat";

    private static final Double PRICE = 100.00;

    private static final Double FIRST_PRICE = 80.00;

    private static final String ADDRESS = "Lviv";

    private static final String PHONE = "+380507778855";

    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2024, 10, 24, 20, 24);

    private AdvertDtoFactory() {
    }

    public static AdvertReadDto createAdvertReadDto(UserReadDto userReadDto) {
        final AdvertReadDto advertReadDto = new AdvertReadDto();
        advertReadDto.setTitle(TITLE);
        advertReadDto.setDescription(DESCRIPTION);
        advertReadDto.setCategory(Category.TOYS);
        advertReadDto.setStatus(AdvertStatus.PUBLISHED);
        advertReadDto.setPhone(PHONE);
        advertReadDto.setPrice(PRICE);
        advertReadDto.setFirstPrice(FIRST_PRICE);
        advertReadDto.setFirstPriceDisplayed(true);
        advertReadDto.setCurrency(Currency.EUR);
        advertReadDto.setAddress(ADDRESS);
        advertReadDto.setUser(userReadDto);
        return advertReadDto;
    }

    public static AdvertCreateDto createAdvertCreateDto(Long userId) {
        final AdvertCreateDto advertCreateDto = new AdvertCreateDto();
        advertCreateDto.setTitle(TITLE);
        advertCreateDto.setDescription(DESCRIPTION);
        advertCreateDto.setCategory(Category.TOYS);
        advertCreateDto.setStatus(AdvertStatus.PUBLISHED);
        advertCreateDto.setPhone(PHONE);
        advertCreateDto.setPrice(PRICE);
        advertCreateDto.setFirstPrice(FIRST_PRICE);
        advertCreateDto.setFirstPriceDisplayed(true);
        advertCreateDto.setCurrency(Currency.EUR);
        advertCreateDto.setAddress(ADDRESS);
        advertCreateDto.setUserId(userId);
        return advertCreateDto;
    }

}
