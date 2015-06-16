package com.teinproductions.tein.papyrosprogress;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ConfigurationSmallActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView textSizeTextView;

    int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setResult(RESULT_CANCELED);

        restoreAppWidgetId();

        seekBar = (SeekBar) findViewById(R.id.textSize_SeekBar);
        textSizeTextView = (TextView) findViewById(R.id.textSize_textView);

        int progress = getSharedPreferences(ConfigurationActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE).getInt(ConfigurationActivity.TEXT_SIZE_PREFERENCE + appWidgetId, 24);
        seekBar.setProgress(progress);
        textSizeTextView.setText(getString(R.string.text_size) + " " + progress);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void restoreAppWidgetId() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textSizeTextView.setText(getString(R.string.text_size) + " " + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { /*ignore*/ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { /*ignore*/ }

    public void onClickCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onClickOK(View view) {
        int newTextSize = seekBar.getProgress();
        getSharedPreferences(ConfigurationActivity.SHARED_PREFERENCES, MODE_PRIVATE).edit()
                .putInt(ConfigurationActivity.TEXT_SIZE_PREFERENCE + appWidgetId, newTextSize).apply();

        ProgressWidgetSmall.updateAppWidgets(this, new int[]{appWidgetId});

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
