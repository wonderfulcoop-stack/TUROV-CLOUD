package cloud.turov.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends Activity {
    private static final String APP_URL = "http://cloud-turov.duckdns.org/"; // поменяй на свой домен, если другой
    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout offlineView;
    private ValueCallback<Uri[]> filePathCallback;
    private static final int FILE_CHOOSER_REQUEST = 1001;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);

        webView = new WebView(this);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);

        offlineView = buildOfflineView();
        offlineView.setVisibility(View.GONE);

        root.addView(webView, new FrameLayout.LayoutParams(-1, -1));
        root.addView(progressBar, new FrameLayout.LayoutParams(-1, 8, Gravity.TOP));
        root.addView(offlineView, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (isInternal(url) || isPayment(url)) return false;
                openExternal(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                offlineView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                if (request.isForMainFrame()) showOffline();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> callback, FileChooserParams params) {
                filePathCallback = callback;
                Intent intent = params.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST);
                } catch (Exception e) {
                    filePathCallback = null;
                    return false;
                }
                return true;
            }
        });

        webView.loadUrl(APP_URL);
    }

    private boolean isInternal(String url) {
        try {
            return Uri.parse(url).getHost().equals(Uri.parse(APP_URL).getHost());
        } catch (Exception e) { return false; }
    }

    private boolean isPayment(String url) {
        return url.contains("yoomoney.ru") || url.contains("yandex.ru");
    }

    private void openExternal(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void showOffline() {
        offlineView.setVisibility(View.VISIBLE);
    }

    private LinearLayout buildOfflineView() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(48, 48, 48, 48);
        box.setBackgroundColor(Color.rgb(5,5,5));

        TextView title = new TextView(this);
        title.setText("Нет соединения");
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);

        TextView text = new TextView(this);
        text.setText("Turov Cloud не открылся. Проверь интернет или доступность сайта.");
        text.setTextColor(Color.rgb(185,185,185));
        text.setTextSize(15);
        text.setGravity(Gravity.CENTER);
        text.setPadding(0, 18, 0, 28);

        Button reload = new Button(this);
        reload.setText("Повторить");
        reload.setOnClickListener(v -> {
            offlineView.setVisibility(View.GONE);
            webView.loadUrl(APP_URL);
        });

        box.addView(title, new LinearLayout.LayoutParams(-1, -2));
        box.addView(text, new LinearLayout.LayoutParams(-1, -2));
        box.addView(reload, new LinearLayout.LayoutParams(-1, -2));
        return box;
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST && filePathCallback != null) {
            Uri[] result = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            filePathCallback.onReceiveValue(result);
            filePathCallback = null;
        }
    }
}
