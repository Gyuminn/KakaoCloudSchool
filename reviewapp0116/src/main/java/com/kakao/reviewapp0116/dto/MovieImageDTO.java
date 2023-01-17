package com.kakao.reviewapp0116.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieImageDTO {
    // movie_mno는 movie에 있으니까 안가져와도 된다.
    private String uuid;
    private String imgName;
    private String path;

    // 실제 이미지 경로를 리턴해주는 메서드
    public String getImageUrl() {
        try {
            return URLEncoder.encode(path + "/" + uuid + imgName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }

    // Thumbnail 이미지 경로를 리턴해주는 메서드
    public String getThumbnailUrl() {
        try {
            return URLEncoder.encode(path + "/s_" + uuid + imgName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }
}
