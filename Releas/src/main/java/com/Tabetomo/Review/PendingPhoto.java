package com.Tabetomo.Review;

import java.io.Serializable;

// レビュー投稿の確認画面を挟むフローで、アップロードされた写真のバイト列を
// セッションに一時保存しておくためのクラス。
// MultipartFile自体はリクエストが終わると使えなくなるため、中身だけ取り出して保持する。
public class PendingPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] data;
    private String originalFilename;
    private String contentType;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}