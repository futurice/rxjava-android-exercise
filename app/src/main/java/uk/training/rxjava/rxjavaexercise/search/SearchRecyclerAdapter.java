package uk.training.rxjava.rxjavaexercise.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< 40678e70a55a37ac17076f6d31feb05ce4de1e25
<<<<<<< bd57120e5cb5d331d1ffcd017ad9a05fb24d5b3b
=======
import android.widget.ImageButton;
>>>>>>> error handling
=======
>>>>>>> implementation retryWhen
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.training.rxjava.rxjavaexercise.R;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 15/08/2016.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolderItem> {
    private static final String TAG = SearchRecyclerAdapter.class.getSimpleName();
    private List<SearchActivity.InfoDisplay> currentListItems = new ArrayList<>();

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        holder.getTextViewName().setText(currentListItems.get(position).getTitle());
        holder.getTextViewOwner().setText("Forks Count: " + currentListItems.get(position).getForkCount());
        if (currentListItems.get(position).getBitmap() != null) {
            holder.getItemImageView().setImageBitmap(currentListItems.get(position).getBitmap());
        } else {
            holder.getItemImageView()
                    .setImageDrawable(holder.getItemImageView().getResources().getDrawable(R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return currentListItems.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView itemNameTv;
        TextView itemForksCountTv;
        ImageView itemImageView;

        public ViewHolderItem(View view) {
            super(view);
            itemNameTv = (TextView) view.findViewById(R.id.item_title);
            itemForksCountTv =  (TextView) view.findViewById(R.id.item_owner);
            itemImageView = (ImageView) view.findViewById(R.id.item_picture);
        }

        public TextView getTextViewName() {
            return itemNameTv;
        }
        public TextView getTextViewOwner() {
            return itemForksCountTv;
        }

        public ImageView getItemImageView() {
            return itemImageView;
        }
    }

    public void refreshList(List<SearchActivity.InfoDisplay> infoDisplay) {
        Logger.v(TAG, "refresh the list: " + Logger.getSafeSize(infoDisplay));

        currentListItems = infoDisplay;
        notifyDataSetChanged();
    }

    public List<SearchActivity.InfoDisplay> getList() {
        return currentListItems;
    }
}
