package pl.blackwaterapi.store;

public interface Callback<T>
{
    T done(T p0);
    
    void error(Throwable p0);
}
