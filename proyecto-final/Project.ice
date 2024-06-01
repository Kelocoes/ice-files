module Demo
{
    interface Worker
    {
        void getTask(string Function, double start, double end, int cantNum);

        void connectionResponse(string msg);
    }

// Se usa asterizco porque no se refiere a Callback sino al proxy de Callback creado por ice
    interface Master
    {
        Master* connectToMaster(string s, Worker* worker);

        void addPartialResult(double partialResult, long time);
    }

}
