import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import com.zeroc.Ice.Util;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;

import Demo.MasterPrx;
import Demo.WorkerPrx;

public class Worker {
    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "config.worker", extraArgs)) {
            MasterPrx service = MasterPrx.checkedCast(communicator.propertyToProxy("Master.Proxy"));

            if (service == null) {
                throw new Error("Invalid proxy");
            }

            String user = System.getProperty("user.name");
            String hostname = "";

            try {
                hostname = execReadToString("hostname").trim();
            } catch (IOException e) {
                hostname = "null";
            }

            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Callback", "tcp -p " + args[0] + " -h " + args[1]);
            WorkerImp workerImp = new WorkerImp();
            ObjectPrx obprx = adapter.add(workerImp, Util.stringToIdentity("Worker"));
            adapter.activate();

            WorkerPrx prx = WorkerPrx.uncheckedCast(obprx);
            MasterPrx masterPrx = service.connectToMaster(user + ":" + hostname, prx);
            workerImp.setMasterPrx(masterPrx);

            communicator.waitForShutdown();
        }
    }

    public static String execReadToString(String execCommand) throws IOException {
        Process proc = Runtime.getRuntime().exec(execCommand);
        try (InputStream stream = proc.getInputStream()) {
            try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                return s.hasNext() ? s.next() : "";
            }
        }
    }
}
