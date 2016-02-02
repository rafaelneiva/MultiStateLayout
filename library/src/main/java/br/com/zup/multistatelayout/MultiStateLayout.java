package br.com.zup.multistatelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by rafaelneiva on 07/08/15.
 */
public class MultiStateLayout extends FrameLayout {
    private static final int UNKNOWN_VIEW = -1;

    private static final int CONTENT_VIEW = 0;

    private static final int ERROR_VIEW = 1;

    private static final int EMPTY_VIEW = 2;

    private static final int LOADING_VIEW = 3;

    public enum State {
        CONTENT,
        LOADING,
        EMPTY,
        ERROR
    }

    private LayoutInflater mInflater;

    private View mContentView;

    private View mLoadingView;

    private View mErrorView;

    private View mEmptyView;

    private State mViewState = State.CONTENT;

    public MultiStateLayout(Context context) {
        this(context, null);
    }

    public MultiStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateLayout);

        int loadingViewResId = a.getResourceId(R.styleable.MultiStateLayout_msl_loadingLayout, -1);
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }

        int emptyViewResId = a.getResourceId(R.styleable.MultiStateLayout_msl_emptyLayout, -1);
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }

        int errorViewResId = a.getResourceId(R.styleable.MultiStateLayout_msl_errorLayout, -1);
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        }

        int viewState = a.getInt(R.styleable.MultiStateLayout_msl_viewLayout, UNKNOWN_VIEW);
        if (viewState != UNKNOWN_VIEW) {
            switch (viewState) {
                case CONTENT_VIEW:
                    mViewState = State.CONTENT;
                    break;

                case ERROR_VIEW:
                    mViewState = State.EMPTY;
                    break;

                case EMPTY_VIEW:
                    mViewState = State.EMPTY;
                    break;

                case LOADING_VIEW:
                    mViewState = State.LOADING;
                    break;
            }
        }

        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");

        setView(true);
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    /**
     * Returns the {@link View} associated with the {@link State}
     *
     * @param state The {@link State} with to return the view for
     * @return The {@link View} associated with the {@link State}, null if no view is present
     */
    @Nullable
    public View getView(State state) {
        switch (state) {
            case LOADING:
                return mLoadingView;

            case CONTENT:
                return mContentView;

            case EMPTY:
                return mEmptyView;

            case ERROR:
                return mErrorView;

            default:
                return null;
        }
    }

    /**
     * Returns the current {@link State}
     *
     * @return
     */
    public State getViewState() {
        return mViewState;
    }

    /**
     * Sets the current {@link State}
     *
     * @param state The {@link State} to set {@link MultiStateLayout} to
     */
    public void setViewState(State state) {
        if (state != mViewState) {
            mViewState = state;
            setView(false);
        }
    }

    /**
     * Sets the current {@link State}
     *
     * @param state The {@link State} to set {@link MultiStateLayout} to
     */
    public void setViewState(State state, boolean hideContent) {
        if (state != mViewState) {
            mViewState = state;
            setView(hideContent);
        }
    }

    /**
     * Shows the {@link View} based on the {@link State}
     */
    private void setView(boolean hideContent) {
        switch (mViewState) {
            case LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }

                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null) {
                    if (hideContent) {
                        mContentView.setVisibility(View.GONE);
                    } else {
                        mContentView.setVisibility(VISIBLE);
                    }
                }
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);

                mLoadingView.bringToFront();

                this.requestLayout();
                this.invalidate();
                mLoadingView.requestLayout();
                mLoadingView.invalidate();
                mContentView.invalidate();
                mContentView.requestLayout();

                break;

            case EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }

                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;

            case ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error View");
                }

                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case CONTENT:
            default:
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw new NullPointerException("Content View");
                }

                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Checks if the given {@link View} is valid for the Content View
     *
     * @param view The {@link View} to check
     * @return
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }

    /**
     * Sets the view for the given view state
     *
     * @param view          The {@link View} to use
     * @param state         The {@link State}to set
     * @param switchToState If the {@link State} should be switched to
     */
    public void setViewForState(View view, State state, boolean switchToState) {
        switch (state) {
            case LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;

            case ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;

            case CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if (switchToState) setViewState(state, true);
    }

    /**
     * Sets the {@link View} for the given {@link State}
     *
     * @param view  The {@link View} to use
     * @param state The {@link State} to set
     */
    public void setViewForState(View view, State state) {
        setViewForState(view, state, false);
    }

    /**
     * Sets the {@link View} for the given {@link State}
     *
     * @param layoutRes     Layout resource id
     * @param state         The {@link State} to set
     * @param switchToState If the {@link State} should be switched to
     */
    public void setViewForState(@LayoutRes int layoutRes, State state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    /**
     * Sets the {@link View} for the given {@link State}
     *
     * @param layoutRes Layout resource id
     * @param state     The {@link View} state to set
     */
    public void setViewForState(@LayoutRes int layoutRes, State state) {
        setViewForState(layoutRes, state, false);
    }
}