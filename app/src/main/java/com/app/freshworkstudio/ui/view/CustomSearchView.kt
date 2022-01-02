package com.app.freshworkstudio.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnKeyListener
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.app.freshworkstudio.FreshWorkApp
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.SearchViewBinding
import com.app.freshworkstudio.model.GifData
import com.app.freshworkstudio.ui.adapter.GifSearchListAdapter
import com.app.freshworkstudio.ui.view.callBack.OnSearchCallBack
import com.app.freshworkstudio.ui.viewDataModels.SearchViewModel
import com.app.freshworkstudio.utils.AnimationUtils.circleHideView
import com.app.freshworkstudio.utils.AnimationUtils.circleRevealView
import com.app.freshworkstudio.utils.DataUtils
import com.app.freshworkstudio.utils.DataUtils.item
import com.app.freshworkstudio.utils.DataUtils.loading
import com.app.freshworkstudio.utils.binding.ViewUtils
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Mauker and Adam McNeilly on 30/03/2016. dd/MM/YY.
 * Maintained by Mauker, Adam McNeilly and our beautiful open source community <3
 * Based on stadiko on 6/8/15. https://github.com/krishnakapil/MaterialSeachView
 */
class CustomSearchView @JvmOverloads constructor(
    private val mContext: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttributes: Int = 0
) : FrameLayout(mContext, attributeSet) {


    private val EMPTY_STRING = ""

    // TODO adapter.updateAdapter(filtered) -- update the data
    /**
     * Determines if the search view is opened or closed.
     * @return True if the search view is open, false if it is closed.
     */
    /**
     * Whether or not the search view is open right now.
     */
    var isOpen = false
        private set

    /**
     * Whether or not the MaterialSearchView will animate into view or just appear.
     */
    private var mShouldAnimate = true

    /**
     * Whether or not the MaterialSearchView will close under a click on the Tint View (Blank Area).
     */
    private var mShouldCloseOnTintClick = false

    /**
     * Wheter to keep the search history or not.
     */
    private var mShouldKeepHistory = true

    /**
     * Flag for whether or not we are clearing focus.
     */
    private var mClearingFocus = false

    /**
     * Voice hint prompt text.
     */
    private lateinit var mHintPrompt: String

/*

    //region UI Elements
    */
    /**
     * The tint that appears over the search view.
     *//*

    private lateinit var mTintView: View

    */
    /**
     * The root of the search view.
     *//*

    private lateinit var dBinding.searchLayout: FrameLayout

    */
    /**
     * The bar at the top of the SearchView containing the EditText and ImageButtons.
     *//*

    private lateinit var dBinding.searchBar: LinearLayout

    */
    /**
     * The EditText for entering a search.
     *//*

    private lateinit var dBinding.etSearch: EditText

    */
    /**
     * The ImageButton for navigating back.
     *//*

    private lateinit var mBack: ImageButton


    */
    /**
     * The ImageButton for clearing the search text.
     *//*

    private lateinit var dBinding.actionClear: ImageButton

    */
    /**
     * The ListView for displaying suggestions based on the search.
     *//*

    private lateinit var mSuggestionsRecyclerView: RecyclerView
*/

    /**
     * Adapter for displaying suggestions.
     */
    lateinit var adapterSearch: GifSearchListAdapter
        private set

    /**
     * view data binding object
     */
    lateinit var dBinding: SearchViewBinding
        private set
    //endregion

    //region Query Properties
    /**
     * The previous query text.
     */
    private lateinit var mOldQuery: CharSequence

    /**
     * The current query text.
     */
    private lateinit var mCurrentQuery: CharSequence
    //endregion


    //region Initializers
    /**
     * Preforms any required initializations for the search view.
     */

    var searchViewModel: SearchViewModel? = null
        set(value) {

            field = value

            init()

            // Initialize style
            initStyle(attributeSet, defStyleAttributes)
        }

    private fun init() {
        // Inflate view
        //LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true)
        adapterSearch = GifSearchListAdapter(onAdapterPositionClicked(), onRetry())
        dBinding = SearchViewBinding.inflate(LayoutInflater.from(context), this, true)


        /* // Get items
         dBinding.searchLayout = findViewById(R.id.search_layout)
         mTintView = dBinding.searchLayout.findViewById(R.id.transparent_view)
         dBinding.searchBar = dBinding.searchLayout.findViewById(R.id.search_bar)
         mBack = dBinding.searchLayout.findViewById(R.id.action_back)
         dBinding.etSearch = dBinding.searchLayout.findViewById(R.id.et_search)
         dBinding.actionClear = dBinding.searchLayout.findViewById(R.id.action_clear)
         mSuggestionsRecyclerView = dBinding.searchLayout.findViewById(R.id.suggestion_list)*/

        // Set click listeners
        with(dBinding) {

            adapter = adapterSearch
            viewModel = searchViewModel

            actionBack.setOnClickListener { closeSearch() }
            actionClear.setOnClickListener { onClearClicked() }

            // Initialize the search view.
            initSearchView()




            suggestionList.adapter = adapter

        }
    }


    /*
    *  callback to save item in db for fav.
    * */
    private fun onAdapterPositionClicked(): (GifData) -> Unit {
        return { gifData ->

            // show dialog of gif to mark fav and unfav
            ViewUtils.showFavDialog(gifData, context, searchViewModel!!)
        }
    }


    var pageCount:Int = loading

    /*
    *  callback to retry emit last page due to recovery from error
    * */
    private fun onRetry(): (Int) -> Unit {
        return {

            if (FreshWorkApp.isInternetAvailable()) {
                searchViewModel.whatIfNotNull {
                    pageCount++
                    it.loadGifPage(
                        it.getCurrentPage().plus(pageCount)
                    )
                }
            } else {
                //showError(getString(R.string.error_msg_no_internet))
                adapterSearch.showErrorPage(context.getString(R.string.error_msg_no_internet))
            }
        }
    }

    /**
     * Initializes the style of this view.
     * @param attributeSet The attributes to apply to the view.
     * @param defStyleAttribute An attribute to the style theme applied to this view.
     */
    private fun initStyle(attributeSet: AttributeSet?, defStyleAttribute: Int) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val typedArray = mContext.obtainStyledAttributes(
            attributeSet,
            R.styleable.MaterialSearchView,
            defStyleAttribute,
            0
        )

        if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
            background = typedArray.getDrawable(R.styleable.MaterialSearchView_searchBackground)
        }

        if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColor)) {
            setTextColor(
                typedArray.getColor(
                    R.styleable.MaterialSearchView_android_textColor,
                    ContextCompat.getColor(mContext, R.color.black)
                )
            )
        }

        if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColorHint)) {
            setHintTextColor(
                typedArray.getColor(
                    R.styleable.MaterialSearchView_android_textColorHint,
                    ContextCompat.getColor(mContext, R.color.gray_50)
                )
            )
        }

        if (typedArray.hasValue(R.styleable.MaterialSearchView_android_hint)) {
            setHint(typedArray.getString(R.styleable.MaterialSearchView_android_hint))
        }


        if (typedArray.hasValue(R.styleable.MaterialSearchView_searchCloseIcon)) {
            setClearIcon(
                typedArray.getResourceId(
                    R.styleable.MaterialSearchView_searchCloseIcon,
                    R.drawable.ic_action_navigation_close
                )
            )
        }
        if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBackIcon)) {
            setBackIcon(
                typedArray.getResourceId(
                    R.styleable.MaterialSearchView_searchBackIcon,
                    R.drawable.ic_action_navigation_arrow_back
                )
            )
        }
        if (typedArray.hasValue(R.styleable.MaterialSearchView_searchSuggestionBackground)) {
            setSuggestionBackground(
                typedArray.getResourceId(
                    R.styleable.MaterialSearchView_searchSuggestionBackground,
                    R.color.search_layover_bg
                )
            )
        }

        /*if (typedArray.hasValue(R.styleable.MaterialSearchView_listTextColor)) {
            adapter.textColor = typedArray.getColor(
                R.styleable.MaterialSearchView_listTextColor,
                ContextCompat.getColor(mContext, R.color.white)
            )
        }*/

        if (typedArray.hasValue(R.styleable.MaterialSearchView_android_inputType)) {
            setInputType(
                typedArray.getInteger(
                    R.styleable.MaterialSearchView_android_inputType,
                    InputType.TYPE_CLASS_TEXT
                )
            )
        }
        if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBarHeight)) {
            setSearchBarHeight(
                typedArray.getDimensionPixelSize(
                    R.styleable.MaterialSearchView_searchBarHeight,
                    appCompatActionBarHeight
                )
            )
        } else {
            setSearchBarHeight(appCompatActionBarHeight)
        }

        fitsSystemWindows = false
        typedArray.recycle()

        // Show voice button. We put this here because whether or not it's shown is defined by a style above.

    }

    /**
     * Preforms necessary initializations on the SearchView.
     */
    private fun initSearchView() {

        dBinding.etSearch
        dBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                // When the text changes, filter
                searchViewModel.whatIfNotNull {
                    it.lastPageNumber = DataUtils.item
                    it.searchQuery = text.toString()
                    it.query.value = text.toString()

                }
                this@CustomSearchView.onTextChanged(text)
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isEmpty()) {
                    searchViewModel.whatIfNotNull {

                        it.isLoading = false

                        adapterSearch.clear()
                    }
                }
            }
        })
        dBinding.etSearch.onFocusChangeListener =
            OnFocusChangeListener { _, hasFocus -> // If we gain focus, show keyboard and show suggestions.
                if (hasFocus) {
                    showKeyboard(dBinding.etSearch)

                }
            }
    }
    //endregion


    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {

        if (mSearchEditTextLayoutListener.onKey(this, event.keyCode, event)) {
            return true
        }
        return super.dispatchKeyEventPreIme(event)
    }

    /**
     * If the search term is empty and the user closes the soft keyboard, close the search UI.
     */
    private val mSearchEditTextLayoutListener =
        OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN &&
                isOpen
            ) {
                val keyboardHidden: Boolean = hideKeyboard(v)
                if (keyboardHidden) return@OnKeyListener true
                closeSearch()
                return@OnKeyListener true
            }
            false
        }
    //region Show Methods
    /**
     * Displays the keyboard with a focus on the Search EditText.
     * @param view The view to attach the keyboard to.
     */
    private fun showKeyboard(view: View?) {
        view?.requestFocus()
        if (isHardKeyboardAvailable.not()) {
            val inputMethodManager =
                view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.showSoftInput(view, 0)
        }
    }

    /**
     * Method that checks if there's a physical keyboard on the phone.
     *
     * @return true if there's a physical keyboard connected, false otherwise.
     */
    private val isHardKeyboardAvailable: Boolean
        get() = mContext.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS


    /**
     * Changes the visibility of the clear button to VISIBLE or GONE.
     * @param display True to display the clear button, false to hide it.
     */
    private fun displayClearButton(display: Boolean) {

        dBinding.actionClear.visibility = if (display) VISIBLE else GONE
    }

    var onSearchView: OnSearchCallBack? = null
        set(value) {
            field = value
        }


    /**
     * Displays the SearchView.
     */
    fun openSearch() {
        // If search is already open, just return.
        if (isOpen) {
            return
        }

        // Get focus
        dBinding.etSearch.setText(EMPTY_STRING)
        dBinding.etSearch.requestFocus()
        dBinding.searchLayout.visibility = VISIBLE

        val listenerAdapter: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // After the animation is done. Hide the root view.
                showSuggestions()
            }
        }

        circleRevealView(view = dBinding.searchBar, listenerAdapter = listenerAdapter)



        isOpen = true
    }
    //endregion


    /**
     * Hides the keyboard displayed for the SearchEditText.
     * @param view The view to detach the keyboard from.
     */
    private fun hideKeyboard(view: View): Boolean {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Closes the search view if necessary.
     */
    fun closeSearch() {
        // If we're already closed, just return.
        if (isOpen.not()) {
            return
        }

        // Clear text, values, and focus.
        dBinding.etSearch.setText(EMPTY_STRING)
        clearFocus()
        onSearchView?.onClose()
        dismissSuggestions()

            val listenerAdapter: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    // After the animation is done. Hide the root view.
                    dBinding.searchLayout.visibility = INVISIBLE
                }
            }
                circleHideView(dBinding.searchBar, listenerAdapter)


        // Call listener if we have one
        //mSearchViewListener?.onSearchViewClosed()

        isOpen = false
    }

    private fun showSuggestions() {

        dBinding.suggestionList.visibility = VISIBLE
    }

    private fun dismissSuggestions() {

        dBinding.suggestionList.removeAllViewsInLayout()

        dBinding.suggestionList.visibility = GONE
    }
    //endregion

    //region Interface Methods
    /**
     * Filters and updates the buttons when text is changed.
     * @param newText The new text.
     */
    private fun onTextChanged(newText: CharSequence) {
        // Get current query
        mCurrentQuery = newText.toString()

        // If the text is not empty, show the empty button and hide the voice button
        if (mCurrentQuery.isNotEmpty()) {

            displayClearButton(true)
        } else {
            displayClearButton(false)

        }

        // If we have a query listener and the text has changed, call it.
        //mOnQueryTextListener?.onQueryTextChange(newText.toString())

        mOldQuery = mCurrentQuery
    }


    /**
     * Handles when the clear (X) button is clicked.
     */
    private fun onClearClicked() {
        dBinding.etSearch.setText(EMPTY_STRING)
    }


    /**
     * Toggles the Tint click action.
     *
     * @param shouldClose - Whether the tint click should close the search view or not.
     */
    fun setCloseOnTintClick(shouldClose: Boolean) {
        mShouldCloseOnTintClick = shouldClose
    }


    /**
     * Sets whether the MSV should be animated on open/close or not.
     *
     * @param mShouldAnimate - true if you want animations, false otherwise.
     */
    fun setShouldAnimate(mShouldAnimate: Boolean) {
        this.mShouldAnimate = mShouldAnimate
    }

    /**
     * Sets whether the MSV should be keeping track of the submited queries or not.
     *
     * @param keepHistory - true if you want to save the search history, false otherwise.
     */
    fun setShouldKeepHistory(keepHistory: Boolean) {
        mShouldKeepHistory = keepHistory
    }


    fun setSearchBarColor(color: Int) {
        // Set background color of search bar.
        dBinding.etSearch.setBackgroundColor(color)
    }


    /**
     * Adjust the alpha of a color based on a percent factor.
     *
     * @param color - The color you want to change the alpha value.
     * @param factor - The factor of the alpha, from 0% to 100%.
     * @return The color with the adjusted alpha value.
     */
    private fun adjustAlpha(color: Int, factor: Float): Int {
        if (factor < 0) return color
        val alpha = (Color.alpha(color) * factor).roundToInt()
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    /**
     * Sets the text color of the EditText.
     * @param color The color to use for the EditText.
     */
    fun setTextColor(color: Int) {
        dBinding.etSearch.setTextColor(color)
    }

    /**
     * Sets the text color of the search hint.
     * @param color The color to be used for the hint text.
     */
    fun setHintTextColor(color: Int) {
        dBinding.etSearch.setHintTextColor(color)
    }

    /**
     * Sets the hint to be used for the search EditText.
     * @param hint The hint to be displayed in the search EditText.
     */
    fun setHint(hint: CharSequence?) {
        dBinding.etSearch.hint = hint
    }


    /**
     * Sets the icon for the clear action.
     * @param resourceId The resource ID of drawable that will represent the clear action.
     */
    fun setClearIcon(resourceId: Int) {
        dBinding.actionClear.setImageResource(resourceId)
    }

    /**
     * Sets the icon for the back action.
     * @param resourceId The resource Id of the drawable that will represent the back action.
     */
    fun setBackIcon(resourceId: Int) {
        dBinding.actionBack.setImageResource(resourceId)
    }

    /**
     * Sets the background of the suggestions ListView.
     *
     * @param resource The resource to use as a background for the
     * suggestions listview.
     */
    fun setSuggestionBackground(resource: Int) {
        if (resource > 0) {
            dBinding.suggestionList.setBackgroundResource(resource)
        }
    }

    // TODO - Set text size, icon size and other modifications. -
    // TODO - See: https://github.com/Mauker1/MaterialSearchView/issues/157


    /**
     * Sets the input type of the SearchEditText.
     *
     * @param inputType The input type to set to the EditText.
     */
    fun setInputType(inputType: Int) {
        dBinding.etSearch.inputType = inputType
    }


    /**
     * Sets the bar height if prefered to not use the existing actionbar height value
     *
     * @param height The value of the height in pixels
     */
    fun setSearchBarHeight(height: Int) {
        dBinding.searchBar.minimumHeight = height
        dBinding.searchBar.layoutParams.height = height
    }

    fun setVoiceHintPrompt(hintPrompt: String) {
        mHintPrompt = if (hintPrompt.isNotBlank()) {
            hintPrompt
        } else {
            mContext.getString(R.string.hint_prompt)
        }
    }

    /**
     * Returns the actual AppCompat ActionBar height value. This will be used as the default
     *
     * @return The value of the actual actionbar height in pixels
     */
    private val appCompatActionBarHeight: Int
        get() {
            val tv = TypedValue()
            context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)
            return resources.getDimensionPixelSize(tv.resourceId)
        }
    //endregion

    //region Accessors
    /**
     * Gets the current text on the SearchView, if any. Returns an empty String if no text is available.
     * @return The current query, or an empty String if there's no query.
     */
    val currentQuery: String
        get() = if (mCurrentQuery.isNotEmpty()) {
            mCurrentQuery.toString()
        } else EMPTY_STRING // Get package manager


    //endregion

    //region View Methods
    /**
     * Handles any cleanup when focus is cleared from the view.
     */
    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        dBinding.etSearch.clearFocus()
        mClearingFocus = false
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        // Don't accept if we are clearing focus, or if the view isn't focusable.
        return !(mClearingFocus || !isFocusable) && dBinding.etSearch.requestFocus(
            direction,
            previouslyFocusedRect
        )
    }
}
