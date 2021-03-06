package com.allan.kostku.ActivityUser;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.allan.kostku.Model.Feedback;
import com.allan.kostku.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackDialog extends AppCompatActivity {
    private DatabaseReference database;
//    private SeekBar seekbarFeedback;
    private EditText et_feedbackDesc;
    private Button btn_submit;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_dialog);

        database = FirebaseDatabase.getInstance().getReference("Feedback");
//        seekbarFeedback = findViewById(R.id.seekbarFeedback);
        ratingBar = findViewById(R.id.ratingBar);
        et_feedbackDesc = findViewById(R.id.et_feedbackDesc);
        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeedback();
            }
        });
    }

    private void addFeedback(){
        String rating = String.valueOf(ratingBar.getRating());
        String feedbackDesc = et_feedbackDesc.getText().toString();
        if(feedbackDesc.equals("")){
            et_feedbackDesc.setError("Silahkan masukkan feedback");
            et_feedbackDesc.requestFocus();
        }else{
            String id = database.push().getKey();
            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            Feedback feedback = new Feedback(id, feedbackDesc, rating,user);
//            database.child(id).setValue(feedback);
            Toast.makeText(this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(FeedbackDialog.this,Me.class));
        }
    }
}
