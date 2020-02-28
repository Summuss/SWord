package top.summus.sword.dagger;

import javax.inject.Singleton;

import dagger.Component;

import top.summus.sword.activity.StartActivity;
import top.summus.sword.viewmodel.BookNodeViewModel;

@Component(modules = {SingletonModule.class})
@Singleton
public interface AppComponent {
    void inject(BookNodeViewModel bookNodeViewModel);

    void inject(StartActivity startActivity);

}
