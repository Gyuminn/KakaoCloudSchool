package test.java;

import junit.framework.Assert;
import main.java.Source;
import org.junit.jupiter.api.Test;

public class SourceTest2 {
    @Test
    public void testmethod() {
        System.out.println(new Source().add(100, 200));
    }
    
    @Test
    public void testmethod2() {
        Source source = new Source();
        // 메서드의 수행 결과를 찾아오기
        int result = source.add(200, 300);
        // 기대값과 비교
        // 일치하면 정상 수행 하지만 일치하지 않으면 예외를 발생시킴
        Assert.assertEquals(result, 300);
    }
}
