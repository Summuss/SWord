package top.summus.sword.dagger;

import javax.inject.Singleton;

import dagger.Component;

import top.summus.sword.activity.StartActivity;
import top.summus.sword.network.HttpModule;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.TimeHttpService;
import top.summus.sword.room.RoomModule;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.viewmodel.BookNodeViewModel;

@Component(modules = {SingletonModule.class, HttpModule.class, RoomModule.class})
@Singleton
public interface AppComponent {
    void inject(BookNodeViewModel bookNodeViewModel);

    void inject(StartActivity startActivity);

    void inject(TimeHttpService timeHttpService);

    void inject(BookNodeHttpService bookNodeHttpService);


    void inject(BookNodeRoomService bookNodeRoomService);
}
