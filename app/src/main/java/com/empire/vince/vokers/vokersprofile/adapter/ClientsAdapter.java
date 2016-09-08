package com.empire.vince.vokers.vokersprofile.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vince.vokers.vokersprofile.R;
import com.empire.vince.vokers.vokersprofile.models.Project;
import com.empire.vince.vokers.vokersprofile.models.Tag;
import com.empire.vince.vokers.vokersprofile.util.TagGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.empire.vince.vokers.vokersprofile.util.UtilMethods.browseUrl;

/**
 * Created by VinceGee on 09/07/2016.
 */
public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder>  {

    private ValueAnimator mAnimator;
    private int updatePosition = -1;
    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private Context mContext = null;
    private List<Project> projectList;

    public ClientsAdapter(Context context,List<Project> projectList) {
        mContext = context;
        this.projectList = projectList;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_clients, null);
        ClientsViewHolder vh = new ClientsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ClientsViewHolder holder, final int position) {

        holder.tagGroup.setTags(projectList.get(position).getTagList());
        holder.tagGroup.setOnTagClickListener(mTagClickListener);
        holder.companyNameTv.setText(projectList.get(position).getClientName());
        holder.delegateNameTv.setText(projectList.get(position).getDelegateName());
        holder.countryTag.setText(projectList.get(position).getCountryName());
        holder.clientImgView.setImageResource(projectList.get(position).getImage());

        if(projectList.get(position).getIsExpanded()==0) {
            holder.extendedView.setVisibility(View.GONE);
        } else {
            holder.extendedView.setVisibility(View.VISIBLE);
        }

        holder.clientsItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibilityState(position,holder);
            }
        });
    }

    private void changeVisibilityState(int position, final ClientsViewHolder holder) {
        updatePosition = position;
        if(projectList.get(position).getIsExpanded()==0) {
            Log.v("logCheck","clicked position: "+position+"\n"+" action:expand"+" value:"+holder.companyNameTv.getText().toString());
            expand(position,holder.extendedView);
        } else {
            Log.v("logCheck","clicked position: "+position+"\n"+" action:collapse"+" value:"+holder.companyNameTv.getText().toString());
            collapse(position, holder.extendedView);
        }
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    class ClientsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout clientsItemView;
        private RelativeLayout extendedView;
        private TextView companyNameTv;
        private TextView delegateNameTv;
        private TextView countryTag;
        private TagGroup tagGroup;
        private CircleImageView clientImgView;
        private ImageView arrowImgView;

        public ClientsViewHolder(View itemView) {
            super(itemView);
            clientsItemView = (LinearLayout) itemView.findViewById(R.id.clientsItemView);
            extendedView = (RelativeLayout) itemView.findViewById(R.id.extendedView);
            tagGroup = (TagGroup)itemView.findViewById(R.id.tagGroup);
            companyNameTv = (TextView)itemView.findViewById(R.id.companyNameTv);
            delegateNameTv = (TextView)itemView.findViewById(R.id.delegateNameTv);
            countryTag = (TextView)itemView.findViewById(R.id.countryTag);
            clientImgView = (CircleImageView) itemView.findViewById(R.id.clientImgView);
            arrowImgView = (ImageView)itemView.findViewById(R.id.arrowImgView);
        }
    }

    private void expand(final int position, View view) {
        view.measure(widthSpec, heightSpec);
        mAnimator = slideAnimator(view, 0, view.getMeasuredHeight());
        projectList.get(position).setIsExpanded(1);
        notifyDataSetChanged();
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private void collapse(final int position, final View view) {
        int finalHeight = view.getHeight();
        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                projectList.get(position).setIsExpanded(0);
                notifyDataSetChanged();
            }
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String text) {

        }

        @Override
        public void onTagClick(Tag tag) {
            browseUrl(mContext, tag.getUrl());
        }
    };
}
