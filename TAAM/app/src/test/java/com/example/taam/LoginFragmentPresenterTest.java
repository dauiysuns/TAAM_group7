package com.example.taam;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Build;

import com.example.taam.ui.login.LoginFragmentModel;
import com.example.taam.ui.login.LoginFragmentPresenter;
import com.example.taam.ui.login.LoginFragmentView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class LoginFragmentPresenterTest {

    @Mock
    LoginFragmentView view;

    @Mock
    FirebaseAuth firebaseAuth;

    @Mock
    Task<AuthResult> mockTask;

    private LoginFragmentModel model;
    private LoginFragmentPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new LoginFragmentModel(firebaseAuth);
        presenter = new LoginFragmentPresenter(view, model);
    }

    @Test
    public void testEmptyEmail() {
        String email = "";
        String password = "password123";
        presenter.tryLogin(email, password);
        verify(view).invalidInputView();
    }

    @Test
    public void testEmptyPassword() {
        String email = "demo";
        String password = "";
        presenter.tryLogin(email, password);
        verify(view).invalidInputView();
    }

    @Test
    public void testLoginSuccess() {
        String email = "demo@demo.com";
        String password = "demodemo";

        when(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
        when(mockTask.addOnCompleteListener(any())).thenAnswer(new Answer<Object>() {

            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.loginSuccessPresenter();
                return null;
            }
        });

        presenter.tryLogin(email, password);

        verify(view).loginSuccessView();
    }

    @Test
    public void testLoginFailure() {
        String email = "demo@demo.com";
        String password = "demodemo";

        when(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
        when(mockTask.addOnCompleteListener(any())).thenAnswer(new Answer<Object>() {

            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.loginFailurePresenter("error");
                return null;
            }
        });

        presenter.tryLogin(email, password);

        verify(view).loginFailureView();
    }

//    @Test
//    public void testLoginFailure() {
//        String email = "demo@demo.com";
//        String password = "demodem";
//
//        // Mock failed login
//        when(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
//        when(mockTask.isSuccessful()).thenReturn(false);
//
//        presenter.tryLogin(email, password);
//
//        verify(view).loginFailureView();
//    }
}
