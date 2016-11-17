package com.mcx.footerviewforrecyclerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcx.footerheaderforrecyclerview.RecyclerViewFooter;
import com.mcx.footerheaderforrecyclerview.RecyclerViewHeader;
import com.mcx.footerviewforrecyclerview.R;
import com.mcx.footerviewforrecyclerview.adapter.ColorItemsAdapter;

public class ListReversedFragment extends Fragment {

    public static ListReversedFragment newInstance() {
        return new ListReversedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_reversed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recycler.setAdapter(new ColorItemsAdapter(getActivity(), 21));

        RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) view.findViewById(R.id.header);
        recyclerHeader.attachTo(recycler);

        RecyclerViewFooter recyclerFooter = (RecyclerViewFooter) view.findViewById(R.id.footer);
        recyclerFooter.attachTo(recycler);
    }

}
