package quick.click.core.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.repository.ImageDataRepository;

import java.util.ArrayList;
import java.util.List;

import static quick.click.core.utils.ImageUtils.decompressImage;

@Component
public class AdvertToAdvertReadDtoConverter implements TypeConverter<Advert, AdvertReadDto> {

    private final TypeConverter<User, UserReadDto> typeConverterUserReadDto;
    private final TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto;

    private final ImageDataRepository imageDataRepository;
    @Autowired
    public AdvertToAdvertReadDtoConverter(final TypeConverter<User, UserReadDto> typeConverterUserReadDto,
                                          final TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto,
                                          final ImageDataRepository imageDataRepository) {
        this.typeConverterUserReadDto = typeConverterUserReadDto;
        this.typeConverterCommentReadDto = typeConverterCommentReadDto;
        this.imageDataRepository = imageDataRepository;
    }

    @Override
    public Class<Advert> getSourceClass() {
        return Advert.class;
    }

    @Override
    public Class<AdvertReadDto> getTargetClass() {
        return AdvertReadDto.class;
    }

    @Override
    public AdvertReadDto convert(final Advert advert) {
        final AdvertReadDto advertReadDto = new AdvertReadDto();
        advertReadDto.setId(advert.getId());
        advertReadDto.setTitle(advert.getTitle());
        advertReadDto.setDescription(advert.getDescription());
        advertReadDto.setCategory(advert.getCategory());
        advertReadDto.setStatus(advert.getStatus());
        advertReadDto.setPhone(advert.getPhone());
        advertReadDto.setPrice(advert.getPrice());
        advertReadDto.setFirstPrice(advert.getFirstPrice());
        advertReadDto.setFirstPriceDisplayed(advert.isFirstPriceDisplayed());
        advertReadDto.setCurrency(advert.getCurrency());
        advertReadDto.setAddress(advert.getAddress());
        advertReadDto.setFavorite(advert.isFavorite());
        advertReadDto.setComments(typeConverterCommentReadDto.convert(advert.getComments()));
        advertReadDto.setImages(getImages(advert.getId()));

        advertReadDto.setUser(typeConverterUserReadDto.convert(advert.getUser()));
        return advertReadDto;
    }

    private List<byte[]> getImages(Long advertId){
        List<ImageData> listToDecompress = imageDataRepository.findAllByAdvertId(advertId);
        List<byte[]> byteList = new ArrayList<>();
        for (ImageData imageData : listToDecompress) {
            if (!ObjectUtils.isEmpty(imageData)) {
                byteList.add(decompressImage(imageData.getImageData()));
            }
        }
       return byteList;
    }
}
