import java.io.IOException;

import static control.FileManager.readFileContent;
import static control.ThreadManager.*;

class ThreadCreate {
    public static void main(String[] args) {
        Thread ThreadA = new ThreadA();
        Thread ThreadB = new ThreadB();
        Thread ThreadC = new ThreadC();
        ThreadA.start();
        ThreadB.start();
        ThreadC.start();
    }
}


class ThreadA extends Thread {

    @Override
    public void run() {
        try {
            String str = readFileContent("D:\\txt\\Z01-Example.txt").toLowerCase().replaceAll("\\d+","");
            getEN(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ThreadB extends Thread {

    @Override
    public void run() {
        try {
            String str = readFileContent("D:\\txt\\Z01-Example.txt").toLowerCase().replaceAll("\\d+","");
            getCH(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ThreadC extends Thread {

    @Override
    public void run() {
        try {
            sum();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}