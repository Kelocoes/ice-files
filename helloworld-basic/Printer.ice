module Demo
{
    class Response{
        string value;
    }

    interface Printer
    {
        Response printString(string s);
    }
}