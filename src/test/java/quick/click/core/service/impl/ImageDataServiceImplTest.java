package quick.click.core.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.ImageData;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.ImageDataRepository;
import quick.click.core.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ImageDataServiceImplTest {
    @Mock
    private ImageDataRepository imageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertRepository advertRepository;

    @InjectMocks
    private ImageDataServiceImpl imageDataService;
    @Test    void findByteListToAdvert() {    }
    @Test    void findImageByIdAndByAdvertId() {    }
    @Test    void deleteImageByIdAndByAdvertId() {    }

    @Test
    void testUploadImageToAdvert() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "Hello, World!".getBytes());
        Advert advert = new Advert();
        advert.setId(1L);
        User user = new User();
        user.setId(1L);

        when(advertRepository.findAdvertById(anyLong())).thenReturn(Optional.of(advert));
     //   when(userRepository.currentUser()).thenReturn(user);
        doNothing().when(imageDataService)
                .uploadImageToFileSystem(any(MultipartFile.class), any(ImageData.class));

        ImageData result = imageDataService.uploadImageToAdvert(1L, file);

        assertNotNull(result);
        verify(imageRepository).saveAndFlush(any(ImageData.class));
    }

    @Test
    void testFindByteListToAdvert() {
        List<ImageData> images = List.of(new ImageData()); // Set up test data
        when(imageRepository.findAllByAdvertId(anyLong())).thenReturn(images);
        doReturn(new byte[0]).when(imageDataService).decompressImageData(any());

        List<byte[]> result = imageDataService.findByteListToAdvert(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindImageByIdAndByAdvertId() {
        ImageData imageData = new ImageData();
        when(imageRepository.findByIdAndAdvertId(anyLong(), anyLong())).thenReturn(Optional.of(imageData));
        doNothing().when(imageDataService).decompressImageData(any(ImageData.class));

        ImageData found = imageDataService.findImageByIdAndByAdvertId(1L, 1L);

        assertNotNull(found);
    }
    @Test
    void testDeleteImageByIdAndByAdvertId() {
        ImageData imageData = new ImageData();
        when(imageRepository.findByIdAndAdvertId(anyLong(), anyLong())).thenReturn(Optional.of(imageData));

        imageDataService.deleteImageByIdAndByAdvertId(1L, 1L);

        verify(imageRepository).delete(imageData);
    }

    @Test
    void testCompressAndDecompressImage() throws IOException {
        byte[] originalData = "TestData".getBytes();
//        byte[] compressedData = imageDataService.compressImage(originalData);
//        byte[] decompressedData = imageDataService.decompressImage(compressedData);
//
//        assertArrayEquals(originalData, decompressedData);
    }

}