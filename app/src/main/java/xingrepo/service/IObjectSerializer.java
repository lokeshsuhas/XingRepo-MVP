package xingrepo.service;

/**
 * Created by Lokesh on 22-02-2016.
 */
public interface IObjectSerializer {
    byte[] serialize(Object instance);

    <T> T deserialize(String data, Class<?> clazz);
}