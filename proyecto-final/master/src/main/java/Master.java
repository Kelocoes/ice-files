import Demo.MasterPrx;

import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;

public class Master {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "config.master", extraArgs)) {
            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                for (String v : extraArgs) {
                    System.out.println(v);
                }
            }

            ObjectAdapter adapter = communicator.createObjectAdapter("Master");
            MasterImp masterImp = new MasterImp();
            adapter.add(masterImp, Util.stringToIdentity("MasterObject"));
            adapter.activate();

            masterImp.readCommandLine();

            communicator.waitForShutdown();
        }
    }

}