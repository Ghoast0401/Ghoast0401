package com.Tabetomo.shop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Tabetomo.Review.PendingPhoto;
import com.Tabetomo.Review.ReviewEntity;
import com.Tabetomo.Review.ReviewRepository;

@Service
public class PhotoService {
	@Autowired
	private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * 店舗画像を保存
     */
    public void saveAll(List<MultipartFile> files, ShopEntity shop) {

        if (files == null || files.isEmpty() || shop == null) {
            return;
        }

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            try {

                String filePath = storeFile(
                        file.getBytes(),
                        file.getOriginalFilename());

                PhotoEntity photo = new PhotoEntity();
                photo.setUrl(filePath);
                photo.setShop(shop);

                photoRepository.save(photo);

            } catch (IOException e) {
                throw new RuntimeException("写真の保存に失敗しました", e);
            }
        }
    }

    /**
     * MultipartFileをセッション保存用に変換
     */
    public static List<PendingPhoto> toPendingPhotos(List<MultipartFile> files) {

        List<PendingPhoto> result = new ArrayList<>();

        if (files == null) {
            return result;
        }

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            try {

                PendingPhoto pending = new PendingPhoto();

                pending.setData(file.getBytes());
                pending.setOriginalFilename(file.getOriginalFilename());
                pending.setContentType(file.getContentType());

                result.add(pending);

            } catch (IOException e) {

                throw new RuntimeException(e);

            }
        }

        return result;
    }

    /**
     * ファイル保存
     */
    public String storeFile(byte[] data, String originalFilename) throws IOException {

        Path dir = Paths.get(uploadDir);

        Files.createDirectories(dir);

        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {

            extension =
                    originalFilename.substring(originalFilename.lastIndexOf('.'));

        }

        String storedFilename =
                UUID.randomUUID() + extension;

        Path target = dir.resolve(storedFilename);

        Files.copy(
                new ByteArrayInputStream(data),
                target,
                StandardCopyOption.REPLACE_EXISTING);

        return "/images/uploads/" + storedFilename;
    }
    public void savePending(List<PendingPhoto> pendingPhotos, Long reviewId) {

        if (pendingPhotos == null || pendingPhotos.isEmpty() || reviewId == null) {
            return;
        }

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));

        for (PendingPhoto pending : pendingPhotos) {

            if (pending == null
                    || pending.getData() == null
                    || pending.getData().length == 0) {
                continue;
            }

            try {

                String url = storeFile(
                        pending.getData(),
                        pending.getOriginalFilename());

                PhotoEntity photo = new PhotoEntity();
                photo.setUrl(url);

                // レビューに紐付ける
                photo.setReview(review);

                photoRepository.save(photo);

            } catch (IOException e) {
                throw new RuntimeException(
                        "写真の保存に失敗しました : "
                                + pending.getOriginalFilename(),
                        e);
            }
        }
    }
}