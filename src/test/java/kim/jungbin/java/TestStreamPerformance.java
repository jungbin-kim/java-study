package kim.jungbin.java;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Ignore
public class TestStreamPerformance {
    /*
    https://tw-you.tistory.com/entry/Spring-Webflux-Coroutine%EC%97%90%EC%84%9C-MDC%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-Context-%EA%B4%80%EB%A6%AC%ED%95%98%EA%B8%B0
    "간혹 stream을 이용해 context의 값을 옮겨주는 코드가 보이곤 하는데, 이는 성능을 저하시키기 때문에 지양하기 바란다..."
    위 내용을 보고 stream을 사용했을 때 성능이 저하되는지 궁금해서 테스트 코드를 작성해보았다.
    하지만 데이터가 많아야 성능 저하가 두드러지며, 데이터가 적을 경우에는 성능 차이가 미미하다.
     */


    @Test
    void main() {

        for (int i = 1; i < 8; i++) {
            int LOOP_MAX = 10 * (int)Math.pow(10, i);
            System.out.println("LOOP_MAX: " + LOOP_MAX);
            // Generate large data set
            List<Integer> data = new ArrayList<>();
            for (int j = 0; j < LOOP_MAX; j++) {
                data.add(j);
            }

            // Test with simple loop
            testForLoop(data);

            // Test with stream
            testStream(data);

            // Test with parallel stream
            testStreamParallel(data);
            System.out.println("=========================================");
        }

    }


    private void testForLoop(List<Integer> data) {
        long startTime = System.currentTimeMillis();
        List<Integer> result1 = new ArrayList<>();
        for (Integer value : data) {
            if (value % 2 == 0) {
                result1.add(value * 2);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Simple loop time: " + (endTime - startTime) + " ms");
    }


    private void testStream(List<Integer> data) {
        long startTime = System.currentTimeMillis();
        List<Integer> result2 = data.stream()
                                    .filter(value -> value % 2 == 0)
                                    .map(value -> value * 2)
                                    .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println("Stream time: " + (endTime - startTime) + " ms");
    }


    private void testStreamParallel(List<Integer> data) {
        long startTime = System.currentTimeMillis();
        List<Integer> result3 = data.parallelStream()
                                    .filter(value -> value % 2 == 0)
                                    .map(value -> value * 2)
                                    .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel stream time: " + (endTime - startTime) + " ms");
    }

}
