package algonquin.cst2335.mobilegroupassignment;
/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.mobilegroupassignment.aram.MainRecipeActivity;
import algonquin.cst2335.mobilegroupassignment.mahsa.MainDictionaryActivity;

import com.android.application.R;

/**
 * These is the Menu or dashboard of
 * the application. This class provides the function for the
 * 4 buttons, when click it launches an activity.
 * manages the navigation between different parts of your application,
 * including launching the activities for different features such as Rustom's activity,
 * Mahsa's dictionary activity, Farock,  and Aram's recipe activity.
 *
 * @Author Rustom
 * @Since March/08/2024
 */
public class MainActivity extends AppCompatActivity {

    /**DO NOT CODE IN THis CLASS! Only code, when you link the button to your activity **/
    /**These class provide the function to launch your class **/
    /**Provide comment for your changes**/
    /**Create a class for your own activity. Check RustomActivity class Example**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /** ~Rustom Function
         * This fuction when click launch the button from my activity class(RustomClass)
         * Sunrice and Sunset Look.
         *
         * Copy and follow this code for linking your activity
         */
        Button sunRiseSetButton = findViewById(R.id.RustomButton); //Make sure its your button from the xml activity
        sunRiseSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RustomClass.class);//Make sure its your class from the class activity
                startActivity(intent);
            }



            /********** CODE BELOW inside the onCreate ************/


        });

        findViewById(R.id.NathanielButton).setOnClickListener(e -> startActivity(new Intent(this, DeezerRoom.class)));

        // ARAM RECIPE BTN (When this button is clicked, it launches an activity represented by the MainRecipeActivity class.)
        findViewById(R.id.action_aram).setOnClickListener(e -> startActivity(new Intent(this, MainRecipeActivity.class)));

        // MAHSA Dictionary API
        findViewById(R.id.MahsaButton).setOnClickListener(e -> startActivity(new Intent(this, MainDictionaryActivity.class)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action for Rustom's activity
        if (item.getItemId() == R.id.action_rustom) {
            startActivity(new Intent(this, RustomClass.class));
            return true;
        } else if (item.getItemId() == R.id.action_farock1) {
            startActivity(new Intent(this, DeezerRoom.class));
            return true;
        }
// Handle the action for Aram's activity
        if (item.getItemId() == R.id.aramm) {
            startActivity(new Intent(this, MainRecipeActivity.class));
            return true;
        }
        // Handle the action for Mahsa's activity
        else if (item.getItemId() == R.id.action_mahsa) {
            startActivity(new Intent(this, MainDictionaryActivity.class));



        }  // Handle other menu items if there are any
        else if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.help)
                .setMessage(R.string.SunriseSunseHelp)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}






