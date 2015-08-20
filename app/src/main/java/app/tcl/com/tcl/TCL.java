package app.tcl.com.tcl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TCL extends AppCompatActivity {

    public String _htmlCode = "";

    //Lists
    public List<String> _lstTitles = new ArrayList<>();
    public List<String> _lstImages = new ArrayList<>();

    //Main Image
    public Bitmap _MainIMG;

    //Universal Counters
    public int _indexToShow = 0;
    public int _pageToLoad = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcl);

        //GetHML
        WebData _AsyncObject = new WebData();
        _AsyncObject.execute("");

        //Assing Buttons Actions
        Button _nextButton = (Button) findViewById(R.id.btnNext);
        _nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _indexToShow++;
                SetTitleAndImage(_indexToShow);
            }
        });

        Button _prevButton = (Button) findViewById(R.id.btnPrev);
        _prevButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _indexToShow--;
                SetTitleAndImage(_indexToShow);
            }
        });
    }

    public void SetTitleAndImage(int _index)
    {
        if (_index < 0)
        {
            _indexToShow = 0;
            return;
        }
        if (_index >= _lstTitles.size())
        {
            //Update List
            WebDataRefresh _refreshData = new WebDataRefresh();
            _refreshData.execute("");

            return;
        }
        //Set Title
        TextView _txtTitle = (TextView) findViewById(R.id.txtTitle);
        _txtTitle.setText(_lstTitles.get(_index));
        //Set Image
        WebView _imgMain = (WebView) findViewById(R.id.webView);
        _imgMain.loadUrl(_lstImages.get(_index));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tcl, menu);
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

        private class WebDataRefresh extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String _status = "Error";
                try
                {
                    HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
                    HttpGet httpget = new HttpGet("http://thecodinglove.com/page/" + _pageToLoad); // Set the action you want to do
                    HttpResponse response = httpclient.execute(httpget); // Executeit
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent(); // Create an InputStream with the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) // Read line by line
                        sb.append(line + "\n");

                    _htmlCode = sb.toString(); // Result is here

                    is.close(); // Close the stream
                    _status = "OK";
                }
                catch (IOException e)
                {
                    _status = "Error";
                }

                return _status;
            }

            protected void onPostExecute(String result)
            {
                List<String> _tempTitles;
                List<String> _tempImages;

                _tempTitles = WebReader.GetTitles(_htmlCode);
                _tempImages = WebReader.GetImages(_htmlCode);

                _lstTitles.addAll(_tempTitles);
                _lstImages.addAll(_tempImages);

                SetTitleAndImage(_indexToShow);
                _pageToLoad++;
            }
        }

        private class WebData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String _status = "Error";
            try
            {
                HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
                HttpGet httpget = new HttpGet("http://thecodinglove.com"); // Set the action you want to do
                HttpResponse response = httpclient.execute(httpget); // Executeit
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent(); // Create an InputStream with the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) // Read line by line
                    sb.append(line + "\n");

                _htmlCode = sb.toString(); // Result is here

                is.close(); // Close the stream
                _status = "OK";
            }
            catch (IOException e)
            {
                _status = "Error";
            }

            return _status;
        }

        @Override
        protected void onPostExecute(String result) {

            _lstTitles = WebReader.GetTitles(_htmlCode);
            _lstImages = WebReader.GetImages(_htmlCode);

            SetTitleAndImage(0);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
