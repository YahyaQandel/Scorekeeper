/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.scorekeeper;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ScorePlusMinusTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    /**
     * Tests the score plus and minus buttons.
     */
    @Test
    public void scorePlusMinusTest() throws InterruptedException {
        ViewInteraction appCompatImageButton = onView(withId(R.id.increaseTeam1));
        appCompatImageButton.perform(click());
        Thread.sleep(1000);

        ViewInteraction textView = onView(withId(R.id.score_1));
        textView.check(matches(withText("1")));
        Thread.sleep(1000);

        ViewInteraction appCompatImageButton2 = onView(withId(R.id.decreaseTeam1));
        appCompatImageButton2.perform(click());

        ViewInteraction textView2 = onView(withId(R.id.score_1));
        textView2.check(matches(withText("0")));
        Thread.sleep(2000);
    }
}