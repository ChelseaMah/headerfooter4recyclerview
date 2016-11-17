/*
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcx.footerviewforrecyclerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcx.footerheaderforrecyclerview.RecyclerViewFooter;
import com.mcx.footerheaderforrecyclerview.RecyclerViewHeader;
import com.mcx.footerviewforrecyclerview.R;
import com.mcx.footerviewforrecyclerview.adapter.ColorItemsAdapter;

/**
 * Created by Bartosz Lipinski
 * 01.04.15
 */
public class GridRecyclerFragment extends Fragment {

    public static GridRecyclerFragment newInstance() {
        GridRecyclerFragment fragment = new GridRecyclerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new ColorItemsAdapter(getActivity(), 32));

        RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) view.findViewById(R.id.header);
        recyclerHeader.attachTo(recycler);

        RecyclerViewFooter recyclerFooter = (RecyclerViewFooter) view.findViewById(R.id.footer);
        recyclerFooter.attachTo(recycler);
    }

}
