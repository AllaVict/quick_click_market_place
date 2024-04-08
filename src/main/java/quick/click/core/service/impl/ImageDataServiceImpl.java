package quick.click.core.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.ImageDataRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.ImageDataService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public ImageData uploadImageToAdvert(Long advertId, MultipartFile file) throws IOException {
        // User user = getUserByPrincipal(principal);
        User user = currentUser();
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        ImageData imageData = new ImageData();
        imageData.setAdvert(advertRepository.findAdvertById(advertId).orElseThrow());
        imageData.setUserId(user.getId());
        imageData.setType(file.getContentType());
        imageData.setImageData(compressImage(file.getBytes()));
        imageData.setName(resultFilename);
        imageData.setCreatedDate(LocalDateTime.now());
        uploadImageToFileSystem(file, imageData);

        LOGGER.info("In uploadImageToAdvert Uploading image to Advert with id: {}", advertId);

        return imageRepository.save(imageData);
    }

    @Override
    public List<byte[]> findByteListToAdvert(Long advertId) {
        List<ImageData> listToDecompress = imageRepository.findAllByAdvertId(advertId);
        List<byte[]> byteList = new ArrayList<>();
        for (ImageData imageData : listToDecompress) {
            if (!ObjectUtils.isEmpty(imageData)) {
                byteList.add(decompressImage(imageData.getImageData()));
            }
        }
        return byteList;
    }

    @Override
    public byte[] findImageByIdAndByAdvertId(Long imageId,Long advertId) {
        ImageData imageDataToDecompress = imageRepository.findByIdAndAdvertId(imageId, advertId)
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
        return   decompressImage(imageDataToDecompress.getImageData());
    }


    @Override
    public void deleteImageById(Long imageId) {
        ImageData imageToDelete = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
        imageRepository.delete(imageToDelete);
    }


 @Override
 public void deleteImageDataListToAdvert(Long advertId) {
    List<ImageData> listToDelete = imageRepository.findAllByAdvertId(advertId);
    if (!listToDelete.isEmpty()){
        for (int i=0; i < listToDelete.size(); i++) {
                  imageRepository.deleteById(listToDelete.get(i).getId());
            }
        }
    }

    public String uploadImageToFileSystem(MultipartFile file, ImageData imageData) throws IOException {
        final String filePath;
        if(System.getProperty("os.name") == "WINDOWS"){
            filePath=System.getProperty("user.dir")+WINDOWS_PATH+imageData.getName();
        } else {
            filePath=System.getProperty("user.dir")+LINUX_PATH+imageData.getName();
        }

        file.transferTo(new File(filePath));
        if (imageData != null)
            return "In uploadImageToFileSystem file uploaded successfully : " + filePath;

        return null;
    }

    @Override
    public ImageData findImageToAdvert(Long advertId) {

        ImageData imageData = imageRepository.findAllByAdvertId(advertId).stream().findFirst().orElseThrow();
        if (!ObjectUtils.isEmpty(imageData)) {
            decompressImageData(imageData);
        }
        return imageData;
    }

    @Override
    public byte[] downloadImageFromFileSystem(Long advertId) throws IOException  {

        ImageData imageData = imageRepository.findAllByAdvertId(advertId).stream().findFirst().orElseThrow();
        final String filePath;
        if(System.getProperty("os.name") == "WINDOWS"){
            filePath=System.getProperty("user.dir")+WINDOWS_PATH+imageData.getName();
        } else {
            filePath=System.getProperty("user.dir")+LINUX_PATH+imageData.getName();
        }
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
    private ImageData decompressImageData(ImageData imageData){
        ImageData decompressedImageData = new ImageData();
        imageData.setAdvert(imageData.getAdvert());
        imageData.setUserId(imageData.getUserId());
        imageData.setImageData(decompressImage(imageData.getImageData()));
        imageData.setName(imageData.getName());

        return decompressedImageData;
    }

    private User currentUser(){
        return userRepository.findUserById(1L).orElseThrow();
    }


}
