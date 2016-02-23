package cn.fangcunjian.rxokhttp.sample.newgame;

import android.os.Bundle;

import cn.fangcunjian.rxokhttp.sample.R;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseActivity;

/**
 * Create by Mcin on 16/2/23
 */
public class NewGameListActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_new_game_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new NewGameListFragment())
                    .commit();
        }
    }


}
