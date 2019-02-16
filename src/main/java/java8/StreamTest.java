package java8;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StreamTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("a");
        list.add("a");
        list.add("a");

        long count = list.stream().filter(w -> w.length()<12).count();

        System.out.println(count);


        System.out.println( Instant.now());
    }
}
