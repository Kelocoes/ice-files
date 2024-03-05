import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackPrx;
import Demo.Response;

public class Client
{
    public static void main(String[] args)
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            Response response = null;
            Demo.PrinterPrx service = Demo.PrinterPrx
                    .checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            if(service == null)
            {
                throw new Error("Invalid proxy");
            }
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            CallbackImp callbackImp = new CallbackImp();
            ObjectPrx obprx= adapter.add(callbackImp, Util.stringToIdentity("callbackClient"));
            adapter.activate();

            CallbackPrx prx = CallbackPrx.uncheckedCast(obprx);
            service.printString("Hello World from a remote client!", prx);
            System.out.println("invoked callback");

            communicator.waitForShutdown();
        }
    }
}
