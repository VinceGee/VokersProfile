package com.empire.vince.vokers.vokersprofile.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.models.Project;
import com.empire.vince.vokers.vokersprofile.util.AITSRecyclerView;

import java.util.List;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class PortfolioAdapter extends AITSRecyclerView.Adapter<PortfolioAdapter.PortfolioListRowHolder>
        implements View.OnClickListener, View.OnLongClickListener{

    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private Context mContext = null;
    private List<Project> projectList;
    private int lastPosition = -1;
    private boolean isDataChanged = false;

    public PortfolioAdapter(Context context, List<Project> projectList) {
        mContext = context;
        this.projectList = projectList;
    }

    @Override
    public PortfolioListRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_portfolio_item, null);
        PortfolioListRowHolder vh = new PortfolioListRowHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PortfolioListRowHolder holder, final int position) {
        if(!isDataChanged) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
        if(projectList.get(position).getIsLiked() == 0) {
            holder.likeImageView.setImageResource(R.drawable.ic_heart_outline_grey);
        } else {
            holder.likeImageView.setImageResource(R.drawable.ic_heart_red);
        }

        holder.headingTv.setText(projectList.get(position).getTitle());
        holder.tagTv.setText(projectList.get(position).getPlatforms());
        holder.portfolioImageView.setImageResource(projectList.get(position).getImage());
        holder.holderPosition = position;
        holder.likeImageView.setTag(holder);
        holder.portfolioImageView.setTag(holder);
        holder.likeImageView.setOnClickListener(this);
        holder.portfolioImageView.setOnLongClickListener(this);
        if(position == projectList.size()-1) {
            isDataChanged = false;
        }
    }


    private void showHeartAnimation(final PortfolioListRowHolder holder) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.likeImageView, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.likeImageView, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                projectList.get(holder.holderPosition).setIsLiked(1);
                isDataChanged = true;
                holder.likeImageView.setImageResource(R.drawable.ic_heart_red);
//                notifyDataSetChanged();
            }
        });
        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void setFilterList(List<Project> filterList) {
        if(filterList!=null) {
            projectList = filterList;
//            isDataChanged = true;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.likeImageView) {
            showHeartAnimation((PortfolioListRowHolder) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.portfolioImageView) {
            showHeartAnimation((PortfolioListRowHolder) v.getTag());
        }
        return false;
    }

    public class PortfolioListRowHolder extends AITSRecyclerView.ViewHolder {
        private ImageView likeImageView;
        private ImageView portfolioImageView;
        private TextView headingTv;
        private TextView tagTv;
        private int holderPosition;

        public PortfolioListRowHolder(View view) {
            super(view);
            likeImageView = (ImageView)view.findViewById(R.id.likeImageView);
            portfolioImageView = (ImageView)view.findViewById(R.id.portfolioImageView);
            headingTv = (TextView)view.findViewById(R.id.headingTv);
            tagTv = (TextView) view.findViewById(R.id.tagTv);
        }
    }
}
