package kakao.itstudy.io;

import java.io.Serializable;

public class Data implements Serializable {
    private int num;
    private String name;
    private String address;

    public Data() {
        super();
    }

    public Data(int num, String name, String address) {
        super();
        this.num = num;
        this.name = name;
        this.address = address;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Data{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
