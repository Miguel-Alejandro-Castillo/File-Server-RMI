package ar.com.file_server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
public class StartRemoteObject
{
    public static void main (String args[])
    {
        try{
            System.out.println(" Registry.REGISTRY_PORT " + Registry.REGISTRY_PORT);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            FileServerRemote robject = new FileServerRemote();
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/file_server";
            Naming.rebind(rname, robject);
        }catch (Exception e) {
            System.out.println("Hey, an error occurred at Naming.rebind");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
