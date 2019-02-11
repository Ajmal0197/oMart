package com.example.omart.interfaces;

import android.view.View;

public interface ItemClickListener
{
    //used in recycler view
    void onClick(View view, int position, boolean isLongClick);
}
