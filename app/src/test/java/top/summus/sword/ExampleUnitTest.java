package top.summus.sword;

import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public String test() {
        final String s = "hello";
        return s;
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(-1, true);
        System.out.println("start");
        new Runnable() {

            @Override
            public void run() {
                System.out.println("now semaphore:" + semaphore.availablePermits());
                semaphore.release();
                System.out.println("1 finished");
            }
        }.run();

        new Runnable() {
            @Override
            public void run() {
                System.out.println("now semaphore:" + semaphore.availablePermits());
                semaphore.release();
                System.out.println("2 finished");
            }
        }.run();
        try {
            semaphore.acquire();
            System.out.println("main proceed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}