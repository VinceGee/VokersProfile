package com.empire.vince.vokers.vokersprofile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.models.Methodology;

import java.util.List;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class MethodologyAdapter extends RecyclerView.Adapter<MethodologyAdapter.MethodologyRowHolder> {
    private Context mContext;
    private List<Methodology> methodologyList;

    public MethodologyAdapter(Context context, List<Methodology> methodologyList) {
        mContext = context;
        this.methodologyList = methodologyList;
    }



    @Override
    public MethodologyRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_methodology_item, null);
        MethodologyRowHolder vh = new MethodologyRowHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MethodologyRowHolder holder, int position) {
        holder.methodologyItemIcon.setImageResource(getDrawableIdFromFileName(mContext,
                methodologyList.get(position).getImageId()));
        holder.methodologyItemTitle.setText(methodologyList.get(position).getTitle());
        holder.methodologyItemBody.setText(methodologyList.get(position).getBody());
        if(position == getItemCount()-1) {
            holder.methodologyItemBody.append("\n\n\n");
        }
    }

    @Override
    public int getItemCount() {
        return methodologyList.size();
    }

    public class MethodologyRowHolder extends RecyclerView.ViewHolder {
        private ImageView methodologyItemIcon;
        private TextView methodologyItemTitle;
        private TextView methodologyItemBody;

        public MethodologyRowHolder(View view) {
            super(view);
            methodologyItemIcon = (ImageView)view.findViewById(R.id.methodologyItemIcon);
            methodologyItemTitle = (TextView)view.findViewById(R.id.methodologyItemTitle);
            methodologyItemBody = (TextView)view.findViewById(R.id.methodologyItemBody);
        }
    }
}

