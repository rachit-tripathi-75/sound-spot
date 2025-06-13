package com.rachit.tripathi75.soundspot.interfaces;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
public class QueueItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;
    public QueueItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // No swipe flags are set as swiping is disabled.
        return makeMovementFlags(dragFlags, 0);
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Delegate the actual item movement logic to the adapter.
        return adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Not used in this implementation as isItemViewSwipeEnabled() returns false.
        // If swiping were enabled, the adapter's onItemDismiss() would be called here.
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // If the action state is not IDLE (i.e., it's being dragged or swiped),
        // notify the ViewHolder to update its appearance.
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // Notify the ViewHolder to revert its appearance to the normal state.
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }
}
