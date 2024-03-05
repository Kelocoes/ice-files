module Demo
{
    class Response{
        long responseTime;
        string value;
    }

    interface Callback{
        void callbackClient(Response res);
    }
// Se usa asterizco porque no se refiere a Callback sino al proxy de Callback creado por ice
    interface Printer
    {
        void printString(string s, Callback* callback);
    }

}
