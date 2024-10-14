package com.example.indegenousmedicine2;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactInfoFragment extends Fragment {

    private UserInfoViewModel userInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);

        EditText editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        EditText editTextPan = view.findViewById(R.id.editTextPan);

        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);
            v.startAnimation(animation);

            userInfoViewModel.getUserInfo().setPhoneNumber(editTextPhoneNumber.getText().toString().trim());
            userInfoViewModel.getUserInfo().setPanNumber(editTextPan.getText().toString().trim());

            // Get the activity to display the toast
            UserInfoActivityDummy activity = (UserInfoActivityDummy) getActivity();
            if (activity != null) {
                activity.displayUserInfo();
            }
        });

//        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
//        buttonSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);
//                v.startAnimation(animation);
//                // Handle submit action
//                // For example, collect data and submit to server
//                Toast.makeText(getContext(), "Submit clicked", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        return view;    }
}
