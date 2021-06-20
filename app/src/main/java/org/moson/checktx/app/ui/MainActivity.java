package org.moson.checktx.app.ui;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.moson.checktx.R;
import org.moson.checktx.app.utils.SystemUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NavController mController;
    private NavController.OnDestinationChangedListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupActionBarWithNavController(this, mController);
        mListener = (controller, destination, arguments) -> {
            Log.i(TAG, "onDestinationChanged: id = " + destination.getId());
        };
        mController.addOnDestinationChangedListener(mListener); //添加监听
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.removeOnDestinationChangedListener(mListener); //移除监听
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mController.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mController.navigateUp();
    }
}
