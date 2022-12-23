package kakao.itstudy.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SerializableMain {
    public static void main(String[] args) {
        // 인스턴스 단위로 기록할 수 있는 스트림 생성
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sample.dat"))) {
            // 기록할 인스턴스 생성
            Data data = new Data(1, "gyumin", "강릉");
            oos.writeObject(data);
            oos.flush();

        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sample.dat"))) {
            Data data = (Data)ois.readObject();
            System.out.println(data);
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
