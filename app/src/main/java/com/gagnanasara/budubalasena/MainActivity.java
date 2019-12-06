package com.gagnanasara.budubalasena;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class MainActivity extends AppCompatActivity{

    public static SharedPreferences preferences;

    private TextView mTextMessage;

    Fragment tabOne = null;
    Fragment tabTwo = null;
    Fragment tabThree = null;

    private static final String TAG = "MainActivity";


    private MenuItem about_bbs;
    private MenuItem delete_notifications;
    private MenuItem clear_fields;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedTab = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedTab = tabOne;
                    showOption(about_bbs.getItemId());
                    break;
                    //mTextMessage.setText(R.string.title_home);
                    //return true;
                case R.id.navigation_contact_us:
                    selectedTab = tabTwo;
                    showOption(clear_fields.getItemId());
                    break;
                    //mTextMessage.setText(R.string.title_dashboard);
                    //return true;
                case R.id.navigation_notifications:
                    showOption(delete_notifications.getItemId());
                    selectedTab = tabThree;
                    break;
                    //mTextMessage.setText(R.string.title_notifications);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.tab_view,selectedTab).commit();

            return true;
        }
    };

    private void showOption(int  itemId){

        if(itemId == about_bbs.getItemId()){
            about_bbs.setVisible(true);
            delete_notifications.setVisible(false);
            clear_fields.setVisible(false);
        }
        else if(itemId == delete_notifications.getItemId()){
            about_bbs.setVisible(false);
            delete_notifications.setVisible(true);
            clear_fields.setVisible(false);
        }
        else if(itemId == clear_fields.getItemId()){
            about_bbs.setVisible(false);
            delete_notifications.setVisible(false);
            clear_fields.setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.bbs_options, menu);

        about_bbs = menu.findItem(R.id.about_bbs).setVisible(false);;
        delete_notifications = menu.findItem(R.id.delete_notifications);
        clear_fields = menu.findItem(R.id.clear_fields);

        about_bbs.setVisible(true);
        delete_notifications.setVisible(false);
        clear_fields.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_bbs:
                new AlertDialog.Builder(this)
                        .setTitle("About BBS")
                        .setMessage("Bodu Bala Sena, is a Sinhalese Buddhist nationalist organisation based in Colombo, Sri Lanka. Key person in the organisation is Venerable Galagoda Aththe Gnanasara Thero.\n\nAddress: No 615, Nawala Rd,Rajagiriya.\nemail: info@bodubalasena.org\nCall Us: (whatsapp, Viber, Imo):\n+94 76 688 0030\n+94 77 321 8677")
                        .setPositiveButton("OK", null)
                        .show();
                break;

            case R.id.delete_notifications:

                new AlertDialog.Builder(this)
                        .setTitle("Delete Notifications")
                        .setMessage("All notifications will be permanently deleted. Do you wish to continue?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllNotifications();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;

            case R.id.clear_fields:

                new AlertDialog.Builder(this)
                        .setTitle("Clear Input")
                        .setMessage("All input fields will be cleared. Do you wish to continue?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)findViewById(R.id.text_name)).setText("");
                                ((EditText)findViewById(R.id.text_telephone)).setText("");
                                ((EditText)findViewById(R.id.text_email)).setText("");
                                ((EditText)findViewById(R.id.text_message)).setText("");
                                ((Button)findViewById(R.id.imageButton)).setText(getString(R.string.attach_a_photo));
                                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(null);
                                (findViewById(R.id.text_name)).requestFocus();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;

            default:
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    private void deleteAllNotifications() {

        preferences.edit().clear().commit();

        Intent intent = new Intent("notification-count");
        intent.putExtra("n-count", 0);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        setTitle(R.string.app_bar_name);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tabOne =  new TabOne(this);
        tabTwo = new TabTwo(this);
        tabThree = new TabThree(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.tab_view,tabOne).commit();

        subscribeToTopic();

        //mWebView = findViewById(R.id.web_view);

        //WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        //mWebView.loadUrl("http://www.google.com");

        //YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        //getLifecycle().addObserver(youTubePlayerView);

        /*
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });
        */


        //PreferenceHelper.setValue(getString(R.string.notification_count), "2");

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MainActivity: ", "Key: " + key + " Value: " + value);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //mWebView.loadUrl("http://www.bodubalasena.org/");
        }catch (Exception e){
            System.out.println("ERRRRROR: "+e);
        }

        //subscribeToTopic();
    /*
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
                */
    }

    private void subscribeToTopic(){
        Log.d(TAG, "Subscribing to weather topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        //Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        //toast.show();
                    }
                });
        // [END subscribe_topics]
    }


}
