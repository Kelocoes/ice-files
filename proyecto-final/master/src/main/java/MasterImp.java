import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

public class MasterImp implements Demo.Master {
    // ThreadPool ca;

    List<Map.Entry<String, Demo.WorkerPrx>> callbackWorkersList = new ArrayList<>();

    MasterImp() {
        callbackWorkersList = new ArrayList<>();
    }

    public void connectToMaster(String s, WorkerPrx worker, Current current) {
        callbackWorkersList.add(new java.util.AbstractMap.SimpleEntry<>(s, worker));
        System.out.println("Worker " + s + " connected");
        worker.connectionResponse("Connected to the Master");
    }

    public void readCommandLine() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                scanner.close();
                break;
            }

            System.out.println("Enter the function example: x^2 + 2x + 1");
            String function = scanner.nextLine();

            System.out.println("Enter the initial point of the definite integral:");
            double initialPoint = scanner.nextDouble();

            System.out.println("Enter the final point of the definite integral:");
            double finalPoint = scanner.nextDouble();

            System.out.println("Enter the number of workers:");
            int workers = scanner.nextInt();

            System.out.println("Processing the integral of the function: " + function + " from " + initialPoint + " to "
                    + finalPoint + " with " + workers + " workers.");

            if (workers > callbackWorkersList.size()) {
                System.out.println("The number of workers is greater than the number of workers connected.");
                System.out.println("The number of workers connected is: " + callbackWorkersList.size());
                System.out.println("Please connect more workers.");
            } else {
                sendPartialIntegralsToWorkers(function, initialPoint, finalPoint, workers);
            }
        }
    }

    public void sendPartialIntegralsToWorkers(String function, double initialPoint, double finalPoint, int workers) {
        double interval = (finalPoint - initialPoint) / workers;
        double start = initialPoint;
        double end = start + interval;

        for (int i = 0; i < workers; i++) {
            Map.Entry<String, Demo.WorkerPrx> entry = callbackWorkersList.get(i);
            entry.getValue().getTask(function, start, end);
            start = end;
            end = start + interval;
        }
    }
}
