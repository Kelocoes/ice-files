module Demo
{
    interface Worker
    {
        void getTask(string Function, double start, double end);

        void connectionResponse(string msg);
    }

// Se usa asterizco porque no se refiere a Callback sino al proxy de Callback creado por ice

    interface Master
    {
        void connectToMaster(string s, Worker* worker);
    }

}
