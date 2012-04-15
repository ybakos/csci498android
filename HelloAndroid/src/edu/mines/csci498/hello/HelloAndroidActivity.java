package edu.mines.csci498.hello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class HelloAndroidActivity extends Activity implements View.OnClickListener {

	Button btn;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        btn = new Button(this);
        btn.setOnClickListener(this);
        updateTime();
        setContentView(btn);
    }
    
    public void onClick(View view) {
    	updateTime();
    }
    
    public void updateTime() {
    	btn.setText(new Date().toString());
    }

}