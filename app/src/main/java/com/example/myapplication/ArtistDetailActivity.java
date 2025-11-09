package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ArtistDetailActivity extends AppCompatActivity {

    private TextView tvName, tvGenres, tvStars, tvRating, tvPrice, tvBio, btnBack;
    private ImageView imgAvatar;
    private GridView gridSlots;
    private LinearLayout reviewsContainer;
    private SlotAdapter slotAdapter;
    private List<String> availableSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        initializeViews();
        setupData();
        setupListeners();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.tvName);
        tvGenres = findViewById(R.id.tvGenres);
        tvStars = findViewById(R.id.tvStars);
        tvRating = findViewById(R.id.tvRating);
        tvPrice = findViewById(R.id.tvPrice);
        tvBio = findViewById(R.id.tvBio);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        gridSlots = findViewById(R.id.gridSlots);
        reviewsContainer = findViewById(R.id.reviewsContainer);
    }

    private void setupData() {
        // Sample data - in real app, this would come from intent or database
        // Artist profile data
        String stageName = "Luna";
        String genres = "Pop • Ballad";
        double pricePerHour = 2.0;
        String location = "TP.HCM";
        int experienceYears = 5;
        String bio = "Nghệ sĩ pop nổi tiếng với giọng hát ngọt ngào và " + experienceYears + " năm kinh nghiệm biểu diễn.";

        tvName.setText(stageName);
        tvGenres.setText(genres + " • " + location);
        tvStars.setText("★★★★★");
        tvRating.setText(" 4.8");
        tvPrice.setText(String.format("%.1fM đ/giờ", pricePerHour));
        tvBio.setText(bio);

        // Sample available slots - in real app, this would come from artist_availabilities table
        availableSlots = new ArrayList<>();
        availableSlots.add("19:00 - 20:00");
        availableSlots.add("20:00 - 21:00");
        availableSlots.add("21:00 - 22:00");

        slotAdapter = new SlotAdapter(this, availableSlots);
        gridSlots.setAdapter(slotAdapter);

        // Sample reviews
        addSampleReviews();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addSampleReviews() {
        // Simplified review display without custom layout
        TextView review1 = new TextView(this);
        review1.setText("Nguyễn Văn A: ★★★★★ 5.0\nBuổi biểu diễn rất tuyệt vời! Luna hát rất hay và chuyên nghiệp.");
        review1.setPadding(8, 8, 8, 8);
        reviewsContainer.addView(review1);

        TextView review2 = new TextView(this);
        review2.setText("Trần Thị B: ★★★★☆ 4.0\nRất hài lòng với dịch vụ. Sẽ book lại lần sau.");
        review2.setPadding(8, 8, 8, 8);
        reviewsContainer.addView(review2);
    }
}
