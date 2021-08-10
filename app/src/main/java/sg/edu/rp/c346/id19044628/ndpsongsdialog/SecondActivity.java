package sg.edu.rp.c346.id19044628.ndpsongsdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    EditText etID, etName, etDesc, etArea;
    //RadioButton rb1, rb2, rb3, rb4, rb5;
    Button btnCancel, btnUpdate, btnDelete;
    //RadioGroup rg;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.edName);
        etDesc = (EditText) findViewById(R.id.etDEsc);
        etArea = (EditText) findViewById(R.id.etAreaa);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar2);

        Intent i = getIntent();
        final Island currentIsland = (Island) i.getSerializableExtra("island");

        etID.setText(currentIsland.getId()+"");
        etName.setText(currentIsland.getName());
        etDesc.setText(currentIsland.getDescription());
        etArea.setText(currentIsland.getArea()+"");
        ratingBar.setRating(currentIsland.getStars());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(SecondActivity.this);
                currentIsland.setName(etName.getText().toString().trim());
                currentIsland.setDescription(etDesc.getText().toString().trim());
                int area = 0;
                try {
                    area = Integer.valueOf(etArea.getText().toString().trim());
                } catch (Exception e){
                    Toast.makeText(SecondActivity.this, "Invalid area", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentIsland.setArea(area);

                currentIsland.setStars((int) ratingBar.getRating());

                int result = dbh.updateIsland(currentIsland);
                if (result>0){
                    Toast.makeText(SecondActivity.this, "Island updated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    setResult(RESULT_OK);

                    dbh.close();
                    finish();
                } else {
                    Toast.makeText(SecondActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(SecondActivity.this);

                myBuilder.setTitle("Danger");
                myBuilder.setMessage("Are you sure you want to delete the island \n "+ etName.getText().toString().trim());
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("Cancel",null);
                myBuilder.setNeutralButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbh = new DBHelper(SecondActivity.this);
                        int result = dbh.deleteSong(currentIsland.getId());
                        if (result>0){
                            Toast.makeText(SecondActivity.this, "Island deleted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(SecondActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(SecondActivity.this);

                myBuilder.setTitle("Danger");
                myBuilder.setMessage("Are you sure you want to discard the changes");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("Do not discard",null);
                myBuilder.setNeutralButton("discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });


    }

}