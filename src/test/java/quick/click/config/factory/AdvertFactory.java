package quick.click.config.factory;

import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.enums.AdvertStatus;
import quick.click.core.enums.Category;
import quick.click.core.enums.Currency;

import java.time.LocalDateTime;

public class AdvertFactory {
    private static final long ADVERT_ID = 101L;

    private static final String TITLE = "Black cat";

    private static final String DESCRIPTION = "description a toy Black Cat";

    private static final Double PRICE = 100.00;

    private static final Double FIRST_PRICE = 80.00;

    private static final String ADDRESS = "Lviv";

    private static final String PHONE = "+380507778855";

    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2024, 10, 24, 20, 24);

    private AdvertFactory() {
    }

    public static Advert createAdvert(User user) {
        final Advert advert = new Advert();
        advert.setTitle(TITLE);
        advert.setDescription(DESCRIPTION);
        advert.setCategory(Category.TOYS);
        advert.setStatus(AdvertStatus.PUBLISHED);
        advert.setPhone(PHONE);
        advert.setPrice(PRICE);
        advert.setFirstPrice(FIRST_PRICE);
        advert.setFirstPriceDisplayed(true);
        advert.setCurrency(Currency.EUR);
        advert.setAddress(ADDRESS);
        advert.setUser(user);
        advert.setCreatedDate(CREATED_DATE);
        return advert;
    }

}
