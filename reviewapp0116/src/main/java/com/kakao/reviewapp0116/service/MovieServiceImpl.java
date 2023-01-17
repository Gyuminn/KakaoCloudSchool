package com.kakao.reviewapp0116.service;

import com.kakao.reviewapp0116.domain.Movie;
import com.kakao.reviewapp0116.domain.MovieImage;
import com.kakao.reviewapp0116.dto.MovieDTO;
import com.kakao.reviewapp0116.persistence.MovieImageRepository;
import com.kakao.reviewapp0116.persistence.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    // 이전과는 다르게 두 개의 레포지토리가 필요하다.
    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;

    @Override
    // 영화와, 영화이미지 정보를 두 번 넣기 때문에 꼭 트랜잭션 처리를 해줘야 한다.
    @Transactional
    public Long register(MovieDTO movieDTO) {
        log.warn("movieDTO:" + movieDTO);

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        // 영화와 영화 이미지 정보 찾아오기
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        // movie는 그냥 삽입하면 되는데
        movieRepository.save(movie);
        // imgList는 반복문을 통해 삽입한다.
        movieImageList.forEach(movieImage -> {
            movieImageRepository.save(movieImage);
        });
        return movie.getMno();
    }
}
