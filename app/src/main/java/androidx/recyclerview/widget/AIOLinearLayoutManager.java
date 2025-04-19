package androidx.recyclerview.widget;

public class AIOLinearLayoutManager extends RecyclerView.LayoutManager {
    public int findFirstVisibleItemPosition() {
        return 0;
    }
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
}
