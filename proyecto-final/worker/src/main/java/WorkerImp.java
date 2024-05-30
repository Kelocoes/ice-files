import com.zeroc.Ice.Current;

public class WorkerImp implements Demo.Worker {

    @Override
    public void getTask(String Function, long start, long end, long time, Current current) {
        System.out.println("Task received: " + Function + " " + start + " " + end + " " + time);
    }

    @Override
    public void connectionResponse(String msg, Current current) {
        System.out.println(msg);
    }

}
