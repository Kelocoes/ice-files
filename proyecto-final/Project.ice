module Demo
{
    interface Worker {
        void getTask(string Function, double start, double end, int cantNum);
        void connectionResponse(string msg);
    }

    interface ManageTask {
        ManageTask* connectWorker(string s, Worker* worker);
        void addPartialResult(double partialResult, long time);
    }
}
