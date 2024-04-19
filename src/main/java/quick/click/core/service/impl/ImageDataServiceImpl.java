package quick.click.core.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.ImageData;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.ImageDataRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static quick.click.core.utils.ImageUtils.compressImage;
import static quick.click.core.utils.ImageUtils.decompressImage;

@Service
public class ImageDataServiceImpl implements ImageDataService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ImageDataServiceImpl.class);

    private final ImageDataRepository imageRepository;
    private final UserRepository userRepository;
    private final AdvertRepository advertRepository;

    @Value("${upload.windows}")
    private String WINDOWS_PATH;

    @Value("${upload.linux}")
    private String LINUX_PATH;

    public ImageDataServiceImpl(final ImageDataRepository imageRepository,
                                final UserRepository userRepository,
                                final AdvertRepository advertRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.advertRepository = advertRepository;
    }

    @Override
    public ImageData uploadImageToAdvert(final Long advertId,
                                         final MultipartFile file,
                                         final AuthenticatedUser authenticatedUser) throws IOException {

        final ImageData imageData = getImageData(advertId, file, authenticatedUser);
        uploadImageToFileSystem(file, imageData);

        LOGGER.info("In uploadImageToAdvert Uploading image to Advert with id: {}", advertId);

        return imageRepository.saveAndFlush(imageData);
    }

    @Override
    public List<byte[]> findByteListToAdvert(final Long advertId) {
        final List<ImageData> listToDecompress = imageRepository.findAllByAdvertId(advertId);
        final List<byte[]> byteList = new ArrayList<>();
        for (ImageData imageData : listToDecompress) {
            if (!ObjectUtils.isEmpty(imageData)) {
                byteList.add(decompressImage(imageData.getImageData()));
            }
        }
        return byteList;
    }

    @Override
    public ImageData findImageByIdAndByAdvertId(final Long imageId,
                                                final Long advertId) {

        final ImageData imageData = imageRepository.findByIdAndAdvertId(imageId, advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
        if (!ObjectUtils.isEmpty(imageData))
            decompressImageData(imageData);

        return imageData;
    }

    @Override
    public void deleteImageByIdAndByAdvertId(final Long imageId,
                                             final Long advertId,
                                             final AuthenticatedUser authenticatedUser) {

        final ImageData imageToDelete = imageRepository.findByIdAndAdvertId(imageId,advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));

        imageRepository.delete(imageToDelete);

    }

    private ImageData getImageData(Long advertId, MultipartFile file, AuthenticatedUser authenticatedUser) throws IOException {
        final String uuidFile = UUID.randomUUID().toString();
        final String resultFilename = uuidFile + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        ImageData imageData = new ImageData();
        imageData.setAdvert(advertRepository.findAdvertById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId)));
        imageData.setUserId(getUserIdByAuthenticatedUser(authenticatedUser));
        imageData.setType(file.getContentType());
        imageData.setImageData(compressImage(file.getBytes()));
        imageData.setName(resultFilename);
        imageData.setCreatedDate(LocalDateTime.now());
        return imageData;
    }

    private Long getUserIdByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {

        final String username = authenticatedUser.getEmail();

        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"))
                .getId();
    }

    protected ImageData decompressImageData(final ImageData imageData){
        ImageData decompressedImageData = new ImageData();
        imageData.setAdvert(imageData.getAdvert());
        imageData.setUserId(imageData.getUserId());
        imageData.setImageData(decompressImage(imageData.getImageData()));
        imageData.setName(imageData.getName());

        return decompressedImageData;
    }

     private String uploadImageToFileSystem(MultipartFile file, ImageData imageData) throws IOException {
        final String filePath;
        if(System.getProperty("os.name").equals("WINDOWS")){
            filePath=System.getProperty("user.dir")+WINDOWS_PATH+imageData.getName();
        } else {
            filePath=System.getProperty("user.dir")+LINUX_PATH+imageData.getName();
        }

        file.transferTo(new File(filePath));
        if (imageData != null)
            return "In uploadImageToFileSystem file uploaded successfully : " + filePath;

        return filePath;
    }

    //====================================================================

//    @Override
//    public void deleteImageDataListToAdvert(Long advertId) {
//        List<ImageData> listToDelete = imageRepository.findAllByAdvertId(advertId);
//        if (!listToDelete.isEmpty()){
//            for (int i=0; i < listToDelete.size(); i++) {
//                imageRepository.deleteById(listToDelete.get(i).getId());
//            }
//        }
//    }
//
//
//    @Override
//    public byte[] downloadImageFromFileSystem(Long advertId) throws IOException  {
//
//        ImageData imageData = imageRepository.findAllByAdvertId(advertId).stream().findFirst().orElseThrow();
//        final String filePath;
//        if(System.getProperty("os.name").equals("WINDOWS")){
//            filePath=System.getProperty("user.dir")+WINDOWS_PATH+imageData.getName();
//        } else {
//            filePath=System.getProperty("user.dir")+LINUX_PATH+imageData.getName();
//        }
//        return Files.readAllBytes(new File(filePath).toPath());
//    }

}
