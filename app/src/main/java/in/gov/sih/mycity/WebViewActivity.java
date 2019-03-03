package in.gov.sih.mycity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

        private String url;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_web_view);

                url = getIntent().getStringExtra("url");

                WebView webView = (WebView) findViewById(R.id.webview);
                webView.loadUrl(url);
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
        }
}
