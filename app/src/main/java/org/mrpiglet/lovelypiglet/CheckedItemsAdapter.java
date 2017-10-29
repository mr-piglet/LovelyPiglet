package org.mrpiglet.lovelypiglet;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mrpiglet.lovelypiglet.data.CheckedItemsContract;

import java.sql.Timestamp;
import java.util.Calendar;

class CheckedItemsAdapter extends RecyclerView.Adapter<CheckedItemsAdapter.CheckedItemsHolder> {

    private static final String TAG = CheckedItemsAdapter.class.getSimpleName();

    private Context myContext;
    private Cursor myCursor;

    CheckedItemsAdapter(Context context, Cursor cursor) {
        this.myContext = context;
        this.myCursor = cursor;
    }


    @Override
    public CheckedItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.text_holder_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new CheckedItemsHolder(view);
    }

    @Override
    public int getItemCount() {
        return myCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        // Always close the previous myCursor first!!!
        if (myCursor != null) myCursor.close();
        myCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(CheckedItemsHolder holder, int position) {
       if (!myCursor.moveToPosition(position)) return; //no data to display

        String description = myCursor.getString(myCursor.getColumnIndex(CheckedItemsContract.CheckedItemsEntry.COLUMN_DESCRIPTION_NAME));
        Timestamp itemTimestamp = Timestamp.valueOf(myCursor.getString(myCursor.getColumnIndex(CheckedItemsContract.CheckedItemsEntry.COLUMN_TIMESTAMP)));
        long id = myCursor.getLong(myCursor.getColumnIndex(CheckedItemsContract.CheckedItemsEntry._ID));

        long timestamp = itemTimestamp.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        String dayMonth = "" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH);

        holder.descriptionTextView.setText(description);
        holder.checkedDateTextView.setText(dayMonth);
        holder.itemView.setTag(id); //set database id of item to be able to delete it later, if needed!!

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
    }
}
