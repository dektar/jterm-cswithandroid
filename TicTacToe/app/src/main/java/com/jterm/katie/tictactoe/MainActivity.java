package com.jterm.katie.tictactoe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private String mPhotoUrl;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private GoogleApiClient mGoogleApiClient;
    private GameView mGameView;
    private DatabaseReference mUserDb;
    private DatabaseReference mGameDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = (GameView) findViewById(R.id.board);
        mGameView.setCellTouchListener(new GameView.CellTouchListener() {
            @Override
            public void onCellTouched(int x, int y) {
                Log.d(TAG, "Cell touched at x:" + x + " and y:" + y);
                processCellTouch(x, y);
            }
        });

        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            // TODO: The user could pick a username if they don't have one already?
            mUsername = mFirebaseUser.getEmail().split("@")[0];
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .build();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.equals(mUsername, ANONYMOUS)) {
            mUserDb = mFirebaseDatabaseReference.child("users").child(mUsername);
        }
        // TODO: Pick the game better.
        mGameDatabaseReference = mFirebaseDatabaseReference.child("games").child("-BCDEFGH");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: Update this DB on a win.
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    // We need to add one!
                    mUserDb.setValue(new User(mUsername, mFirebaseAuth.getCurrentUser().getEmail()));
                } else {
                    ((TextView) findViewById(R.id.username)).setText(user.getUsername() + " (" +
                            user.getWins() + " wins)");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGameDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());
                Game g1 = dataSnapshot.getValue(Game.class);
                mGameView.updateGame(g1);
                Log.d(TAG, "now what?");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO: remove event listener
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // Sign out of Firebase
                mFirebaseAuth.signOut();

                // Also forget the user -- sign all the way out. Then if they sign in again, the
                // account picker is shown again.
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void processCellTouch(int x, int y) {
        if (mGameView.getGame().getResult() != Game.NO_PLAYER) {
            Toast.makeText(this, getResources().getString(R.string.error_game_over),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mGameView.getGame().isUserTurn(mUsername)) {
            Toast.makeText(this, getResources().getString(R.string.error_not_your_turn),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGameView.getGame().getBoard().get(x).get(y) != Game.NO_PLAYER) {
            Toast.makeText(this, getResources().getString(R.string.error_spot_taken),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mGameView.getGame().doTurn(x, y, mUsername);
        if (mGameView.getGame().getTurn() == Game.NO_PLAYER) {
            // Then the game is over.
            // TODO: Display a message or something about who won
            Toast.makeText(this, getResources().getString(R.string.game_over),
                    Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Display a message that we are waiting for the other player to go... or update
            // the views to show that
        }
        // Update the view in real-time to avoid lag
        mGameView.updateGame(mGameView.getGame());

        // Update the game in the Firebase database
        mGameDatabaseReference.setValue(mGameView.getGame());
    }
}
