package com.example.eric.mylist;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;


public class MainActivity extends ActionBarActivity {

    String[] colourNames;
    String[] colourIds;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colourNames = getResources().getStringArray(R.array.listArray);
        colourIds = getResources().getStringArray(R.array.listValues);

        try {
            int pos = 0;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/Downloads");
            File colours = new File(dir, "Colour.txt");
            FileInputStream in = new FileInputStream(colours);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(in));
            String position = myReader.readLine();
            pos = Integer.parseInt(position);
            if (pos != 0) {
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.mLayout);
                layout.setBackgroundColor(Color.parseColor(colourIds[pos]));
            }
            in.close();
        } catch (Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.mLayout);
                layout.setBackgroundColor(Color.parseColor(colourIds[position]));
                pos = position;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write Colour to SD Card");
        menu.add(0, v.getId(), 0, "Read Colour from SD Card");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write Colour to SD Card") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int listPosition = info.position;
            try {
                String position = Integer.toString(listPosition);
                String selected = colourNames[listPosition];
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Downloads");
                dir.mkdirs();
                File colours = new File(dir, "Colour.txt");
                FileOutputStream f = new FileOutputStream(colours);
                OutputStreamWriter writer = new OutputStreamWriter(f);
                writer.append(position + "\n");
                writer.append(selected +"\n");
                writer.close();
                f.close();
                Toast.makeText(getBaseContext(),
                        "Colour written to SD card.",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

         else if (item.getTitle() == "Read Colour from SD Card") {
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Downloads");
                File colours = new File(dir, "Colour.txt");
                FileInputStream in = new FileInputStream(colours);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(in));
                String position = myReader.readLine();
                int pos = Integer.parseInt(position);
                String datarow = "";
                String buffer = "";
                while ((datarow = myReader.readLine()) != null) {
                    buffer += datarow ;
                }
                myReader.close();
                in.close();
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.mLayout);
                layout.setBackgroundColor(Color.parseColor(colourIds[pos]));
                Toast.makeText(getBaseContext(), "Background colour set to " + buffer,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            return false;
        }
        return true;
    }
}
