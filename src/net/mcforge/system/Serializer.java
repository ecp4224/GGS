package net.mcforge.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

public class Serializer<E> {
    
    private SaveType type;
    
    private static final Kryo LOADER = new Kryo();
    private static final Gson GSON = new Gson();
    

    /**
     * Get the {@link Kryo} object that loads/saves objects that use {@link SaveType#BYTE} save type.
     * @return
     *        The loader/saver
     */
    public static Kryo getKryo() {
        return LOADER;
    }
    
    /**
     * Get the {@link Gson} object that loads/saves objects that use {@link SaveType#JSON} save type.
     * @return
     *        The loader/saver
     */
    public static Gson getGson() {
        return GSON;
    }
    
    public Serializer(SaveType type) {
        this.type = type;
    }
    
    /**
     * Get the current save type to save in.
     * @return
     *        The current save type.
     */
    public SaveType getSaveType() {
        return type;
    }
    
    /**
     * Save the object to the given {@link OuputStream}.
     * The style this object is saved is determined by the method {@link Serializer#getSaveType()}.
     * @param object
     *              The object to save
     * @param out
     *           The {@link OutputStream} to write to.
     * @return
     *        Whether the save was successful or not.
     */
    public boolean saveObject(E object, OutputStream out) {
        try {
            if (type == SaveType.BYTE)
                saveByteStyle(out, object);
            else if (type == SaveType.JSON)
                saveJsonStyle(out, object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get the object from the {@link InputStream} provided.
     * If the current save style is {@link SaveType#JSON}, then please use the method {@link Serializer#getObject(InputStream, Class)}
     * otherwise an exception will be thrown.
     * @param in
     *          The {@link InputStream} to read from.
     * @return
     *        The object read from the stream
     * @throws IOException
     *                    This exception can be thrown for 3 reason: </br>
     *                    1. If there was an error reading from the stream </br>
     *                    2. The current save style is json (<b> Please use {@link Serializer#getObject(InputStream, Class)} to get json objects</b>) </br>
     *                    3. The current save type is invalid </br>
     */
    public E getObject(InputStream in) throws IOException {
        if (type == SaveType.BYTE)
            return getByteStyle(in);
        else if (type == SaveType.JSON)
            throw new IOException("JSON Saving requires a class parameter. Try calling Serializer.getObject(InputStream, Class<E>)");
        else
            throw new IOException("Invalid save type!");
    }
    
    /**
     * Get the object from the {@link InputStream} provided.
     * @param in
     *          The {@link InputStream} to read from.
     * @param class_
     *          The class for this object
     * @return
     *        The object read from the stream
     * @throws IOException
     *                    This exception can be thrown for 2 reason: </br>
     *                    1. If there was an error reading from the stream </br>
     *                    2. The current save type is invalid </br>
     */
    public E getObject(InputStream in, Class<E> class_) throws IOException {
        if (type == SaveType.BYTE)
            return getByteStyle(in);
        else if (type == SaveType.JSON)
            return getJsonStyle(in, class_);
        else
            throw new IOException("Invalid save type!");
    }
    
    @SuppressWarnings("unchecked")
    private E getByteStyle(InputStream in) throws IOException {
        Input objInput = new Input(in);
        long version = objInput.readLong();
        Class<?> class_ = LOADER.readObject(objInput, Class.class);
        E l = null;
        if (version == getVersion(l)){
            l = (E)LOADER.readObject(objInput, class_);
            return l;
        }
        else {
            objInput.close();
            throw new IOException("Invalid class version. Expected version - " + getVersion(l) + ". Version got - " + version);
        }
    }
    
    private void saveByteStyle(OutputStream out, E object) throws IOException {
        Output kout = new Output(out);
        long version = getVersion(object);
        Class<?> class_ = object.getClass();
        kout.writeLong(version);
        LOADER.writeObject(kout, class_);
        LOADER.writeObject(kout, object);
        kout.close();
    }
    
    private void saveJsonStyle(OutputStream out, E object) throws IOException {
        PrintStream pout = new PrintStream(out);
        String objects = GSON.toJson(object);
        pout.println(objects);
        pout.close();
    }
    
    private E getJsonStyle(InputStream in, Class<E> class_) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String json = reader.readLine();
        return GSON.fromJson(json, class_);
    }
    
    private long getVersion(E object) {
        try {
            Field f = object.getClass().getDeclaredField("serialVersionUID");
            return (long)f.getLong(object);
        } catch (Exception e) {
            return 0L;
        }
    }
    
    
    public enum SaveType {
        /**
         * Serialize the object using {@link Kryo}.
         * This option is good for saving flat files.
         */
        BYTE("byte"),
        
        /**
         * Save the object as a Json String.
         * This option will create a PrintWriter when saving and write to the PrintWriter.
         * You could use this for web API's that require json.
         * Example:
         * <pre>
         * {@code
           URL url = new URL("https://api.mywebsite.com/);
 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
 connection.setDoOutput(true);
 connection.setRequestMethod("POST");
 serializer.saveObject(MyObject, connection.getOutputStream()); //This will write the object in json format to the site.
           }
           </pre>
         */
        JSON("json");
        
        String type;
        SaveType(String type) { this.type = type; }
        public String getType() {
            return type;
        }
        
        public static SaveType fromString(String string) {
            string = string.toLowerCase();
            
            if (string.equals("byte") || string.equals("bytes"))
                return SaveType.BYTE;
            else if (string.equals("json"))
                return SaveType.JSON;
            else
                return SaveType.BYTE;
        }
    }

}
