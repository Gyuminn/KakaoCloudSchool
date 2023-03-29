package com.gyumin.springreactive0329.controller;

import com.gyumin.springreactive0329.dto.Dish;
import com.gyumin.springreactive0329.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ServerController {
    private final KitchenService kitchenService;

    @GetMapping(value = "/server", produces = "application/stream+json")
    Flux<Dish> serverDished() {
        return kitchenService.getDishes();
    }

}
