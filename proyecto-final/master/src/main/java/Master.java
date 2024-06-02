import com.zeroc.Ice.Util;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;

import Demo.ManageTaskPrx;

import java.util.ArrayList;
import java.util.List;

public class Master {
    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "config.master", extraArgs)) {
            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                for (String v : extraArgs) {
                    System.out.println(v);
                }
            }

            ObjectAdapter adapter = communicator.createObjectAdapter("Master");
            ManageTask manageTask = new ManageTask();
            ObjectPrx obprx = adapter.add(manageTask, Util.stringToIdentity("ManageTask"));
            adapter.activate();

            ManageTaskPrx manageTaskPrx = ManageTaskPrx.uncheckedCast(obprx);
            manageTask.setManageTaskPrx(manageTaskPrx);
            CLI cli = new CLI();
            cli.readCommandLine(manageTask);

            communicator.waitForShutdown();
        }
    }

}