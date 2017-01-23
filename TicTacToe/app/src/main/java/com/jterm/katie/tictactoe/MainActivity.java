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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

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
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mGameDatabaseReference;
    private User mUser;
    private ValueEventListener mGameEventListener;
    private ValueEventListener mUserEventListener;

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

        // Set default username to anonymous.
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
            mUsername = mUsername.replace(".", "");
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
            // TODO: This doesn't work if we haven't added the user to the database yet!
            mUserDatabaseReference = mFirebaseDatabaseReference.child("users").child(mUsername);
        }

        mUserEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                if (user == null) {
                    // We need to add one!
                    mUserDatabaseReference.setValue(new User(mUsername,
                            mFirebaseAuth.getCurrentUser().getEmail()));
                    // TODO: Need to show an option to start a new game for this new user
                } else {
                    ((TextView) findViewById(R.id.username)).setText(user.getUsername() + " (" +
                            user.getWins() + " wins)");
                    if (mGameDatabaseReference == null) {
                        initializeGameDatabaseReference(mUser.getGames());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mGameEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {Log.d(TAG, dataSnapshot.toString());
                Game g1 = dataSnapshot.getValue(Game.class);
                mGameView.updateGame(g1);
                if (mGameView.getGame().getTurn() == Game.NO_PLAYER) {
                    doEndGameUi();
                } else if (mGameView.getGame().isUserTurn(mUsername)) {
                    ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                            R.string.your_turn));
                } else {
                    ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                            R.string.waiting_for_player));
                }
                String opponentName =
                        TextUtils.equals(mGameView.getGame().getUsername_p1(), mUsername) ?
                                mGameView.getGame().getUsername_p2() :
                                mGameView.getGame().getUsername_p1();
                ((TextView) findViewById(R.id.opponent_info)).setText(String.format(
                        getResources().getString(R.string.opponent), opponentName));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error: " + databaseError.getMessage());
            }
        };

        findViewById(R.id.newGameBn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: How to keep two users from joining different new games?
                Game newGame = new Game(mUsername);
                String key = mFirebaseDatabaseReference.child("games").push().getKey();
                mFirebaseDatabaseReference.child("games").child(key).setValue(newGame);

                if (mUser.getGames() != null) {
                    mUser.getGames().add(key);
                } else {
                    ArrayList<String> games = new ArrayList<>();
                    games.add(key);
                    mUser.setGames(games);
                }
                // TODO: Add the game to the other user too
                mUserDatabaseReference.setValue(mUser);

                // Update the referenced game
                initializeGameDatabaseReference(mUser.getGames());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserDatabaseReference.addValueEventListener(mUserEventListener);
    }

    private void initializeGameDatabaseReference(List<String> games) {
        if (games == null || games.size() == 0) {
            // TODO start new game option
            return;
        }
        // Maybe pick the last game in the user's game list?
        mGameDatabaseReference = mFirebaseDatabaseReference.child("games").child(
                games.get(games.size() - 1));
        mGameDatabaseReference.addValueEventListener(mGameEventListener);
    }

    @Override
    protected void onStop() {
        // TODO: remove event listener
        if (mUserDatabaseReference != null) {
            mUserDatabaseReference.removeEventListener(mUserEventListener);
        }
        if (mGameDatabaseReference != null) {
            mGameDatabaseReference.removeEventListener(mGameEventListener);
        }
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
            if (TextUtils.equals(mGameView.getGame().getWinner(), mUsername)) {
                mUser.setWins(mUser.getWins() + 1);
                mUserDatabaseReference.setValue(mUser);
            }
            doEndGameUi();
        } else {
            ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                    R.string.waiting_for_player));
        }
        // Update the view in real-time to avoid lag
        mGameView.updateGame(mGameView.getGame());

        // Update the game in the Firebase database
        mGameDatabaseReference.setValue(mGameView.getGame());
    }

    private void doEndGameUi() {
        // Then the game is over.
        // Display a message about who won
        if (TextUtils.equals(mGameView.getGame().getWinner(), mUsername)) {
            ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                    R.string.you_win));
        } else if (mGameView.getGame().getResult() == Game.NO_PLAYER) {
            ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                    R.string.cats_game));
        } else {
            ((TextView) findViewById(R.id.status)).setText(getResources().getString(
                    R.string.you_lose));
        }
        Toast.makeText(this, getResources().getString(R.string.game_over),
                Toast.LENGTH_SHORT).show();

        // Show a new game button!
        // TODO: Need to update the user when they start a new game with the game field.
        // May also need to update the other user with the new game too
    }
}
