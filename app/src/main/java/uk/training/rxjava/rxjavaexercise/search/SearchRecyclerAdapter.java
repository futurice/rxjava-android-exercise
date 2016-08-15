package uk.training.rxjava.rxjavaexercise.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.training.rxjava.rxjavaexercise.R;
import uk.training.rxjava.rxjavaexercise.search.pojo.GitHubRepository;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 15/08/2016.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolderItem> {
    private static final String TAG = SearchRecyclerAdapter.class.getSimpleName();
    private List<GitHubRepository> currentListItems = new ArrayList<>();

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        holder.getTextViewName().setText(currentListItems.get(position).getName());
        holder.getTextViewOwner().setText("Forks Count: " + currentListItems.get(position).getForksCount());
    }

    @Override
    public int getItemCount() {
        return currentListItems.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView itemNameTv;
        TextView itemForksCountTv;

        public ViewHolderItem(View view) {
            super(view);
            itemNameTv = (TextView) view.findViewById(R.id.item_title);
            itemForksCountTv =  (TextView) view.findViewById(R.id.item_owner);
        }

        public TextView getTextViewName() {
            return itemNameTv;
        }
        public TextView getTextViewOwner() {
            return itemForksCountTv;
        }
    }

    public void refreshList(List<GitHubRepository> infoDisplay) {
        Logger.v(TAG, "refresh the list: " + Logger.getSafeSize(infoDisplay));

        currentListItems = infoDisplay;
        notifyDataSetChanged();
    }

    public List<GitHubRepository> getList() {
        return currentListItems;
    }
}
