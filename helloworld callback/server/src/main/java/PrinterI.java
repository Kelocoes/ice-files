// import java.util.concurrent.Semaphore;

// import com.zeroc.IceInternal.ThreadPool;

import Demo.Response;

public class PrinterI implements Demo.Printer
{
    // ThreadPool ca;
    // Semaphore s = new Semaphore(2)

    public synchronized void printString(String s,Demo.CallbackPrx callback, com.zeroc.Ice.Current current)
    {
        /*
            synchronized(objs){};
            ca.execute(null);
        */
        new Thread(()->{
            try{
                System.out.println(s);
                Thread.sleep(5000);
                callback.callbackClient(new Response(0, "Server response: " + s));

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        ).start();
    }
}