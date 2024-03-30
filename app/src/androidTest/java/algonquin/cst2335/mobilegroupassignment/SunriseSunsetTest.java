package algonquin.cst2335.mobilegroupassignment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SunriseSunsetTest {

    @Rule
    public ActivityScenarioRule<RustomClass> mActivityScenarioRule =
            new ActivityScenarioRule<>(RustomClass.class);

    @Test
    public void testSunriseSunsetLookup() {
        onView(withId(R.id.latitudeEditText)).perform(replaceText("40.7128"), closeSoftKeyboard());
        onView(withId(R.id.longitudeEditText)).perform(replaceText("-74.0060"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());

        onView(withId(R.id.sunriseTimeTextView)).check(matches(not(withText(""))));
        onView(withId(R.id.sunsetTimeTextView)).check(matches(not(withText(""))));
    }


    @Test
    public void testLoadLastSearch() {
        // Precondition: Assume a search has been made previously
        onView(withId(R.id.latitudeEditText)).check(matches(withText(not(""))));
        onView(withId(R.id.longitudeEditText)).check(matches(withText(not(""))));
    }


    @Test
    public void testSaveToFavorites() {
        onView(withId(R.id.latitudeEditText)).perform(replaceText("34.0522"), closeSoftKeyboard());
        onView(withId(R.id.longitudeEditText)).perform(replaceText("-118.2437"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());
        onView(withId(R.id.saveToFavoritesButton)).perform(click());
    }

    @Test
    public void testInputValidation() {
        onView(withId(R.id.latitudeEditText)).perform(replaceText("invalid input"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());
    }

    @Test
    public void testInputLongitude() {
        onView(withId(R.id.longitudeEditText)).perform(replaceText("invalid input"), closeSoftKeyboard());
        onView(withId(R.id.lookupButton)).perform(click());
    }



}
