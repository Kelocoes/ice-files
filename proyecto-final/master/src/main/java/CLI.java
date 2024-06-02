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
                
                System.out.println("\nEnter the function as in Java, example: x^2 + 2*x + 1");
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

                if (workers > manageTask.callbackWorkersList.size()) {
                    System.out.println("The number of workers is greater than the number of workers connected.");
                    System.out.println("The number of workers connected is: " + manageTask.callbackWorkersList.size());
                    System.out.println("Please connect more workers.");
                } else {
                    manageTask.sendIntegral(function, initialPoint, finalPoint, workers, cantNum);
                    System.out.println("The integral has been sent to the workers.");
                }
            } catch (Exception e) {
                System.out.println("You have made a mistake, please try again.");
            }
        }
    }
}
