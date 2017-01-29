package v6.caique;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity Instance;
    //public static String ButtonTag;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Instance != null) {
            Instance.finish();
        }

        Instance = this;
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("420728598029-dt0td1a1a40javb5knfggd0m5crag15d.apps.googleusercontent.com")
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //Close app
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_chats);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //ArrayList<String> Test = new ArrayList<String>();
        //CreateChatList(Test);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.d("", "Sending registration");

                try {
                    String token = acct.getIdToken();

                    if (token != null) {
                        FirebaseMessaging fm = FirebaseMessaging.getInstance();
                        fm.send(new RemoteMessage.Builder(getString(R.string.gcm_defaultSenderId) + "@gcm.googleapis.com")
                                .setMessageId(Integer.toString(FirebaseIDService.msgId.incrementAndGet()))
                                .addData("type", "reg")
                                .addData("text", token)
                                .build());

                        return;
                    }
                }
                catch (NullPointerException Ex)
                {
                    return;
                }
            }

            Log.d("", "Failed sign in");
            this.finish();
        }
    }

    /*public void updateText(String Text) {
        if (Text != null) {
            TextView Layout = (TextView) findViewById(R.id.hello_world);
            Layout.setText(Text);
            Log.d("UpdateText", Text);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        RelativeLayout ContentMain = (RelativeLayout) findViewById(R.id.maincontent);

        if (id == R.id.nav_chats) {
            ContentMain.setVisibility(View.VISIBLE); //REPLACE
        } else if (id == R.id.nav_explore) {
            ContentMain.setVisibility(View.INVISIBLE); //REPLACE
        } else if (id == R.id.nav_favorites) {
            ContentMain.setVisibility(View.INVISIBLE); //REPLACE
        } else if (id == R.id.nav_invite_people) {

            Intent newActivity = new Intent(this, PictureActivity.class);
            startActivity(newActivity);

        } else if (id == R.id.nav_music_library) {

            Intent newActivity = new Intent(this, PictureActivity.class);
            startActivity(newActivity);

        } else if (id == R.id.nav_settings) {

            Intent newActivity = new Intent(this, PictureActivity.class);
            startActivity(newActivity);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CreateChat(View view) {
        Intent newActivity = new Intent(this, SendServerMessage.class);
        startActivity(newActivity);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void GetChatNames(final ArrayList<String> Chat){

        for(int i = 0; i < Chat.size(); i++){
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Query MessageData = mDatabase.child("chat").child(Chat.get(i)).child("data").child("title");

            final int finalI = i;
            MessageData.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CreateChatList((String) dataSnapshot.getValue(), Chat.get(finalI));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        /*Button ChatButton = new Button(this);
        ChatButton.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ChatButton.setText("Test Chat");

        ChatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent newChatActivity = new Intent(MainActivity.Instance, ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("chat", "-KSqbu0zMurmthzBE7GF");
                newChatActivity.putExtras(b);
                startActivity(newChatActivity);
            }
        });

        ChatList.addView(ChatButton);*/
    }

    private void CreateChatList(final String Chat, final String ChatID){
        LinearLayout ChatList = (LinearLayout) findViewById(R.id.ChatList);

            Button ChatButton = new Button(this);
            ChatButton.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            ChatButton.setText(Chat);

            ChatButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent newChatActivity = new Intent(MainActivity.Instance, ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putString("chat", ChatID);
                    newChatActivity.putExtras(b);
                    startActivity(newChatActivity);
                }
            });

            ChatList.addView(ChatButton);
        }
    }

