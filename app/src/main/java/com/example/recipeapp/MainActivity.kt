package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapp.data.database.DatabaseViewModel
import com.example.recipeapp.data.database.OnGetDataListener
import com.example.recipeapp.model.User
import com.example.recipeapp.ui.home.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login_screen.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var database: DatabaseReference
    private lateinit var databaseViewModel: DatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        // get the instance of the firebase db
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // set the account to the current user of someone is logged in already
        val account = auth.currentUser

        if(account == null){
            // start the sign in ritual
            btn_google_sign_in.setOnClickListener {
                signIn()
            }
        } else {
            setFragment(account)
        }

    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("failed", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome, ${user!!.displayName}", Toast.LENGTH_SHORT).show()
                    setFragment(user)
                    saveUser(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FAILED", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Switches to the homefragment and passes the user to the fragment
     */
    fun setFragment(account: FirebaseUser?) {
        val fragment = HomeFragment()
        fragment.account = account

        setContentView(R.layout.activity_main)
        supportActionBar?.show()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Checks if the user already exists, if not then it saves the user
     */
    private fun saveUser(acc: FirebaseUser?) {
        databaseViewModel.readDataOnce("users", object :
            OnGetDataListener {

            // On success data return
            override fun onSuccess(data: DataSnapshot) {
                val children = data.children
                children.forEach {
                    // for every record check if the key exists or not, if not save new user
                    if (!it.key.equals(acc!!.uid)) {
                        val user = User(acc.uid)
                        database.child("users").child(acc.uid).setValue(user)
                    }
                }
            }

            override fun onFailed(databaseError: DatabaseError) {
                Log.d("NO DATA ERROR: ", databaseError.message)
            }

        })
    }

}
