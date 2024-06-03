import java.util.Scanner;

public class CLI {
    public void readCommandLine(ManageTask manageTask) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
                }
                
                System.out.println("-----------------------------------------------");
                System.out.println("Enter the function as in Java, example: x^2 + 2*x + 1");
                String function = scanner.nextLine();

                System.out.println("\nEnter the initial point of the definite integral:");
                double initialPoint = scanner.nextDouble();

                System.out.println("\nEnter the final point of the definite integral:");
                double finalPoint = scanner.nextDouble();

                System.out.println("\nEnter the number of workers:");
                int workers = scanner.nextInt();

                System.out.println("\nEnter the number of random numbers to generate per thread per worker:");
                int cantNum = scanner.nextInt();

                System.out.println("\n**Processing the integral of the function: " + function + " from " + initialPoint + " to "
                        + finalPoint + " with " + workers + " workers.**\n");

                if (validateFields(manageTask, function, initialPoint, finalPoint, workers, cantNum)) {
                    Thread thread = manageTask.sendIntegral(function, initialPoint, finalPoint, workers, cantNum);
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("You have made a mistake, please try again.");
            }
        }
    }

    public boolean validateFields(ManageTask manageTask, String function, double initialPoint, double finalPoint, int workers, int cantNum) {
        if (function.isEmpty()) {
            System.out.println("Please instert a function.");
            return false;
        }

        if (initialPoint > finalPoint) {
            System.out.println("The initial point must be less than the final point.");
            return false;
        }

        if (workers > manageTask.callbackWorkersList.size()) {
            System.out.println("The number of workers is greater than the number of workers connected.");
            System.out.println("The number of workers connected is: " + manageTask.callbackWorkersList.size());
            System.out.println("Please connect more workers.");
            return false;
        }
        return true;
    }
}
