package com.empire.vince.vokers.vokersprofile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.models.Member;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.getDrawableIdFromFileName;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private Context mContext;
    private List<Member> team;

    public TeamAdapter(Context context, List<Member> team) {
        mContext = context;
        this.team = team;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_team, null);
        TeamViewHolder vh = new TeamViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        holder.memberImgView.setImageResource(getDrawableIdFromFileName(mContext,
                team.get(position).getMemberImageId()));
        holder.memberNameView.setText(team.get(position).getMemberName());
        holder.memberDesignationView.setText(team.get(position).getMemberDesignation());
    }

    @Override
    public int getItemCount() {
        return team.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView memberImgView;
        private TextView memberNameView;
        private TextView memberDesignationView;

        public TeamViewHolder(View itemView) {
            super(itemView);
            memberImgView = (CircleImageView) itemView.findViewById(R.id.memberImgView);
            memberNameView = (TextView) itemView.findViewById(R.id.memberNameView);
            memberDesignationView = (TextView) itemView.findViewById(R.id.memberDesignationView);
        }
    }
}
