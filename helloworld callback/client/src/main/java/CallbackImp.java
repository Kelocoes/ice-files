import com.zeroc.Ice.Current;

import Demo.Response;

public class CallbackImp implements Demo.Callback{

    @Override
    public void callbackClient(Response response, Current current) {
        System.out.println("Respuesta del server: " + response.value + ", " + response.responseTime);
        System.out.println("Callback invoke");
    }

}