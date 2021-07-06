package com.example.diseaseprediction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
  @Mock
  private Context mockApplicationContext;
  @Mock
  private Resources mockContextResources;
  @Mock
  private SharedPreferences mockSharedPreferences;


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mockApplicationContext.getResources()).thenReturn(mockContextResources);
    when(mockApplicationContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);

    when(mockContextResources.getString(anyInt())).thenReturn("mocked string");
  }

  @Test
  public void checkPhoneNumberInputTest() throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {

    TextInputLayout mockedPhoneInputLayout = mock(TextInputLayout.class, RETURNS_MOCKS);

    String testPhoneNumber1 = "0123456789";
    String testPhoneNumber2 = "123456789";
    String testPhoneNumber3 = "1234567890";
    String testPhoneNumber4 = "01234567890";

//    Mockito.doAnswer(new Answer() {
//      @Override
//      public Object answer(InvocationOnMock invocation) throws Throwable {
//        TextInputLayout mockedPhoneInputLayout = (TextInputLayout) invocation.getMock();
//        return mockedPhoneInputLayout;
//      }
//    }).when(mockedPhoneInputLayout).setError(any());
//    ContextThemeWrapper contextThemeWrapper = mock(ContextThemeWrapper.class);
//    when(contextThemeWrapper.getResources()).thenReturn(mockContextResources);
//    doNothing().when(mockedPhoneInputLayout).setError(any());


    Login obj = new Login();
    Method privateMethod = Login.class.getDeclaredMethod("checkPhoneNumberInput", String.class);
    privateMethod.setAccessible(true);


    // valid
    Boolean returnValue1 = (Boolean) privateMethod.invoke(obj, testPhoneNumber1);
    Boolean returnValue2 = (Boolean) privateMethod.invoke(obj, testPhoneNumber2);
//    Boolean returnValue3 = (Boolean) privateMethod.invoke(obj, testPhoneNumber3);
//    Boolean returnValue4 = (Boolean) privateMethod.invoke(obj, testPhoneNumber4);


    assertEquals(true, returnValue1);
    assertEquals(true, returnValue2);
//    assertEquals(false, returnValue3);
//    assertEquals(false, returnValue4);
  }

  @Test
  public void addCountryCodeTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String input1 = "0123456789";
    String input2 = "123456789";

    String expected1 = "+84123456789";
    String expected2 = "+84123456789";

    Login obj = new Login();
    Method privateMethod = Login.class.getDeclaredMethod("addCountryCode", String.class);
    privateMethod.setAccessible(true);

    // Get return value
    String return1 = (String) privateMethod.invoke(obj, input1);
    String return2 = (String) privateMethod.invoke(obj, input2);

    assertEquals(expected1, return1);
    assertEquals(expected2, return2);
  }

}
