package com.example.diseaseprediction;

import com.example.diseaseprediction.object.Account;
import com.example.diseaseprediction.object.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseDatabase.class})
public class MainActivityUnitTest {
  // Test data
  private static final String AccountID = "test_acc_id";
  private static final String AccountName = "Test_account";
  private static final int AccountTypeId = 1;

  private static final Account testAccount = new Account(AccountID, AccountTypeId, "", AccountName, 0, "", "", "",
      new Date(), new Date(), 1);
  private static final Message testMessage = new Message("message ID", "senderID", "receiverID", "message",
      new Date(), "session", 1);

  private DatabaseReference mockedDatabaseReference;

  @Before
  public void before() {
    mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

    FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
    when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

    PowerMockito.mockStatic(FirebaseDatabase.class);
    when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
  }

  @Test
  public void getUserMessagesTest() {
    when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];

        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
        when(mockedDataSnapshot.getValue(Message.class)).thenReturn(testMessage);

        valueEventListener.onDataChange(mockedDataSnapshot);

        Chat chat = new Chat();


        return null;
      }
    }).when(mockedDatabaseReference).addValueEventListener(any(ValueEventListener.class));


  }
}
