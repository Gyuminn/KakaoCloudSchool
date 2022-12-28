import org.mindrot.jbcrypt.BCrypt;

public class CryptMain {
    public static void main(String[] args) {
        // 복호화가 불가능한 암호화
        String encryptString = BCrypt.hashpw("abcdefghijklmn", BCrypt.gensalt());
        System.out.println(encryptString);

        // 123456789012345
        // $2a$10$M/4zuO7amri3g8bO5Tu0QewWY.v9InVJKLQUYnhRnvEGyviT5ncGq

        // abcdefghijklmn
        // $2a$10$9GNsfxu57tC/2xPNlVQzsuw.80gtpr6aI3LeJ/bQuY.HkKUqTDHzS

        // 두 번 해보니까 길이는 똑같이 나왔다.
        // 즉 DB password를 저장하는 것은 넉넉히 64 ~ 128로 설정해야 함.

        // 비교 - 평문이 먼저오고 암호화된 문장을 가져와야 한다. 순서가 바뀌면 안됨.
        System.out.println(BCrypt.checkpw("abcdefghijklmn", encryptString));
    }
}
