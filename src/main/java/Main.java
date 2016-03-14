import java.io.File;
import java.util.concurrent.BlockingQueue;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(-1 >> 16);
        DB db = DBMaker.newFileDB(new File("target/db")).make();
        BlockingQueue<String> fifo = db.createQueue("fifo", Serializer.STRING, false);
        fifo.add("hello");
        fifo.add("boo");
        String x = null;
        while ((x = fifo.poll()) != null) {
            System.out.println(x);
        }
    }

}