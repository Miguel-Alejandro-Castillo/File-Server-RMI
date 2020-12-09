package ar.com.file_server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.Map;

public class ClientFileServer
{
    private static final String PATH_FILES = "/files_client/";

    private static String getPathname(String filename) {
        return System.getProperty("user.dir") + PATH_FILES + filename;
    }

    private static void write(String filename, int cant, byte []buffer) throws FileNotFoundException, IOException{
        FileOutputStream fileOutput = new FileOutputStream(getPathname(filename));
        fileOutput.write(buffer,0, cant);
        fileOutput.close();
    }

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("1 argument needed: (remote) hostname filename");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/file_server";
            IFileServerRemote remote = (IFileServerRemote) Naming.lookup(rname);
            String filename = args[1];
            int size = remote.size(filename);
            if(size > -1) {
                Map<String, Object> result = remote.read(filename,0, size);
                int bytesRead = (int) result.get("bytesRead");
                byte[] buffer = (byte[])result.get("buffer");
                if(bytesRead == size){
                    System.out.println("Se leyo todo el archivo");
                    write(filename, size, buffer);
                    int cantBytesWrite = remote.write(filename.replaceFirst("\\.","(1)."), size, buffer);
                }
                else
                    System.out.println("La lectura del archivo es incompleta");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}