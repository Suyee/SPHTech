package com.suyee.mm;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

public class MainActivityTest {

    private RecyclerView recyclerView;
    private int itemSize;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        recyclerView = activityRule.getActivity().findViewById(R.id.list);
        itemSize = recyclerView.getAdapter().getItemCount();
    }

    @Test
    public void recycler_isVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .inRoot(RootMatchers.withDecorView(Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void recycler_isScrollable() {
        Espresso.onView(ViewMatchers.withId(R.id.list))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(itemSize - 1));
    }

    @Test
    public void recycler_isItemClickable() {
        int index = new Random().nextInt(itemSize);
        Espresso.onView(ViewMatchers.withId(R.id.list))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(index, ViewActions.click()));
    }

    @Test
    public void recycler_isItemViewVisible() {
        int index = new Random().nextInt(itemSize);
        Espresso.onView(ViewMatchers.withId(R.id.list))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(activityRule.getActivity().getWindow().getDecorView())))
                .check(ViewAssertions.matches(itemViewAtPosition(index, Matchers.allOf(
                        ViewMatchers.withId(R.id.year), ViewMatchers.isDisplayed()))));
    }

    public Matcher<View> itemViewAtPosition (final int position, final Matcher<View> itemMatcher){
        return new BoundedMatcher(RecyclerView.class) {
            @Override
            protected boolean matchesSafely(Object item) {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(Description description) {
                itemMatcher.describeTo(description);
            }
        };
    }

}
