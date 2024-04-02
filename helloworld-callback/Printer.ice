module Demo
{
    class Response{
        string value;
        long time;
    }

    interface Callback{
        void callbackClient(Response res);
    }

// Se usa asterizco porque no se refiere a Callback sino al proxy de Callback creado por ice
    interface Printer
    {
        void printString(string s, Callback* callback);

        void subscribe(string s, Callback* callback);
    }

}
