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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mobile.voterlist.api.AssemblyApi;
import edu.mobile.voterlist.api.DataFileApi;
import edu.mobile.voterlist.api.DistrictApi;
import edu.mobile.voterlist.api.PersonApi;
import edu.mobile.voterlist.api.StatesApi;
import edu.mobile.voterlist.model.Assembly;
import edu.mobile.voterlist.model.DataFileInfo;
import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.retrofit.RetrofitService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    // Declarations
    AutoCompleteTextView autoCompleteStateView, autoCompleteDistrictView, autoCompleteAssemblyView;
    String name, fatherName, phoneNo, aadhaar_number, getGender, dateOfBirth, dateOfJoining, epicNumber, address;
    int stateId, districtId, assemblyId, personId;
    EditText editName, editFatherName, editPhone, editEpic, editDob, editAadhaar, editJoining, editAddress;
    TextInputLayout datePicker;
    RadioGroup radioGroup;
    RadioButton maleTextView, femaleTextView, transTextView, genderBtn;
    Uri imageUri;
    Bitmap imageBitmap;
    Button browse;
    ImageButton submitBtn;
    ProgressBar progressBar;
    CircleImageView imageView;
    MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
    MaterialDatePicker<Long> materialDatePicker = builder.build();
    private ActivityResultLauncher<String> mGetContent;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = (isDarkTheme(getApplicationContext()) ? R.drawable.ic_launcher_back_light_foreground : R.drawable.ic_launcher_back_foreground);
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(!isDarkTheme(getApplicationContext())){
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
            }
        }

        // Assigning value to variable.
        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        editEpic = findViewById(R.id.editEpic);
        editAadhaar = findViewById(R.id.editAadhaar);
        radioGroup = findViewById(R.id.groupRadio);
        maleTextView = findViewById(R.id.radio_male);
        femaleTextView = findViewById(R.id.radio_female);
        transTextView = findViewById(R.id.radio_trans_gender);
        editDob = findViewById(R.id.editDateOfBirth);
        editJoining = findViewById(R.id.editJoining);
        autoCompleteStateView = findViewById(R.id.editState);
        autoCompleteDistrictView = findViewById(R.id.editDistrict);
        autoCompleteAssemblyView = findViewById(R.id.editAssembly);
        browse = findViewById(R.id.browse_btn);
        datePicker = findViewById(R.id.textLayoutDate);
        imageView = findViewById(R.id.user_image);
        submitBtn = findViewById(R.id.pushBtn);
        progressBar = findViewById(R.id.progress_circular);
        // Date picker Icon
        datePicker.setEndIconOnClickListener(this::onDatePicker);

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
        autoCompleteStateView.setOnItemClickListener((parent, view, position, id) -> {
            States sId = (States) parent.getItemAtPosition(position);
            stateId = sId.getId();
            loadDistrict(stateId);
        });

        autoCompleteDistrictView.setOnItemClickListener((adapterView, view, i, l) -> {
            District dId = (District) adapterView.getItemAtPosition(i);
            districtId = dId.getId();
            loadAssembly(districtId);
        });

        autoCompleteAssemblyView.setOnItemClickListener((adapterView, view, i, l) -> {
            Assembly aId = (Assembly) adapterView.getItemAtPosition(i);
            assemblyId = aId.getId();
        });

        browse.setOnClickListener(view -> openGallery());
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

    private void sendUri(Bitmap bitmap, Uri uri) {
        imageUri = uri;
        imageBitmap = bitmap;
    }

    private void openGallery() {
        mGetContent.launch("image/*");
    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);
        getGender = genderBtn.getText().toString();
    }

    public void onDatePicker(View view) {
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = format.format(calendar.getTime());
            editDob.setText(formattedDate);
        });
    }

    private void loadStates() {
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);

        statesApi.getAllStates()
                .enqueue(new Callback<List<States>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<States>> call, @NonNull Response<List<States>> response) {
                        if(response.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            List<States> states = response.body();
                            ArrayAdapter<States> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, states);
                            autoCompleteStateView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<States>> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "states load failed..", Toast.LENGTH_SHORT).show();
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

    private void loadAssembly(int districtId) {
        RetrofitService retrofitService = new RetrofitService();
        AssemblyApi assemblyApi = retrofitService.getRetrofit().create(AssemblyApi.class);

        progressBar.setVisibility(View.VISIBLE);
        assemblyApi.getIdByName(districtId)
                .enqueue(new Callback<List<Assembly>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Assembly>> call, @NonNull Response<List<Assembly>> response) {
                        if(response.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            List<Assembly> assemblyList = response.body();
                            ArrayAdapter<Assembly> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, assemblyList);
                            autoCompleteAssemblyView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Assembly>> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Assembly fetch failed....", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void savePerson() {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        Person person = new Person();

        person.setName(name);
        person.setGender(getGender);
        person.setDateOfBirth(dateOfBirth);
        person.setDateOfJoining(dateOfJoining);
        person.setFatherName(fatherName);
        person.setPhoneNumber(phoneNo);
        person.setAadhaarNumber(aadhaar_number);
        person.setAddress(address);
        person.setStateId(stateId);
        person.setDistrictId(districtId);
        person.setAssemblyId(assemblyId);
        person.setEpicNumber(epicNumber);

        personApi.save(person).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call,@NonNull Response<Person> response) {
                Person personResponse = response.body();
                if(personResponse != null){
                    personId = personResponse.getId();
                    try {
                        saveImage(imageUri, personId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(MainActivity.this, "You've already register", Toast.LENGTH_SHORT).show();
                    submitBtn.setImageResource(R.drawable.failed_button);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Person> call,@NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "profile added failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImage(Uri uri, int pid) throws IOException {
        RetrofitService retrofitService = new RetrofitService();
        DataFileApi dataFileApi = retrofitService.getRetrofit().create(DataFileApi.class);

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
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);
        personApi.associateProfilePhoto(userId, profileId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Profile added successfully", Toast.LENGTH_SHORT).show();
                    resetPage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                personApi.deletePerson(userId).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        submitBtn.setImageResource(R.drawable.failed_button);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                    }
                });
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPage() {
        editName.setText("");
        radioGroup.clearCheck();
        editDob.setText("");
        editJoining.setText("");
        editFatherName.setText("");
        editPhone.setText("");
        editEpic.setText("");
        editAadhaar.setText("");
        editAddress.setText("");
        imageView.setBackgroundResource(R.drawable.photo_frame);
        imageView.setImageResource(R.drawable.ic_launcher_photo_foreground);
        autoCompleteStateView.setText("");
        autoCompleteStateView.clearFocus();
        autoCompleteDistrictView.setText("");
        autoCompleteDistrictView.clearFocus();
        autoCompleteAssemblyView.setText("");
        autoCompleteAssemblyView.clearFocus();
        editEpic.clearFocus();
    }

    public void validateForm(View view) {
        name = editName.getText().toString().trim();
        dateOfBirth = editDob.getText().toString();
        fatherName = editFatherName.getText().toString().trim();
        phoneNo = editPhone.getText().toString();
        address = editAddress.getText().toString();
        dateOfJoining = editJoining.getText().toString();
        aadhaar_number = editAadhaar.getText().toString();
        epicNumber = editEpic.getText().toString();
        int isImage = imageBitmap.getByteCount();

        if (isImage > 0 && !name.isEmpty() && !getGender.isEmpty() && !dateOfBirth.isEmpty() && !dateOfJoining.isEmpty() && !fatherName.isEmpty()
                && !phoneNo.isEmpty() && !address.isEmpty() && !aadhaar_number.isEmpty() && stateId != 0 && districtId != 0 && assemblyId != 0 && !epicNumber.isEmpty()) {

            savePerson();

        } else Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }

}