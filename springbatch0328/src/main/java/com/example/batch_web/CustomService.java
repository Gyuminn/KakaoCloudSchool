package com.example.batch_web;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomService {
    public void businessLogic(){
        for(int idx=0; idx < 10; idx++){
            log.info("Method Invoke를 이용한 Step 구성");
        }
    }
}
