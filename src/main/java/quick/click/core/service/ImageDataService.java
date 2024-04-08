package quick.click.core.service;

import org.springframework.web.multipart.MultipartFile;
import quick.click.core.domain.model.ImageData;

import java.io.IOException;
import java.util.List;

public interface ImageDataService  {

    ImageData uploadImageToAdvert(Long advertId, MultipartFile file) throws IOException;

    public ImageData findImageToAdvert(Long advertId);

    List<byte[]> findByteListToAdvert(Long advertId);

    byte[] findImageByIdAndByAdvertId(Long advertId, Long imageById);

    void deleteImageDataListToAdvert(Long advertId);

    void deleteImageById(Long imageId);

    byte[] downloadImageFromFileSystem(Long advertId) throws IOException;


}
