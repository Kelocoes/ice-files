import com.zeroc.Ice.Current;

import Demo.MasterPrx;

public class WorkerImp implements Demo.Worker {
    private ThreadPool threadPool = new ThreadPool();
    MasterPrx master;

    public void getTask(String Function, double start, double end, int cantNum, Current current) {
        System.out.println("Task received: " + Function + " " + start + " " + end);

        try {
            long startTime = System.currentTimeMillis();
            
            double result = threadPool.execute(Function, start, end, cantNum);
            double partialResult = (end - start) * result / (cantNum * Runtime.getRuntime().availableProcessors());

            long time = System.currentTimeMillis() - startTime;
            master.addPartialResult(partialResult, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectionResponse(String msg, Current current) {
        System.out.println(msg);
    }

    public void setMasterPrx(MasterPrx master) {
        this.master = master;
    }
}
