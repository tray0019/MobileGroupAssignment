package algonquin.cst2335.mobilegroupassignment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.android.application.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeezerRoomTest {

    @Rule
    public ActivityScenarioRule<DeezerRoom> mActivityScenarioRule =
            new ActivityScenarioRule<>(DeezerRoom.class);

    @Test
    public void testEmptyField() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.searchButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.editText));
        textView.check(matches(withHint("Please enter artist name")));
    }
    @Test
    public void testNonEmptyField() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Dido"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.searchButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.editText));
        textView.check(matches(withHint("Please enter artist name")));
    }
    @Test
    public void testNonEmptyFieldNum() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("45698"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.searchButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.editText));
        textView.check(matches(withHint("Please enter artist name")));
    }

    @Test
    public void testNonEmptyFieldSpecial() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("/././."), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.searchButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.editText));
        textView.check(matches(withHint("Please enter artist name")));
    }

    @Test
    public void testNonEmptyFieldMix() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Di452/,/."), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.searchButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.editText));
        textView.check(matches(withHint("Please enter artist name")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
