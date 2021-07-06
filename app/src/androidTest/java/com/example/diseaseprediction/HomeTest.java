package com.example.diseaseprediction;


import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeTest {

//  @Rule
//  public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);
//
//  @Test
//  public void homeTest() {
//    ViewInteraction materialButton = onView(
//        allOf(withId(R.id.login_btn_login_by_google), withText("Google"),
//            childAtPosition(
//                childAtPosition(
//                    withClassName(is("android.widget.ScrollView")),
//                    0),
//                7)));
//    materialButton.perform(scrollTo(), click());
//
//    ViewInteraction recyclerView = onView(
//        allOf(withId(R.id.home_doctor_all_prediction_recycle_view),
//            withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
//            isDisplayed()));
//    recyclerView.check(matches(isDisplayed()));
//
//    ViewInteraction textView = onView(
//        allOf(withId(R.id.home_txt_title), withText("Hello doctor!"),
//            withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
//            isDisplayed()));
//    textView.check(matches(withText("Hello doctor!")));
//
//    ViewInteraction searchView = onView(
//        allOf(withId(R.id.home_search_view),
//            withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
//            isDisplayed()));
//    searchView.check(matches(isDisplayed()));
//
//    ViewInteraction relativeLayout = onView(
//        allOf(withId(R.id.home_doctor_all_prediction_layout_title),
//            withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
//            isDisplayed()));
//    relativeLayout.check(matches(isDisplayed()));
//
//    ViewInteraction textView2 = onView(
//        allOf(withId(R.id.home_doctor_all_prediction_txt_title), withText("Pending Prediction"),
//            withParent(allOf(withId(R.id.home_doctor_all_prediction_layout_title),
//                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
//            isDisplayed()));
//    textView2.check(matches(withText("Pending Prediction")));
//
//    ViewInteraction textView3 = onView(
//        allOf(withId(R.id.home_doctor_all_prediction_txt_see_more), withText("See more"),
//            withParent(allOf(withId(R.id.home_doctor_all_prediction_layout_title),
//                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
//            isDisplayed()));
//    textView3.check(matches(withText("See more")));
//
//    ViewInteraction textView4 = onView(
//        allOf(withId(R.id.home_doctor_all_prediction_txt_see_more), withText("See more"),
//            withParent(allOf(withId(R.id.home_doctor_all_prediction_layout_title),
//                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
//            isDisplayed()));
//    textView4.check(matches(withText("See more")));
//  }
//
//  private static Matcher<View> childAtPosition(
//      final Matcher<View> parentMatcher, final int position) {
//
//    return new TypeSafeMatcher<View>() {
//      @Override
//      public void describeTo(Description description) {
//        description.appendText("Child at position " + position + " in parent ");
//        parentMatcher.describeTo(description);
//      }
//
//      @Override
//      public boolean matchesSafely(View view) {
//        ViewParent parent = view.getParent();
//        return parent instanceof ViewGroup && parentMatcher.matches(parent)
//            && view.equals(((ViewGroup) parent).getChildAt(position));
//      }
//    };
//  }
}
