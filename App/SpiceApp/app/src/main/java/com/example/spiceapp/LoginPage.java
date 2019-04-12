package com.example.spiceapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiceapp.databinding.ActivityLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * This class implements the LoginPage activity,
 * where users may login or register for the app.
 *
 * @author Alan Manning
 */

public class LoginPage extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private Animation atg3;
    private ActivityLoginPageBinding loginPageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setExitTransition(new Fade());
        mAuth = FirebaseManager.getAuth();

        setContentView(R.layout.activity_login_page);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        final CardView btnLogin = (CardView) findViewById(R.id.cardLogin);
//        final CardView btnRegister = (CardView) findViewById(R.id.cardRegister);
//        final TextView edtEmail = (TextView) findViewById(R.id.edtEmail);
//        final TextView edtPassword = (TextView) findViewById(R.id.edtLoginPassword);

        // activity_home_page.xml data binding
        // Eliminates need for findViewByID()
        loginPageBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_page);

        // Load animations
        atg3 = AnimationUtils.loadAnimation(this, R.anim.atg3);

        loginPageBinding.btnLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginPageBinding.edtEmail.getText().toString();
                String password = loginPageBinding.edtLoginPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty())
                    signInUser(email, password);
            }
        });

        loginPageBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final View img = findViewById(R.id.imageView3);
                Intent nextScreen = new Intent(v.getContext(), RegisterPage.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginPage.this, img, "regUser");
                startActivityForResult(nextScreen, 0);
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    user = mAuth.getCurrentUser();
//                    startActivityForResult(new Intent(LoginPage.this, HomePage.HomePageActivity.class), 0);
                    load(loginPageBinding.revealView);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginPage.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Loads view for loading icon
    public void load(View v) {
        animateButtonWidth();
        loginPageBinding.btnRegister.startAnimation(atg3);
        loginPageBinding.btnRegister.setVisibility(View.INVISIBLE);
        fadeOutTextAndSetProgressDialog();
        nextAction();
    }

    // Changes button width upon animation
    private void animateButtonWidth(){
        ValueAnimator anim = ValueAnimator.ofInt(loginPageBinding.btnLoginBtn.getMeasuredWidth(), getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
//                  Gets layout parameters of cardview and updates
                ViewGroup.LayoutParams layoutParams = loginPageBinding.btnLoginBtn.getLayoutParams();
                layoutParams.width = value;
                loginPageBinding.btnLoginBtn.requestLayout();
            }
        });

        // Sets duration of animation and starts animation
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutTextAndSetProgressDialog(){
        loginPageBinding.btnLoginBtn.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        }).start();
    }

    // Shows progress bar dialog
    private void showProgressDialog(){
        loginPageBinding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        loginPageBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void nextAction(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNextActivity();
            }
        }, 2000);
    }

    private void revealButton(){
        loginPageBinding.btnLoginBtn.setElevation(0f);
        loginPageBinding.revealView.setVisibility(View.VISIBLE);

        int x = loginPageBinding.revealView.getWidth();
        int y = loginPageBinding.revealView.getHeight();

        int startX = (int) (getFinalWidth() / 2 + loginPageBinding.btnLoginBtn.getX());
        int startY = (int) (getFinalWidth() / 2 + loginPageBinding.btnLoginBtn.getY());

        float radius = Math.max(x, y) * 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(loginPageBinding.revealView, startX, startY, getFinalWidth(), radius);
        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                finish();
            }
        });

        reveal.start();
    }

    private void fadeOutProgressDialog(){
        loginPageBinding.progressBar.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(LoginPage.this, SpiceItUp.class), 0);
            }
        }, 100);
    }

    // Returns last width of login button animation
    private int getFinalWidth(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }

}
