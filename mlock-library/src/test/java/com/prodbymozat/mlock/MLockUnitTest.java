/*
  Copyright (c) 2019 Mozart Alexander Louis

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */

package com.prodbymozat.mlock;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.prodbymozat.mlock.exceptions.MLockInitializedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;

/**
 * Unit Test Class for {@link MLock}.
 */
@FixMethodOrder(MethodSorters.JVM)
public class MLockUnitTest {

  // Local Test Data
  private final String TEST_KEY = "test_key";

  @After
  public void teardown() {
    MLock.reset();
  }

  @Test
  public void init_shouldInitMLock() {
    // Arrange
    final Context app = mock(Application.class);

    // Act & Assert
    MLock.init(app, Assert::assertNull);
  }

  @Test
  public void init_activityContext_getApplicationContext_shouldInitMLock() {
    // Arrange
    final Context app = mock(Application.class);
    final Context activity = mock(Activity.class);
    when(activity.getApplicationContext()).thenReturn(app);

    // Act & Assert
    MLock.init(activity, Assert::assertNull);
  }

  @Test
  public void init_activityContext_getApplicationContext_verifyApplicationContext() {
    // Arrange
    Context app = mock(Application.class);
    Context activity = mock(Context.class);
    when(activity.getApplicationContext()).thenReturn(app);

    // Act
    MLock.init(activity, Assert::assertNull);

    // Assert
    verify(activity, times(1)).getApplicationContext();
  }

  @Test
  public void init_calledTwice_ListenerShouldReceiveMLockInitializedException() {
    // Arrange
    final Context app = mock(Application.class);

    // Act & Assert
    MLock.init(app, Assert::assertNull);
    MLock.init(app, exception -> assertThat(exception, instanceOf(MLockInitializedException.class)));
  }

  @Test
  public void commit_shouldCallInternalCommit() {
    // Arrange
    MLock.internal = mock(MLockInternal.class);
    final MLockData data = mock(MLockData.class);

    // Act
    MLock.commit(TEST_KEY, data);

    // Assert
    verify(MLock.internal).commit(TEST_KEY, data);
  }

  @Test
  public void apply_shouldCallInternalApply() {
    // Arrange
    MLock.internal = mock(MLockInternal.class);
    final MLockData data = mock(MLockData.class);

    // Act
    MLock.apply(TEST_KEY, data);

    // Assert
    verify(MLock.internal).apply(TEST_KEY, data);
  }

  @Test
  public void get_shouldCallInternalGet() {
    // Arrange
    MLock.internal = mock(MLockInternal.class);

    // Act
    MLock.get(TEST_KEY);

    // Assert
    verify(MLock.internal).get(TEST_KEY);
  }

  @Test
  public void retrieve_shouldCallInternalRetrieve() {
    // Arrange
    MLock.internal = mock(MLockInternal.class);
    final MLockAsyncRetrieveListener retrieveListener = mock(MLockAsyncRetrieveListener.class);

    // Act
    MLock.retrieve(TEST_KEY, retrieveListener);

    // Assert
    verify(MLock.internal).retrieve(TEST_KEY, retrieveListener);
  }
}
