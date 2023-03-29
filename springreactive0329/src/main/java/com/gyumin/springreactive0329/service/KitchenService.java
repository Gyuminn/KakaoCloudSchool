package com.gyumin.springreactive0329.service;

import com.gyumin.springreactive0329.dto.Dish;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static reactor.core.publisher.Flux.generate;

@Service
public class KitchenService {
    private Random random = new Random();

    private List<Dish> menu = Arrays.asList(
            new Dish("치킨"),
            new Dish("스파게티"),
            new Dish("티본 스테이크")
    );

    private Dish randomDish() {
        return menu.get(random.nextInt(menu.size()));
    }

    public Flux<Dish> getDishes() {
        // 0.25초마다 데이터를 생성해서 리턴
        return Flux.<Dish> generate(sink -> sink.next(randomDish()))
                .delayElements(Duration.ofMillis(1000));
    }
}
