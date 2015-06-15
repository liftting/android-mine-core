package com.xm.cygcore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.xm.cygcore.util.shortcut.XmShortCutUtil;
import com.xm.cygcore.view.dataloadview.XmAutoLoadListView;


public class MainActivity extends Activity {


    private XmAutoLoadListView mLoadListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_activity_main);
        mLoadListView = (XmAutoLoadListView) findViewById(R.id.v_data_load_list);
        String[] s = new String[100];
        for (int i = 0; i < s.length; i++) {
            s[i] = "" + i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s);

        mLoadListView.setAdapter(adapter);

        XmShortCutUtil.getInstance(this).createShortCut();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
