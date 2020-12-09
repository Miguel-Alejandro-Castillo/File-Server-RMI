package ar.com.file_server;
/*
 * IFileServerRemote.java
 * Interface defining only one method which can be invoked remotely
 *
 */
/* Needed for defining remote method/s */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/* This interface will need an implementing class */
public interface IFileServerRemote extends Remote
{
    /*It will be possible to invoke this method from an application in other JVM */
    int size(String name) throws RemoteException;
    Map<String, Object> read(String name, int position, int cant) throws RemoteException;
    int write(String name, int cant, byte []buffer) throws RemoteException;
}
