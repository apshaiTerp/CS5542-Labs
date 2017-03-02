package com.ac.vr.lab6rest;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static Activity reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reference = this;
    }

    public void doClarifaiLookup(int image) {
        System.out.println("Should I be firing right now?");
        AlertDialog.Builder builder = new AlertDialog.Builder(reference);
        builder.setMessage("Is this a message?")
                .setTitle("Wat?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void doSparkLookup(int image) {
        System.out.println("I think I should be firing right now?");
    }

    public void doCoworkerClarifai(View view) {
        doClarifaiLookup(1);
    }

    public void doPrattClarifai(View view) {
        doClarifaiLookup(2);
    }

    public void doCowokerSpark(View view) {
        doSparkLookup(1);
    }

    public void doPrattSpark(View view) {
        doSparkLookup(2);
    }
}
