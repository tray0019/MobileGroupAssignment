package algonquin.cst2335.mobilegroupassignment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * We can have one class to test all or we can seperate clasess
 * for testing. Ethier way are no problems.
 */
@RunWith(AndroidJUnit4.class)
public class GroupAppTesting {

    /** This Boundary are rustom Test!
     **********************Rustom Test****************************/

    @Rule
    public ActivityScenarioRule<RustomClass> activityRule =
            new ActivityScenarioRule<>(RustomClass.class);

    @Test
    public void testSuccessfulDataRetrieval() {
        onView(withId(R.id.latitudeEditText)).perform(replaceText("40.7128"), closeSoftKeyboard());
        onView(withId(R.id.longitudeEditText)).perform(replaceText("-74.0060"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());
        onView(withId(R.id.sunriseTimeTextView)).check(matches(withText(not(isEmptyOrNullString()))));
        onView(withId(R.id.sunsetTimeTextView)).check(matches(withText(not(isEmptyOrNullString()))));
    }

    @Test
    public void testSaveToFavorites() {
        onView(withId(R.id.saveToFavoritesButton)).perform(click());
        onView(withId(R.id.favoritesRecyclerView)).check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void testLoadLastSearch() {
        // Assuming last search was saved
        activityRule.getScenario().recreate();
        onView(withId(R.id.latitudeEditText)).check(matches(withText("40.7128")));
        onView(withId(R.id.longitudeEditText)).check(matches(withText("-74.0060")));
    }

    @Test
    public void testDeleteFromFavorites() {
        onView(withId(R.id.favoritesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.deleteButton)));
        onView(withId(R.id.favoritesRecyclerView)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void testInvalidInputErrorHandling() {
        onView(withId(R.id.latitudeEditText)).perform(replaceText("not a number"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());
        onView(withId(R.id.latitudeEditText)).check(matches(withText("Please enter a valid number")));
    }

    /*********************Rustom Test****************************/

    /** This Boundary are Mahsa's Test!
     *********************Mahsa's Test***************************/



    /********************Mahsa's Test****************************/

    /** This Boundary are Nathaniel Test!
     *********************Nathaniel Test***************************/



    /*********************Nathaniel Test***************************/

    /** This Boundary are Aram Test!
     *********************Aram Test***************************/



    /*********************Aram Test***************************/


}