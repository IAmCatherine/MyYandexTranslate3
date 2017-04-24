
package com.example.user.myyandextranslate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.user.myyandextranslate.model.Translate;
import com.example.user.myyandextranslate.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.CustomViewHolder> {

    private RealmResults<Translate> results;
    private Context context;
    private Realm realm;

    public DeleteAdapter(Context context, Realm realm, RealmResults<Translate> results) {
        this.context = context;
        this.realm = realm;
        this.results = results;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorites, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.favorite.setChecked(results.get(position).getFavorites());
        holder.word.setText(results.get(position).getText());
        holder.translate.setText(results.get(position).getTranslatedText());
        holder.direction.setText(results.get(position).getDirection());

    }

    public void setResults(RealmResults<Translate> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ToggleButton favorite;
        TextView word;
        TextView direction;
        TextView translate;

        public CustomViewHolder(View itemView) {
            super(itemView);

            favorite = (ToggleButton) itemView.findViewById(R.id.isFavorite);
            word = (TextView) itemView.findViewById(R.id.word);
            translate = (TextView) itemView.findViewById(R.id.translate);
            direction = (TextView) itemView.findViewById(R.id.direction);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            Translate translate = new Translate();
                            translate.setText(word.getText().toString());
                            translate.setFavorites(favorite.isChecked());
                            translate.setTranslatedText(CustomViewHolder.this.translate.getText().toString());
                            translate.setDirection(direction.getText().toString());

                            bgRealm.insertOrUpdate(translate);
                        }
                    });

                }
            });

        }

    }
}

