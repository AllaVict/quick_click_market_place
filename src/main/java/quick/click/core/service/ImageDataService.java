package quick.click.core.service;

import org.springframework.web.multipart.MultipartFile;
import quick.click.core.domain.model.ImageData;
import quick.click.security.commons.model.AuthenticatedUser;

import java.io.IOException;
import java.util.List;

public interface ImageDataService {

    ImageData uploadImageToAdvert(Long advertId, MultipartFile file, AuthenticatedUser authenticatedUser) throws IOException;

    byte[] findImageByIdAndByAdvertId(Long imageById, Long advertId);

    List<byte[]> findAllImagesAsByteListByAdvertId(Long advertId);

    List<Long> findAllImageDatasIdsByAdvertId(Long advertId);

    void deleteImageByIdAndByAdvertId(Long imageById,Long advertId, AuthenticatedUser authenticatedUser);

}
