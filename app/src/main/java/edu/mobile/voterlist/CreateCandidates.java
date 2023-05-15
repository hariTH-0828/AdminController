package edu.mobile.voterlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mobile.voterlist.api.CandidateApi;
import edu.mobile.voterlist.api.DataFileApi;
import edu.mobile.voterlist.api.DistrictApi;
import edu.mobile.voterlist.api.PartiesApi;
import edu.mobile.voterlist.api.StatesApi;
import edu.mobile.voterlist.model.Candidate;
import edu.mobile.voterlist.model.Contact;
import edu.mobile.voterlist.model.DataFileInfo;
import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.Parties;
import edu.mobile.voterlist.model.PastPerformance;
import edu.mobile.voterlist.model.Promises;
import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.retrofit.RetrofitService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCandidates extends AppCompatActivity {

    AutoCompleteTextView autoCompleteStateView, autoCompleteDistrictView, autoCompletePartyName;
    String name, gender, qualification, age, phoneNumber, aadhaarNumber, promise, promiseStatus, email, description, positionHeld = "", voteGain = "";
    FrameLayout createCandidate;
    int stateId, districtId, candidateId, partiesId;
    CircleImageView imageView;
    Uri imageUri;
    Button browse;
    RadioGroup radioGroup;
    RadioButton maleTextView, femaleTextView, transTextView, genderBtn;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    private ActivityResultLauncher<String> mGetContent;
    EditText editName, editPhone, editAge, editAadhaar, editQualification, editMail, editDescription, editPositionHeld, editVoteGain;
    LinearLayout layout_promiseList;
    FrameLayout addFieldButton;
    View promiseView;

    List<String> list_status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidates);

        editName = findViewById(R.id.editCandidateName);
        radioGroup = findViewById(R.id.groupRadio);
        maleTextView = findViewById(R.id.radio_male);
        femaleTextView = findViewById(R.id.radio_female);
        transTextView = findViewById(R.id.radio_trans_gender);
        editPhone = findViewById(R.id.editPhone);
        editQualification = findViewById(R.id.editCandidateQualification);
        editAadhaar = findViewById(R.id.editAadhaar);
        editMail = findViewById(R.id.editMailId);
        layout_promiseList = findViewById(R.id.layout_promiseList);
        addFieldButton = findViewById(R.id.addFieldPromise);
        editAge = findViewById(R.id.editCandidateAge);
        editDescription = findViewById(R.id.editBackground);
        progressBar = findViewById(R.id.progress_circular);
        editPositionHeld = findViewById(R.id.editPositionHeld);
        editVoteGain = findViewById(R.id.editNumberOfVote);
        autoCompleteStateView = findViewById(R.id.editState);
        autoCompleteDistrictView = findViewById(R.id.editDistrict);
        autoCompletePartyName = findViewById(R.id.editParty);
        browse = findViewById(R.id.browse_btn);
        createCandidate = findViewById(R.id.createCandidate);
        imageView = findViewById(R.id.candidate_image);

        list_status.add("FULFILLED");
        list_status.add("NOT_FULFILLED");
        list_status.add("ON_PROGRESS");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = (isDarkTheme(getApplicationContext()) ? R.drawable.ic_launcher_back_light_foreground : R.drawable.ic_launcher_back_foreground);
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(!isDarkTheme(getApplicationContext())){
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
            }
        }

        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });

        createCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                            sendUri(bitmap, uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        loadStates();
        loadParties();
        addView();
        autoCompleteStateView.setOnItemClickListener((parent, view, position, id) -> {
            States sId = (States) parent.getItemAtPosition(position);
            stateId = sId.getId();
            loadDistrict(stateId);
        });

        autoCompleteDistrictView.setOnItemClickListener((parent, view, position, id) -> {
            District dId = (District) parent.getItemAtPosition(position);
            districtId = dId.getId();
        });

        autoCompletePartyName.setOnItemClickListener((adapterView, view, i, l) -> {
            Parties pId = (Parties) adapterView.getItemAtPosition(i);
            partiesId = pId.getId();
        });

        browse.setOnClickListener(view -> openGallery());
    }

    private void addView() {
        @SuppressLint("InflateParams") View promiseView = getLayoutInflater().inflate(R.layout.layout_promise_field, null, false);
        TextInputEditText editPromise = promiseView.findViewById(R.id.editPromise);
        AppCompatSpinner spinner = promiseView.findViewById(R.id.promiseStatus);
        FrameLayout removeFieldBtn = promiseView.findViewById(R.id.removeFieldPromise);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, list_status);
        spinner.setAdapter(statusAdapter);

        removeFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_promiseList.getChildCount() > 1){
                    removeField(promiseView);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot delete this field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layout_promiseList.addView(promiseView);
    }

    private void removeField(View view){
        layout_promiseList.removeView(view);
    }

    private void loadParties() {
        RetrofitService retrofitService = new RetrofitService();
        PartiesApi partiesApi = retrofitService.getRetrofit().create(PartiesApi.class);
        progressBar.setVisibility(View.VISIBLE);
        partiesApi.getAllParties().enqueue(new Callback<List<Parties>>() {
            @Override
            public void onResponse(@NonNull Call<List<Parties>> call,@NonNull Response<List<Parties>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    List<Parties> parties = response.body();
                    ArrayAdapter<Parties> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, parties);
                    autoCompletePartyName.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Parties>> call,@NonNull Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });

    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void openGallery() {
        mGetContent.launch("image/*");
    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);
        gender = genderBtn.getText().toString();
    }


    private void sendUri(Bitmap bitmap, Uri uri) {
        imageUri = uri;
        imageBitmap = bitmap;
    }

    private void loadStates() {
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);

        statesApi.getAllStates()
                .enqueue(new Callback<List<States>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<States>> call, @NonNull Response<List<States>> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            List<States> states = response.body();
                            ArrayAdapter<States> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, states);
                            autoCompleteStateView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<States>> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "states load failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDistrict(int stateId) {
        RetrofitService retrofitService = new RetrofitService();
        DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);
        progressBar.setVisibility(View.VISIBLE);
        districtApi.getDistrictByStateId(stateId)
                .enqueue(new Callback<List<District>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<District>> call, @NonNull Response<List<District>> response) {
                        if(response.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            List<District> districtList = response.body();
                            ArrayAdapter<District> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, districtList);
                            autoCompleteDistrictView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<District>> call,@NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "District fetch failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void validateForm() {
        name = editName.getText().toString().trim();
        age = editAge.getText().toString().trim();
        qualification = editQualification.getText().toString().trim();
        email = editMail.getText().toString().trim();
        phoneNumber = editPhone.getText().toString().trim();
        description = editDescription.getText().toString().trim();
        positionHeld = editPositionHeld.getText().toString().trim();
        voteGain = editVoteGain.getText().toString().trim();
        aadhaarNumber = editAadhaar.getText().toString();
        int isImage = imageBitmap.getByteCount();

        promiseView = layout_promiseList.getChildAt(0);
        setPromises(promiseView);

        if (isImage > 0 && !name.isEmpty() && !gender.isEmpty() && !age.isEmpty() && !qualification.isEmpty() && !email.isEmpty() && !promise.isEmpty() && !promiseStatus.isEmpty()
                && !phoneNumber.isEmpty() && !description.isEmpty() && stateId != 0 && districtId != 0 && partiesId != 0 && !aadhaarNumber.isEmpty()) {

            saveCandidate();

        } else Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }

    private void saveCandidate() {
        RetrofitService retrofitService = new RetrofitService();
        CandidateApi candidateApi = retrofitService.getRetrofit().create(CandidateApi.class);
        progressBar.setVisibility(View.VISIBLE);

        Contact contact = new Contact();
        contact.setPhoneNumber(phoneNumber);
        contact.setEmailId(email);

        PastPerformance pastPerformance = new PastPerformance();
        pastPerformance.setPositionHeld(positionHeld);
        pastPerformance.setNumberOfVote(Integer.parseInt(voteGain));

        List<Promises> promisesList = new ArrayList<>();
        for(int i = 0; i < layout_promiseList.getChildCount(); i++){
            promiseView = layout_promiseList.getChildAt(i);
            setPromises(promiseView);
            Promises promises = new Promises();
            promises.setDescription(promise);
            promises.setStatus(promiseStatus);

            promisesList.add(promises);
        }

        Candidate candidate = new Candidate();

        candidate.setName(name);
        candidate.setAge(Integer.parseInt(age));
        candidate.setAadhaarNumber(aadhaarNumber);
        candidate.setBackground(description);
        candidate.setGender(gender);
        candidate.setAverageRating(0);
        candidate.setDistrictId(districtId);
        candidate.setStateId(stateId);
        candidate.setContact(contact);
        candidate.setPartyName(autoCompletePartyName.getText().toString().trim());
        candidate.setQualification(qualification);
        candidate.setPromises(promisesList);
        candidate.setPastPerformance(pastPerformance);

        candidateApi.saveCandidates(candidate).enqueue(new Callback<Candidate>() {
            @Override
            public void onResponse(@NonNull Call<Candidate> call,@NonNull Response<Candidate> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Candidate candidateResponse = response.body();
                    if(candidateResponse != null){
                        candidateId = candidateResponse.getId();
                        try {
                            saveImage(imageUri, candidateId);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"This candidate is exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Candidate> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "profile added failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPromises(View promiseView){
        promise = "";
        promiseStatus = "";
        TextInputEditText getPromise = promiseView.findViewById(R.id.editPromise);
        AppCompatSpinner getStatus = promiseView.findViewById(R.id.promiseStatus);
        promise = Objects.requireNonNull(getPromise.getText()).toString().trim();
        promiseStatus = list_status.get(getStatus.getSelectedItemPosition());
    }

    private void saveImage(Uri uri, int pid) throws IOException {
        RetrofitService retrofitService = new RetrofitService();
        DataFileApi dataFileApi = retrofitService.getRetrofit().create(DataFileApi.class);
        progressBar.setVisibility(View.VISIBLE);
        // It will get the image path from the local storage
        File file = new File(uri.getPath());

        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
        byte[] compressedData = outputStream.toByteArray();

        InputStream compressedStream = new ByteArrayInputStream(compressedData);
        RequestBody requestBody = new InputStreamRequestBody(MediaType.parse(getContentResolver().getType(uri)), compressedStream);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        dataFileApi.save(imagePart).enqueue(new Callback<DataFileInfo>() {
            @Override
            public void onResponse(@NonNull Call<DataFileInfo> call,@NonNull Response<DataFileInfo> response) {
                DataFileInfo dataFileInfo = response.body();
                updateProfile(Objects.requireNonNull(dataFileInfo).getId(), pid);
            }

            @Override
            public void onFailure(@NonNull Call<DataFileInfo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Save Image failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(long profileId, int userId) {
        RetrofitService retrofitService = new RetrofitService();
        CandidateApi candidateApi = retrofitService.getRetrofit().create(CandidateApi.class);
        candidateApi.associateProfilePhoto(userId, profileId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Profile added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Profile photo upload failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
            }
        });
    }
}