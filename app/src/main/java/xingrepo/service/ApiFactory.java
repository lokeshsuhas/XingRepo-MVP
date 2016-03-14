package xingrepo.service;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import xingrepo.cache.ICacheReader;
import xingrepo.cache.onCacheReadListener;
import xingrepo.cache.onCacheWriteListener;
import xingrepo.service.model.CallResult;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class ApiFactory {
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;

    private IObjectSerializer serializer;
    private boolean isCacheEnabled;
    private onResultListener listener;
    private String url;
    private Gson gson;
    private Class<?> decoderType;
    private onCacheWriteListener cacheResultListener;
    private ICacheReader cacheReader;

    public ApiFactory(Builder builder) {
        this.isCacheEnabled = builder.isCacheEnabled;
        this.url = builder.url;
        this.listener = builder.listener;
        this.cacheResultListener = builder.cacheResultListener;
        this.decoderType = builder.decoderType;
        this.cacheReader = builder.cacheReader;

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls().create();
        serializer = new IObjectSerializer() {
            public byte[] serialize(Object instance) {
                return gson.toJson(instance).getBytes();
            }

            @SuppressWarnings("unchecked")
            public <T> T deserialize(String data, Class<?> clazz) {
                return (T) gson.fromJson(data, clazz);
            }
        };
    }

    /**
     * Making xingrepo.service get call
     */
    public void get() {
        if (url == null) {
            if (listener != null) {
                listener.onError("Url cannot be null");
            }
        }

        if (url.length() == 0) {
            if (listener != null) {
                listener.onError("Url is not formed properly");
            }
        }
        if (isCacheEnabled) {
            cacheReader.call(new onCacheReadListener<String>() {
                @Override
                public void onSuccess(String data) {
                    listener.onSuccess(serializer.deserialize(data, decoderType));
                }

                @Override
                public void onEmpty() {
                    new ApiAsyncTask().execute(null, null, null);
                }

                @Override
                public void onError(String error) {
                    listener.onError(error);
                }
            });
        } else {
            new ApiAsyncTask().execute(null, null, null);
        }
    }

    public static class Builder {

        private boolean isCacheEnabled = true;
        private onResultListener listener = null;
        private Class<?> decoderType;
        private String url = null;
        private onCacheWriteListener cacheResultListener;
        private ICacheReader cacheReader;

        public Builder(Class<?> decoderType) {
            this.decoderType = decoderType;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setReadFromCache(boolean isCacheEnabled) {
            this.isCacheEnabled = isCacheEnabled;
            return this;
        }

        public Builder setListener(onResultListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setCacheWriteListener(onCacheWriteListener listener) {
            this.cacheResultListener = listener;
            return this;
        }

        public Builder setCacheReader(ICacheReader task) {
            this.cacheReader = task;
            return this;
        }

        public ApiFactory build() {
            return new ApiFactory(this);
        }

    }

    //Common server api asynctask
    private class ApiAsyncTask extends AsyncTask<String, String, CallResult> {

        @Override
        protected CallResult doInBackground(String... params) {
            CallResult callResult = new CallResult();
            StringBuffer buffer = new StringBuffer();
            try {
                URL u = new URL(url);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                callResult.IsSuccess = true;
                callResult.Data = buffer.toString();
            } catch (MalformedURLException er) {
                callResult.IsSuccess = false;
                callResult.ErrorMessage = "Url is malformed, please correct it!";
                er.printStackTrace();
            } catch (IOException er) {
                callResult.IsSuccess = false;
                callResult.ErrorMessage = "Url cannot be reached or Api limit has exceeded!";
                er.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException err) {
                    if (listener != null) {
                        listener.onError(err.getMessage());
                    }
                    err.printStackTrace();
                }
            }
            // just call the listener
            if (isCacheEnabled) {
                cacheResultListener.insert(callResult.Data);
            }

            return callResult;
        }

        @Override
        protected void onPostExecute(CallResult result) {
            super.onPostExecute(result);
            if (listener != null) {
                if (result.IsSuccess) {
                    listener.onSuccess(serializer.deserialize(result.Data, decoderType));
                } else {
                    listener.onError(result.ErrorMessage);
                }
            }
        }
    }

}
