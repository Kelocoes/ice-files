import com.zeroc.Ice.Current;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class WorkerImp implements Demo.Worker {

    private int classNumber = 0;

    public void getTask(String Function, double start, double end, Current current) {
        System.out.println("Task received: " + Function + " " + start + " " + end);

        try {
            CtClass ctClass = createClassWithMethod(Function);
            double result = ApplyFunction(ctClass);
            System.out.println("Result: " + result);
            classNumber++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double ApplyFunction(CtClass ctClass) throws Exception {
        Class<?> clazz = ctClass.toClass();
        Object obj = clazz.getDeclaredConstructor().newInstance();
        java.lang.reflect.Method method = clazz.getMethod("apply", double.class);
        // Use method for monte carlo integration
        
        double result = (double) method.invoke(obj, 1);
        ctClass.detach();
        return result;
    }

    private CtClass createClassWithMethod(String Function) throws CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().makeClass("MyClass" + classNumber);
        ctClass.addMethod(CtMethod.make("public double apply(double x) { return " + Function + " ; }", ctClass));
        return ctClass;
    }

    public void connectionResponse(String msg, Current current) {
        System.out.println(msg);
    }
}
