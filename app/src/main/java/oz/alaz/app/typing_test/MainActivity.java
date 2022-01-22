package oz.alaz.app.typing_test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView v;
    char[] txtToType;
    int curr;
    long startTime; // If unstarted then it should be 0
    int wordCount;

    // todo: Make support for glide typing
    // todo: Make UI more asthetic and simple
    // todo: Show a timer when the user starts typing

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.type_text);
        v.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v.getRootView(), InputMethodManager.SHOW_FORCED);
            }
            return true;
        });
        // The string which we will be typing.
        feedText(getString(R.string.demo_type_text));

    }
    void feedText(String txt){
        txtToType = txt.toCharArray();
        curr = 0;
        startTime = 0;
        wordCount = txt.split(" ").length;
        v.setText(txtToType,0, txtToType.length);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        processKey(event.getUnicodeChar());
        return true;
    }

    private void processKey(int unicode) {
        if (unicode == v.getText().charAt(0)) {
            // if not started the typing test then start it.
            if (startTime == 0){
                startTime = System.currentTimeMillis();
            }
            // Delete each character as it is typed.
            v.setText(txtToType,++curr, txtToType.length - curr);
            // Test is over when all the characters are typed.
            System.out.println("'"+ v.getText()+"'");
            if (v.getText().length() == 0){

                System.out.println("U");
                // Test is over, lets calculate the stats.
                long currTime = System.currentTimeMillis();
                long deltaTime = currTime - startTime;
                System.out.println((float) deltaTime/1_000);

                showDialog(wordCount/((float)deltaTime/60_000));

            }
        }
    }
    void showDialog(float speed){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your typing speed is: " + speed + ". Well Done!!")
                .setCancelable(true).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                feedText(getString(R.string.demo_type_text));
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Typing Speed");
        alert.show();
    }
}