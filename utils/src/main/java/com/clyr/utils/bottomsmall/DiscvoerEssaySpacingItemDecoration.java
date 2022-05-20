package com.clyr.utils.bottomsmall;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lzy of clyr on 2022/05/20.
 */

public class DiscvoerEssaySpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public DiscvoerEssaySpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (0 == position) {
            outRect.left = 0;
        }
        outRect.right = spacing;
        super.getItemOffsets(outRect, view, parent, state);
    }
}
