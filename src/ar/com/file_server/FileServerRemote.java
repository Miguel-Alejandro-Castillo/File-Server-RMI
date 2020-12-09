package ar.com.file_server;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class FileServerRemote extends UnicastRemoteObject implements IFileServerRemote {

    private static final String PATH_FILES = "/files_server/";

    protected FileServerRemote() throws RemoteException {
        super();
    }

    private String getPathname(String name) {
        return System.getProperty("user.dir")  + PATH_FILES + name;
    }

    public int size(String name) throws RemoteException {
        String msg;
        int size = -1;
        try {
            File file = new File(this.getPathname(name));
            if(file.exists() && file.isFile())
                size = (int)file.length();
        }
        catch (SecurityException e1) {
            msg = "El usuario no tiene permisos de lectura sobre el archivo " + name;
            throw  new RemoteException(msg, e1);
        } /*
        catch (Exception e2) {
            System.out.println("El archivo " + name + " no existe");
            e2.printStackTrace();
        } */
        return size;

    }

    public Map<String, Object> read(String name, int position, int cant) throws RemoteException {
        String msg;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("bytesRead", -1);
        result.put("buffer", null);

        FileInputStream fileInput = null;
        try{
            fileInput = new FileInputStream(this.getPathname(name));

            byte[] buffer = new byte[cant];
            fileInput.skip(position);
            int bytesRead = fileInput.read(buffer, 0, cant);
            result.put("bytesRead", bytesRead);
            result.put("buffer", buffer);
        }
        catch(FileNotFoundException e1){
            msg = "El archivo " + name + " no existe";
            throw new RemoteException(msg, e1);
        }
        catch(SecurityException e2){
            msg = "El usuario no tiene permisos de lectura sobre el archivo " + name;
            throw new RemoteException(msg, e2);
        }
        catch(IOException e3){
            msg = "Ocurrio un error de I/O";
            throw new RemoteException(msg, e3);
        }
        /*
        catch(Exception e4){
            e4.printStackTrace();;
        }
        */
        finally{
            this.close(fileInput);
        }
        return result;
    }

    public int write(String name, int cant, byte []buffer) throws RemoteException {
        String msg;
        FileOutputStream fileOutput = null;
        int result = -1;
        try{
            fileOutput = new FileOutputStream(this.getPathname(name),true);
            fileOutput.write(buffer,0, cant);
            result = cant;
        }
        catch(FileNotFoundException e1){
            msg = "El archivo " + name + " no pudo ser creado";
            throw new RemoteException(msg, e1);
        }
        catch(SecurityException e2){
            msg = "El usuario no tiene permisos de escritura sobre el archivo " + name;
            throw new RemoteException(msg, e2);
        }
        catch(IOException e3){
            msg = "Ocurrio un error de I/O";
            throw new RemoteException(msg, e3);
        }
        /*
        catch(Exception e4){
            e4.printStackTrace();
        }
        */
        finally{
            this.close(fileOutput);
        }
        return result;
    }

    private void close(Closeable file) throws RemoteException {
        if( file != null) {
            try {
                file.close();
            } catch (IOException e) {
                String msg = "Ocurrio un error de I/O";
                throw new RemoteException(msg, e);
            }
        }
    }

}
