package algonquin.cst2335.mobilegroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.application.R;

/**
 * These is the Menu or dashboard of
 * the application. This class provides the function for the
 * 4 buttons, when click it launches an activity.
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
                    Intent intent = new Intent(MainActivity.this, DeezerRoom.class);//Make sure its your class from the class activity
                    startActivity(intent);
            }

            /********** CODE BELOW inside the onCreate ************/

        }) ;

        }
    }




