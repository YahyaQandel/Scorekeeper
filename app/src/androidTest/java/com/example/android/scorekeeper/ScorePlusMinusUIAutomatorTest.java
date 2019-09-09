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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.filters.SdkSuppress;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ScorePlusMinusUIAutomatorTest {

    private static final String SCORE_KEEPER_PACKAGE = "com.example.android.scorekeeper";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice device;


    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(SCORE_KEEPER_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(SCORE_KEEPER_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }



    @Test
    public void checkPreconditions() {
        assertThat(device, notNullValue());
    }

    @Test
    public void testPlusAndMinus() {
        // click plus button for the first team
        device.findObject(By.res(SCORE_KEEPER_PACKAGE, "increaseTeam1")).click();

        // Verify the result for the team is updated
        UiObject2 firstTeamResult = device
                .wait(Until.findObject(By.res(SCORE_KEEPER_PACKAGE, "score_1")),
                        500 /* wait 500ms */);
        assertThat(firstTeamResult.getText(), is(equalTo("1")));


        // click minus button for the first team
        device.findObject(By.res(SCORE_KEEPER_PACKAGE, "decreaseTeam1")).click();

        assertThat(firstTeamResult.getText(), is(equalTo("0")));
    }


    @Test
    public void testMinusShouldNotResultNegative() {
        // Verify the result for the team is zero
        UiObject2 decreasedText = device
                .wait(Until.findObject(By.res(SCORE_KEEPER_PACKAGE, "score_1")),
                        500 /* wait 500ms */);
        assertThat(decreasedText.getText(), is(equalTo("0")));


        // click minus button for the first team
        device.findObject(By.res(SCORE_KEEPER_PACKAGE, "decreaseTeam1")).click();

        assertThat(decreasedText.getText(), is(equalTo("0")));
    }


    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}