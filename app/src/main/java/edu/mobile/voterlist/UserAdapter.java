package edu.mobile.voterlist;

import android.content.Context;
import android.media.tv.PesRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

import edu.mobile.voterlist.model.Person;

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

        TextView userNameView = convertView.findViewById(R.id.userName);
        userNameView.setText(person.getName());

        TextView userEpicNumber = convertView.findViewById(R.id.userEpicNumber);
        userEpicNumber.setText(person.getEpicNumber());

        return convertView;
    }
}
