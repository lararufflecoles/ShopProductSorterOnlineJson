package es.rufflecol.lara.shopproductsorteronlinejson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.rufflecol.lara.shopproductsorteronlinejson.model.Product;
import es.rufflecol.lara.shopproductsorteronlinejson.model.ShopFeed;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToolbarLRC toolbar = (ToolbarLRC) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        DownloadJsonTask downloadJsonTask = new DownloadJsonTask();
        downloadJsonTask.execute();
    }

    private class DownloadJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("http://lara.rufflecol.es/shopproductfeed.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                text = readStream(urlConnection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String json) {
            if (!TextUtils.isEmpty(json)) {

                Gson gson = new Gson();
                ShopFeed shopFeed = gson.fromJson(json, ShopFeed.class);
                String currencySymbol = shopFeed.getCurrencySymbol();
                List<Product> products = shopFeed.getProducts();
                recyclerAdapter = new RecyclerAdapter(currencySymbol, products, getResources());
                recyclerView.setAdapter(recyclerAdapter);

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
                recyclerAdapter.sortProductsAlphabeticallyAZ();
                return true;
            case R.id.action_sort_alphabetically_za:
                recyclerAdapter.sortProductsAlphabeticallyZA();
                return true;
            case R.id.action_sort_ascending_order_price:
                recyclerAdapter.sortProductsAscendingOrderPrice();
                return true;
            case R.id.action_sort_descending_order_price:
                recyclerAdapter.sortProductsDescendingOrderPrice();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}