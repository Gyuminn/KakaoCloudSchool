package kakao.itstudy.oop;

import java.util.Date;

public class MemberMain {
    public static void main(String[] args) {
        String[] nicknames = {"qmin", "Gyuminn"};

        // 인스턴스 생성
        Member member = new Member();
        member.setEmail("gyumin@gmail.com");
        member.setPassword("1234");
        member.setMarried(false);
        member.setAge(26);
        // 오늘 날짜
        // member.setBirthday(new Date());
        member.setBirthday(new Date(97, 0, 29));
        member.setNicknames(nicknames);
        System.out.println(member);


        Member member1 = new Member("gyumin@gmail.com", "1234", nicknames, new Date(97, 0, 29), false, 26);
        System.out.println(member1);
    }
}
