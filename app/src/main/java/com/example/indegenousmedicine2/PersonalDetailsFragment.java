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

public class PersonalDetailsFragment extends Fragment {

    private UserInfoViewModel userInfoViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextAge = view.findViewById(R.id.editTextAge);
        EditText editTextAadhar = view.findViewById(R.id.editTextAadhar);

        view.findViewById(R.id.buttonNext).setOnClickListener(v -> {
            userInfoViewModel.getUserInfo().setName(editTextName.getText().toString().trim());
            userInfoViewModel.getUserInfo().setAge(editTextAge.getText().toString().trim());
            userInfoViewModel.getUserInfo().setAadharNumber(editTextAadhar.getText().toString().trim());
            // Replace fragment with the next fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new AddressFragment())
                    .addToBackStack(null)
                    .commit();
        });

//        Button buttonNext = view.findViewById(R.id.buttonNext);
//        buttonNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);
//                v.startAnimation(animation);
//
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, new AddressFragment())
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        return view;
    }
}
