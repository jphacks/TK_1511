package tk11.jphacks.titech.view.fragment;

import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.activity.BindingActivity_;
import tk11.jphacks.titech.view.activity.CallActivity_;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @AfterViews
    void onAfterViews() {
    }

    @Click(R.id.main_button_binding)
    void movePager() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BindingActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.main_button_calling)
    void moveRecycler() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CallActivity_.class);
        startActivity(intent);
    }
}