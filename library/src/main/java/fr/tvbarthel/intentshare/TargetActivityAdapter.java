package fr.tvbarthel.intentshare;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Used to adapt {@link TargetActivityView} inside a list.
 */
class TargetActivityAdapter extends RecyclerView.Adapter<TargetActivityAdapter.ViewHolder> {

    /**
     * View types.
     */
    private static final int VIEW_TYPE_HEADER = 0x00000001;
    private static final int VIEW_TYPE_RAW = 0x00000002;
    private final String label;

    /**
     * Target activity info adapted.
     */
    private List<TargetActivity> targetActivities;

    /**
     * Internal listener used to catch the events from the {@link TargetActivityView} adapted.
     */
    private TargetActivityView.Listener internalTargetActivityViewListener;

    /**
     * Listener used to catch adapted views events.
     */
    private Listener listener;

    /**
     * Used to adapt {@link TargetActivityView} inside a list.
     *
     * @param targetActivities list of target activities.
     * @param label            label to display as an header of the list.
     */
    public TargetActivityAdapter(final List<TargetActivity> targetActivities, String label) {
        this.targetActivities = targetActivities;
        this.label = label;
        internalTargetActivityViewListener = new TargetActivityView.Listener() {
            @Override
            public void onTargetActivitySelected(TargetActivity targetActivity) {
                if (listener != null) {
                    listener.onTargetActivitySelected(targetActivity);
                }
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                TargetActivityHeaderView headerView = new TargetActivityHeaderView(parent.getContext());
                headerView.setLayoutParams(layoutParams);
                return new ViewHolder(headerView);
            case VIEW_TYPE_RAW:
                TargetActivityView targetActivityView = new TargetActivityView(parent.getContext());
                targetActivityView.setLayoutParams(layoutParams);
                targetActivityView.setListener(internalTargetActivityViewListener);
                return new ViewHolder(targetActivityView);
            default:
                throw new IllegalStateException("Can't create view "
                        + "holder for the given view type : " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                ((TargetActivityHeaderView) holder.itemView).setModel(label);
                break;
            case VIEW_TYPE_RAW:
                // -1 for the header.
                ((TargetActivityView) holder.itemView).setModel(targetActivities.get(position - 1));
                break;
            default:
                throw new IllegalStateException("Can't bind view "
                        + "holder for view type : " + holder.getItemViewType());
        }
    }


    @Override
    public int getItemCount() {
        // +1 for the header
        return targetActivities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_RAW;
        }
    }

    /**
     * Listener used to catch events from the adapted views.
     *
     * @param listener listener to register.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * View holder pattern.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * View holder.
         *
         * @param itemView {@link TargetActivityView}
         */
        public ViewHolder(TargetActivityView itemView) {
            super(itemView);
        }

        /**
         * View holder.
         *
         * @param headerView {@link TargetActivityHeaderView}
         */
        public ViewHolder(TargetActivityHeaderView headerView) {
            super(headerView);
        }
    }

    /**
     * Listener used to catch events from adapted views.
     */
    public interface Listener {
        /**
         * Called when the user has chosen a target activity for the initial share intent.
         *
         * @param targetActivity chosen target activity.
         */
        void onTargetActivitySelected(TargetActivity targetActivity);
    }
}
