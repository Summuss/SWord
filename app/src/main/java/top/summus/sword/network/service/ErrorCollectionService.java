package top.summus.sword.network.service;

import android.util.Log;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import top.summus.sword.exception.ErrorItem;
import top.summus.sword.exception.WrongStatusCodeException;

public class ErrorCollectionService {
    private static final String TAG = "ErrorCollectionService1";
    private List<ErrorItem> errorItemList = new ArrayList<>();

    private synchronized void add(ErrorItem errorItem) {
        errorItemList.add(errorItem);
    }

    public void addThrowable(Throwable throwable, String process, String message) {
        ErrorItem errorItem = ErrorItem.builder().throwable(throwable).process(process).message(message).build();
        if (!(throwable instanceof WrongStatusCodeException) || ((WrongStatusCodeException) throwable).getCode() != 406) {
            add(errorItem);

        }
    }


    public WrongStatusCodeException addWrongStatusCodeError(int code, String process) {
        WrongStatusCodeException statusCodeException = new WrongStatusCodeException(code);
        ErrorItem errorItem = ErrorItem.builder().throwable(statusCodeException).process(process)
                .message("error code " + code).build();
        if (code != 406) {
            add(errorItem);
        }
        return statusCodeException;
    }

    public boolean hasConnectOut() {
        for (ErrorItem errorItem : errorItemList) {
            if (errorItem.getThrowable() instanceof SocketTimeoutException) {
                Log.i(TAG, "hasConnectOut: true");
                return true;
            }
        }
        Log.i(TAG, "hasConnectOut: false");
        return false;

    }

    public boolean isNoError() {
        return errorItemList.size() == 0 ? true : false;
    }

    public void clear(){
        errorItemList.clear();
    }

    public void printAll() {
        for (ErrorItem errorItem : errorItemList) {
            Log.e(TAG, errorItem.toString());
        }
    }
}
