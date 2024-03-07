package quick.click.config.factory;

import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.enums.AdvertStatus;
import quick.click.core.enums.Category;
import quick.click.core.enums.Currency;

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

    public static AdvertReadDto createAdvertReadDto() {
        final AdvertReadDto advertReadDto = new AdvertReadDto();
        advertReadDto.setId(ADVERT_ID);
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
        advertReadDto.setUser(UserDtoFactory.createUserReadDto());
        return advertReadDto;
    }

    public static AdvertCreateDto createAdvertCreateDto() {
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
        advertCreateDto.setUserId(UserDtoFactory.createUserReadDto().getId());
        return advertCreateDto;
    }

    public static AdvertEditingDto createAdvertEditingDto() {
        final AdvertEditingDto advertEditingDto = new AdvertEditingDto();
        advertEditingDto.setId(ADVERT_ID);
        advertEditingDto.setTitle(TITLE);
        advertEditingDto.setDescription(DESCRIPTION);
        advertEditingDto.setCategory(Category.TOYS);
        advertEditingDto.setStatus(AdvertStatus.PUBLISHED);
        advertEditingDto.setPhone(PHONE);
        advertEditingDto.setPrice(PRICE);
        advertEditingDto.setFirstPrice(FIRST_PRICE);
        advertEditingDto.setFirstPriceDisplayed(true);
        advertEditingDto.setCurrency(Currency.EUR);
        advertEditingDto.setAddress(ADDRESS);
        advertEditingDto.setUserId(UserDtoFactory.createUserReadDto().getId());
        return advertEditingDto;
    }

}
