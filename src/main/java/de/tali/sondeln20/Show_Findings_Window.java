package de.tali.sondeln20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Show_Findings_Window extends AppCompatActivity {
    private ListView lv;
    public ArrayList<Integer> idList;
    private ArrayList<Finding> findingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__findings__window);
        lv = (ListView)findViewById(R.id.listView);
        updatelist();

        getSupportActionBar().hide();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatelist();
    }

    private void updatelist() {
        DatabaseHandler db = new DatabaseHandler(this);
        findingList = db.getAllFinding();
        db.close();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, get_findingnames_for_list(findingList))
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);

                // Return the view
                return view;
            }
        };

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int tempId = findingList.get(position).getid();
                open_details(tempId);
            }
        });
    }

   private ArrayList<String> get_findingnames_for_list(ArrayList<Finding> findinglist)
   {
       ArrayList<String> nameList = new ArrayList<String>();
       if ( findinglist.size() != 0)
       {
           for (int i = 0; i < findinglist.size(); i++) {
               String name = findinglist.get(i).getName().toString();
               nameList.add(i, name);
           }
       }
       else
       {
           nameList.add(0, "Keine Fundstücke vorhanden!!!");
       }
       return nameList;
   }
    private void open_details(int tempId)
    {
        Intent i = new Intent(this,Show_Finding_Details.class);
        i.putExtra("id",tempId);
        startActivity(i);
    }


}
