package top.summus.sword;

import org.junit.Test;

import java.util.Arrays;

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
        String currentPath = "/hello/sdfsd/";

        String path = "";
        String[] nodes = currentPath.split("/");
        System.out.println(Arrays.toString(nodes));
        for (int i = 0; i < nodes.length - 1; i++) {
            path += nodes[i] + "/";
        }
        System.out.println(path);

    }
}