import com.zeroc.Ice.Current;

import Demo.Response;

public class CallbackImp implements Demo.Callback {

    @Override
    public void callbackClient(Response response, Current current) {
        System.out.println("Server response: " + response.value + " Response Time: " + response.time + " ms");
    }

}
