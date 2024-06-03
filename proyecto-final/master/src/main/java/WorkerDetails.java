import Demo.WorkerPrx;

public class WorkerDetails {
    private WorkerPrx workerPrx;
    private boolean busy;

    public WorkerDetails(WorkerPrx workerPrx, boolean busy) {
        this.workerPrx = workerPrx;
        this.busy = busy;
    }

    public WorkerPrx getWorkerPrx() {
        return workerPrx;
    }

    public boolean getBusy() {
        return busy;
    }

    public void setworkerPrx(WorkerPrx workerPrx) {
        this.workerPrx = workerPrx;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

}
