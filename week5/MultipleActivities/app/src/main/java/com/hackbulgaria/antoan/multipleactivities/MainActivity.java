package com.hackbulgaria.antoan.multipleactivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.EditText;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.browse).setOnClickListener(this);
        findViewById(R.id.set_alarm).setOnClickListener(this);
        findViewById(R.id.dial).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String text = ((EditText) findViewById(R.id.edit_text)).getText().toString();
        Intent intent = null;
        if(v.getId() == R.id.dial && matches(text, "[0-9\\+]{3,20}"))
            intent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + text));
        else if(v.getId() == R.id.browse && matches(text, "[a-z]+\\.[a-z]+\\.[a-z]+"))
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + text));
        else if(v.getId() == R.id.set_alarm && matches(text, "[0-9][0-9]:[0-9][0-9]"))
            intent = new Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(text.split(":")[0]))
                    .putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(text.split(":")[1]));

        if(intent != null)
            startActivity(intent);
    }
    private boolean matches(String text, String pattern) {
        boolean result = Pattern.compile(pattern).matcher(text).matches();
        if (!result)
            new AlertDialog.Builder(this).setTitle("Invalid input").setMessage("Please enter valid input.").setPositiveButton("OK", null).show();
        return result;
    }
}