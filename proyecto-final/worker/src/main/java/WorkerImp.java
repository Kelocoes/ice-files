import com.zeroc.Ice.Current;

import Demo.ManageTaskPrx;

public class WorkerImp implements Demo.Worker {
    private ThreadPool threadPool = new ThreadPool();
    ManageTaskPrx manageTaskPrx;

    public void connectionResponse(String msg, Current current) {
        System.out.println(msg);
    }

    public void setManageTaskPrx(ManageTaskPrx manageTaskPrx) {
        this.manageTaskPrx = manageTaskPrx;
    }

    public void getTask(String Function, double start, double end, int cantNum, Current current) {
        System.out.println("Task received: " + Function + " " + start + " " + end);

        try {
            long startTime = System.currentTimeMillis();
            double result = threadPool.execute(Function, start, end, cantNum);
            double partialResult = (end - start) * result / (cantNum * Runtime.getRuntime().availableProcessors());
            long time = System.currentTimeMillis() - startTime;
            manageTaskPrx.addPartialResult(partialResult, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
