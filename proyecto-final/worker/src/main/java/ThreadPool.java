import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import java.util.Random;

public class ThreadPool {
    private ExecutorService executor;
    private int numThreads = Runtime.getRuntime().availableProcessors();
    private int classNumber = 0;
    private CtClass ctClass;
    private List<Future<Double>> futures = new ArrayList<>();
    private Random rand = new Random();

    public ThreadPool() {
        executor = Executors.newFixedThreadPool(numThreads);
    }

    public double execute(String function, double start, double end, int cantNum) throws Exception {
        double range = end - start;
        double division = range / numThreads;
        double finalResult = 0;
        ArrayList<Double> results = new ArrayList<Double>();
        System.out.println(range + " " + division + " " + numThreads + " " + cantNum + " " + function);

        Class<?> dynamicClass = createDynamicClassWithMethod(function);
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();
        java.lang.reflect.Method applyMethod = dynamicClass.getMethod("apply", double.class);
        
        for (int k = 0; k < cantNum; k++) {
            futures.clear();
            for (int i = 0; i < numThreads; i++) {
                double threadStart = start + (i * division);
                double threadEnd = threadStart + division;
                
                Future<Double> future = executor.submit(() -> {
                    double y = 0;
                    for (int j = 0; j < cantNum; j++) {
                        double randomDouble = threadStart + (threadEnd - threadStart) * rand.nextDouble();
                        y += applyFunction(applyMethod, instance, randomDouble);
                    }
                    return y;
                });
    
                futures.add(future);
            }

            double result = 0;
            for (Future<Double> future : futures) {
                result += future.get();
            }

            results.add(result);
        }

        finalResult = results.stream().mapToDouble(a -> a).sum() / results.size();

        ctClass.detach();
        classNumber++;
        return finalResult;
    }

    private double applyFunction(java.lang.reflect.Method method, Object instance, double value) throws Exception {
        double result = (double) method.invoke(instance, value);
        return result;
    }

    private Class<?> createDynamicClassWithMethod(String function) throws CannotCompileException, Exception{
        ctClass = ClassPool.getDefault().makeClass("MyClass" + classNumber);
        ctClass.addMethod(CtMethod.make("public double apply(double x) { return " + function + " ; }", ctClass));
        Class<?> dynamicClass = ctClass.toClass();
        return dynamicClass;
    }
}
