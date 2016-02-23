package cn.fangcunjian.rxokhttp.sample.newgame;

import javax.inject.Singleton;

import cn.fangcunjian.rxokhttp.sample.dagger.AppComponent;
import cn.fangcunjian.rxokhttp.sample.dagger.SimpleModule;
import dagger.Component;

/**
 * Created by Mcin on 16/2/23.
 */

@Singleton
@Component(modules = SimpleModule.class,
        dependencies = AppComponent.class)
public interface NewGameComponent {
    public NewGamePresenter presenter();
}
