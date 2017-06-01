# MultiStateLayout
Android layout with multiple states used to control when show content, loading, error or empty

It is base on https://github.com/Kennyc1012/MultiStateView

# Gradle dependency
    compile 'br.com.zup:multi-state-layout:1.0.3'

# Example
Add xml.
Use one xml for each layout: empty, error and loading.
The content is the view inside MultiStateLayout

    <br.com.zup.multistatelayout.MultiStateLayout
        android:id="@+id/multiStateLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:msl_emptyLayout="@layout/empty_view"
        app:msl_errorLayout="@layout/error_view"
        app:msl_loadingLayout="@layout/loading_view"
        app:msl_viewLayout="content">
        
                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="@string/lorem"/>
              
    </br.com.zup.multistatelayout.MultiStateLayout>
    
    
    Java
    MultiStateLayout multiStateLayout = (MultiStateLayout) findViewById(R.id.multiStateLayout);
    multiStateLayout.setViewState(MultiStateLayout.State.CONTENT);
    /* use enum
    public enum State {
        CONTENT,
        LOADING,
        EMPTY,
        ERROR
    }
    */
