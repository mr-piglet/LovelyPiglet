package org.mrpiglet.lovelypiglet;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mrpiglet on 28.10.17..
 */

public class CheckedItemsAdapter extends RecyclerView.Adapter<CheckedItemsAdapter.CheckedItemsHolder> {

    private static final String TAG = CheckedItemsAdapter.class.getSimpleName();

    private int numberOfItems;

    public CheckedItemsAdapter(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }


    @Override
    public CheckedItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.text_holder_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        CheckedItemsHolder viewHolder = new CheckedItemsHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }

    @Override
    public void onBindViewHolder(CheckedItemsHolder holder, int position) {
        holder.bind(position); //call method of CheckedItemsHolder and bind with data
    }

    //class that holds one item
    class CheckedItemsHolder extends RecyclerView.ViewHolder {
        TextView checkedDateTextView;
        TextView descriptionTextView;
        //ImageView checkMarkImageView;

        CheckedItemsHolder(View itemView) {
            super(itemView);

            checkedDateTextView = (TextView) itemView.findViewById(R.id.tv_checked_date);
            descriptionTextView = (TextView) itemView.findViewById(R.id.tv_description);
            //checkMarkImageView = (ImageView) itemView.findViewById(R.id.iv_check_sign);
        }

        void bind(int listIndex) {
            //dummy data, later to be replaced with database data
            descriptionTextView.setText(Integer.toString(listIndex));
            checkedDateTextView.setText(Integer.toString(listIndex));

        }
    }
}
