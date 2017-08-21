package com.caojian.myworkapp.login;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import com.caojian.myworkapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by CJ on 2017/8/20.
 */
@RunWith(JUnit4.class)
@LargeTest
public class CheckPhoneTest {

//    @Rule
//    public ActivityTestRule<PhoneCheckActivity> mActivityRule = new ActivityTestRule<PhoneCheckActivity>(PhoneCheckActivity.class);

    @Test
    public void checkPhone()
    {
        onView(withId(R.id.edit_check)) .perform(typeText("15651010836"), closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());

    }


}
