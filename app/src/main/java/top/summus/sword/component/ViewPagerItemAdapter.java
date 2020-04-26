/**
 * Copyright (C) 2015 ogaclejapan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.summus.sword.component;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

import lombok.Getter;
import lombok.Setter;

public class ViewPagerItemAdapter extends PagerAdapter {

    private final ViewPagerItems pages;
    @Getter
    @Setter
    private HashMap<Integer, View> holder;
    private final LayoutInflater inflater;


    public ViewPagerItemAdapter(ViewPagerItems pages) {
        this.pages = pages;
        this.holder = new HashMap<>();
        this.inflater = LayoutInflater.from(pages.getContext());
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = getPagerItem(position).initiate(inflater, container);
        container.addView(view);
        holder.put(position, view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        holder.remove(position);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getPagerItem(position).getTitle();
    }

    @Override
    public float getPageWidth(int position) {
        return getPagerItem(position).getWidth();
    }

    public View getPage(int position) {
        return holder.get(position);
    }

    protected ViewPagerItem getPagerItem(int position) {
        return pages.get(position);
    }
}
