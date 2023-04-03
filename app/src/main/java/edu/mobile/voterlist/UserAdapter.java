package edu.mobile.voterlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.tv.PesRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mobile.voterlist.api.DataFileApi;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends ArrayAdapter<Person> {

    public UserAdapter(Context context, List<Person> personList) {
        super(context, 0, personList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        Person person = getItem(position);
        CircleImageView imageView = convertView.findViewById(R.id.userImageView);

        int imageId = (int) person.getImageId().getId();
        RetrofitService retrofitService = new RetrofitService();
        DataFileApi dataFileApi = retrofitService.getRetrofit().create(DataFileApi.class);

        dataFileApi.getUserProfileImage(imageId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        TextView userNameView = convertView.findViewById(R.id.userName);
        userNameView.setText(person.getName());

        TextView userEpicNumber = convertView.findViewById(R.id.userEpicNumber);
        userEpicNumber.setText(person.getEpicNumber());

        return convertView;
    }
}
