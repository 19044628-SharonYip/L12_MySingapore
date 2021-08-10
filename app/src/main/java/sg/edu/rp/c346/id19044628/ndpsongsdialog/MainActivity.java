package sg.edu.rp.c346.id19044628.ndpsongsdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<Island> islandList;
    int requestCode = 9;
    Button btn5Stars, btnInsert;
    CustomAdapter caIsland;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lv = (ListView) this.findViewById(R.id.lv);
        btn5Stars = (Button) this.findViewById(R.id.btnShow5Stars);
        btnInsert=findViewById(R.id.btnInsert);

        DBHelper dbh = new DBHelper(this);
        islandList = dbh.getAllIslands();
        dbh.close();

        caIsland=new CustomAdapter(this, R.layout.row, islandList);
        lv.setAdapter(caIsland);

        btn5Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(MainActivity.this);
                islandList.clear();
                islandList.addAll(dbh.getAllislandsByStars(5));
                caIsland.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra("island", islandList.get(position));
                startActivity(i);*/
                //startActivityForResult(i, requestCode);

                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra("island", islandList.get(position));
                startActivity(i);

            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.insert,null);

                final EditText etName = viewDialog.findViewById(R.id.etName);
                final EditText etDesc = viewDialog.findViewById(R.id.etDesc);
                final  EditText etArea = viewDialog.findViewById(R.id.etArea);
                final RatingBar rbStar = viewDialog.findViewById(R.id.ratingBarStar);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                myBuilder.setView(viewDialog);
                myBuilder.setTitle("Insert New Islands");
                myBuilder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString().trim();
                        String desc = etDesc.getText().toString().trim();
                        String area = etArea.getText().toString().trim();
                        int year = Integer.valueOf(area);
                        int rating = (int) rbStar.getRating();

                        DBHelper dbh = new DBHelper(MainActivity.this);
                        dbh.insertSong(name, desc, year, rating);
                        caIsland.notifyDataSetChanged();
                        islandList = dbh.getAllIslands();
                        dbh.close();

                        caIsland=new CustomAdapter(MainActivity.this, R.layout.row, islandList);
                        lv.setAdapter(caIsland);


                    }
                });

                myBuilder.setNegativeButton("CANCEL", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBHelper dbh = new DBHelper(this);
        islandList.clear();
        islandList.addAll(dbh.getAllIslands());
        caIsland.notifyDataSetChanged();
    }
}