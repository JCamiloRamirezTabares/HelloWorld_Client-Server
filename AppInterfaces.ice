module AppInterfaces
{
    interface Requester
    {
        void printString(string s);
    }

    interface Receiver
    {
        string printString(Requester* requester, string s);
    }
}