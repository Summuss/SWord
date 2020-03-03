package top.summus.sword.util;

import android.widget.TextView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.http.GET;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class Box<T> {
    private T value;

}
