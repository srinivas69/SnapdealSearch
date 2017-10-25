package com.srinivasworks.snapdealsearch;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button searchBt;
    private RecyclerView rlView;
    private TextInputEditText txtInpEt;

    private static String url;

    private MainRecyclerviewAdapter adapter;

    private ProgressBar pBar;

    private List<DataModelClass> dataList = new ArrayList<DataModelClass>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //apiService =
        //        SnapdealSearchApiClient.getClient().create(SnapdealSearchApiInterface.class);
        //pBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBt = (Button) findViewById(R.id.searchBt);
        rlView = (RecyclerView) findViewById(R.id.rlView);
        txtInpEt = (TextInputEditText) findViewById(R.id.txtInpEt);
        pBar = (ProgressBar) findViewById(R.id.progressbar);

        fillRecylerView();

        searchBt.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.searchBt) {

            pBar.setVisibility(View.VISIBLE);

            String keyword = txtInpEt.getText().toString();
            Log.i(TAG, " keyword : " + keyword);

            url = CONST.url + keyword + CONST.urlSort;
            Log.i(TAG, " url : " + url);

            searchBt.setEnabled(false);
            new DownloadWebPageTask().execute(new String[]{url});
        }
    }

    public void fillRecylerView() {

        adapter = new MainRecyclerviewAdapter(this, dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        rlView.setLayoutManager(mLayoutManager);
        rlView.setItemAnimator(new DefaultItemAnimator());
        rlView.setHasFixedSize(true);
        rlView.setAdapter(adapter);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, List<DataModelClass>> {

        @Override
        protected List<DataModelClass> doInBackground(String... urls) {
            List<DataModelClass> dataListThis = new ArrayList<DataModelClass>();
            OkHttpClient client = new OkHttpClient();
            Request request =
                    new Request.Builder()
                            .url(urls[0])
                            .build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBodyStr = response.body().string();
                    Document document = Jsoup.parse(responseBodyStr);
                    //Elements bodyEl = document.select("body");
                    Element content = document.getElementById("content_wrapper");
                    Elements contentChildEle = content.getElementsByClass("col-xs-24 reset-padding marT22");
                    //Elements contentChildEle2 = contentChildEle.select("div");
                    for (Element ele : contentChildEle) {

                        Elements contentChildEle2 = ele.children();
                        for (Element ele2 : contentChildEle2) {

                            String divClassName2 = ele2.className();
                            if (divClassName2.equalsIgnoreCase("col-xs-19 reset-padding")) {
                                Log.i(TAG, "ele2 class name : " + ele2.className());
                                Elements contentChildEle3 = ele2.children();
                                //Log.i(TAG, "contentChildEle3 html : "+contentChildEle3.html());

                                for (Element ele3 : contentChildEle3) {

                                    String divClassName3 = ele3.className();
                                    if (divClassName3.equalsIgnoreCase("comp comp-right-wrapper ref-freeze-reference-point clear")) {

                                        Log.i(TAG, "ele3 class name : " + divClassName3);
                                        Elements contentChildEle4 = ele3.children();
                                        for (Element ele4 : contentChildEle4) {

                                            String eleId = ele4.id();
                                            Log.i(TAG, "eleId : " + eleId);

                                            if (eleId.equalsIgnoreCase("Products")) {

                                                Elements contentChildEle5 = ele4.children();
                                                for (Element ele5 : contentChildEle5) {

                                                    String classNameForele5 = ele5.className();
                                                    //Log.i(TAG, "classNameForele5 class name : "+classNameForele5);
                                                    if (classNameForele5.equalsIgnoreCase("js-section clearfix dp-widget")) {

                                                        //Log.i(TAG, "classNameForele5 class name : "+classNameForele5);
                                                        Elements contentChildEle6 = ele5.children();
                                                        for (Element ele6 : contentChildEle6) {

                                                            String classNameForele6 = ele6.className();
                                                            if (classNameForele6.equalsIgnoreCase("col-xs-6  favDp product-tuple-listing js-tuple")) {

                                                                Log.i(TAG, "classNameForele6 class name : " + classNameForele6);
                                                                //class="product-tuple-image "
                                                                Elements contentChildEle7 = ele6.children();
                                                                for (Element ele7 : contentChildEle7) {

                                                                    String classNameForele7 = ele7.className();
                                                                    if (classNameForele7.equalsIgnoreCase("product-tuple-image")) {

                                                                        Log.i(TAG, "classNameForele7 : " + classNameForele7);
                                                                        Element eleImg = ele7.select("a").select("picture").select("img").first();
                                                                        Element eleSource = ele7.select("a").select("picture").select("source").first();

                                                                        String url = eleSource.attr("srcset");
                                                                        String title = eleImg.attr("title");

                                                                        DataModelClass obj = new DataModelClass(title, url);
                                                                        dataListThis.add(obj);
                                                                        Log.i(TAG, "title : " + title + " url : " + url);

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Elements contentChildEle2 = content.getElementsByClass("col-xs-19 reset-padding");
                    //String bodyHtml =  contentChildEle.html();
                    return dataListThis;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataListThis;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataList.clear();
        }

        @Override
        protected void onPostExecute(List<DataModelClass> dataListArg) {
            super.onPostExecute(dataListArg);
            dataList.addAll(dataListArg);
            //Log.i(TAG, "str : " + str);
            pBar.setVisibility(View.INVISIBLE);
            searchBt.setEnabled(true);
            if (dataList.size() != 0) {

                adapter.notifyDataSetChanged();
            } else {

                adapter.notifyDataSetChanged();
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("No Results found for search");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    }
}
