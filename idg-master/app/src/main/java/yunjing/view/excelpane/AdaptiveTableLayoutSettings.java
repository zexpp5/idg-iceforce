package yunjing.view.excelpane;

/**
 * Settings keeper class.
 */
class AdaptiveTableLayoutSettings {

    /**
     * Layout width
     */
    private int mLayoutWidth;
    /**
     * Layout height
     */
    private int mLayoutHeight;

    private boolean mIsHeaderFixed;

    private int mCellMargin;

    /**
     * if true - value of row header fixed to the row. Fixed to the data
     * if false - fixed to the number of row. Fixed to the row' number from 0 to n.
     */
    private boolean mSolidRowHeader;

    /**
     * If true, then the table can be edited, otherwise it is impossible
     */
    private boolean mDragAndDropEnabled;


    AdaptiveTableLayoutSettings() {
    }

    int getLayoutWidth() {
        return mLayoutWidth;
    }

    AdaptiveTableLayoutSettings setLayoutWidth(int layoutWidth) {
        mLayoutWidth = layoutWidth;
        return this;
    }

    int getLayoutHeight() {
        return mLayoutHeight;
    }

    AdaptiveTableLayoutSettings setLayoutHeight(int layoutHeight) {
        mLayoutHeight = layoutHeight;
        return this;
    }

    public boolean isHeaderFixed() {
        return mIsHeaderFixed;
    }

    public AdaptiveTableLayoutSettings setHeaderFixed(boolean headerFixed) {
        mIsHeaderFixed = headerFixed;
        return this;
    }

    public int getCellMargin() {
        return mCellMargin;
    }

    public AdaptiveTableLayoutSettings setCellMargin(int cellMargin) {
        mCellMargin = cellMargin;
        return this;
    }

    public boolean isSolidRowHeader() {
        return mSolidRowHeader;
    }

    public AdaptiveTableLayoutSettings setSolidRowHeader(boolean solidRowHeader) {
        mSolidRowHeader = solidRowHeader;
        return this;
    }

    public boolean isDragAndDropEnabled() {
        return mDragAndDropEnabled;
    }

    public void setDragAndDropEnabled(boolean dragAndDropEnabled) {
        mDragAndDropEnabled = dragAndDropEnabled;
    }
}
