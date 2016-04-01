package es.rufflecol.lara.shopproductsorteronlinejson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.rufflecol.lara.shopproductsorteronlinejson.model.Product;
import es.rufflecol.lara.shopproductsorteronlinejson.model.ShopFeed;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private boolean jsonDownloaded = false;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToolbarLRC toolbar = (ToolbarLRC) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DownloadJsonTask downloadJsonTask = new DownloadJsonTask();
        downloadJsonTask.execute();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ShopFeed shopFeed = readShopFeedFromFile();
        String currencySymbol = shopFeed.getCurrencySymbol();
        List<Product> products = shopFeed.getProducts();
        adapter = new RecyclerAdapter(currencySymbol, products, getResources());
        recyclerView.setAdapter(adapter);
    }

    private class DownloadJsonTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                URL url = new URL("http://lara.rufflecol.es/shopproductfeed.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                text = readStream(urlConnection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (text != null) {
                writeStreamToFile(text);
            } else {
                return false;
            }

            File directory = getFilesDir();
            File file = new File(directory, "shopproductfeed.json");
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean wasSuccessful) {
            if (wasSuccessful) {
                jsonDownloaded = true;
//                startListActivityIfJsonDownloadedAndTimerCompleted();
            } else {
                Toast.makeText(MainActivity.this, "Error reading data", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String readStream(InputStream inputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    private void writeStreamToFile(String data) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("shopproductfeed.json", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException exception) {
            Log.e("Exception", "File write failed: " + exception.toString());
        }
    }

    private ShopFeed readShopFeedFromFile() {
        String json = readJsonFromFile();
        Gson gson = new Gson();
        ShopFeed shopFeed = gson.fromJson(json, ShopFeed.class);
        return shopFeed;
    }

    private String readJsonFromFile() {
        String returnValue = "";

        try {
            InputStream inputStream = openFileInput("shopproductfeed.json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                returnValue = stringBuilder.toString();
            }
        } catch (FileNotFoundException exception) {
            Log.e("Login activity", "File not found: " + exception.toString());
        } catch (IOException exception) {
            Log.e("Login activity", "Cannot read file: " + exception.toString());
        }
        return returnValue;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_alphabetically_az:
                adapter.sortProductsAlphabeticallyAZ();
                return true;
            case R.id.action_sort_alphabetically_za:
                adapter.sortProductsAlphabeticallyZA();
                return true;
            case R.id.action_sort_ascending_order_price:
                adapter.sortProductsAscendingOrderPrice();
                return true;
            case R.id.action_sort_descending_order_price:
                adapter.sortProductsDescendingOrderPrice();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}