package com.gyumin.springevent0323.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {
    // 회원가입과 탈퇴를 구분하기 위한 속성
    private Type type;
    private Long userId;
    private String emailAddress;

    // 보통 이렇게 안했었음.
    // 메시지를 위해 상속받으려고 해보니 super() 가 에러난다.
    // 이런 경우 상위 클래스에 ~~ 한 것이다.
    private UserEvent(Object source, Type type, Long userId, String emailAddress) {
        super(source);
        this.type = type;
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    // 위에서 생성자를 private으로 만들었기 때문에
    // 외부에서 생성자를 만들 수 있도록 하는 메서드를 만들어야 함.
    // 인스턴스를 생성해주는 별도의 static 메서드 생성
    // 싱글톤 패턴을 적용할 때 또는 생성하는 코드가 복잡할 때 주로 이용
    public static UserEvent created(Object source, Long userId, String emailAddress) {
        return new UserEvent(source, Type.CREATE, userId, emailAddress);
    }
    public enum Type{
        CREATE, DELETE
    }
}
