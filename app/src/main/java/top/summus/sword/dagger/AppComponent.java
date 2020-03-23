package top.summus.sword.dagger;

import javax.inject.Singleton;

import dagger.Component;

import top.summus.sword.activity.StartActivity;
import top.summus.sword.fragment.TestFragment;
import top.summus.sword.network.HttpModule;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.TimeHttpService;
import top.summus.sword.room.RoomModule;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.viewmodel.AddWordViewModel;
import top.summus.sword.viewmodel.BookNodeViewModel;
import top.summus.sword.viewmodel.WordDetailViewModel;
import top.summus.sword.viewmodel.WordViewModel;

@Component(modules = {SingletonModule.class, HttpModule.class, RoomModule.class})
@Singleton
public interface AppComponent {
    void inject(BookNodeViewModel bookNodeViewModel);

    void inject(StartActivity startActivity);

    void inject(TimeHttpService timeHttpService);

    void inject(BookNodeHttpService bookNodeHttpService);


    void inject(BookNodeRoomService bookNodeRoomService);

    void inject(TestFragment testFragment);

    void inject(DaggerContainer daggerContainer);

    void inject(WordViewModel wordViewModel);

    void inject(AddWordViewModel addWordViewModel);

    void inject(WordDetailViewModel wordDetailViewModel);
}
