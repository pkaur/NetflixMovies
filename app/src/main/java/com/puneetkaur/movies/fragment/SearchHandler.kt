package com.puneetkaur.movies.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputEditText

/**
 * A utility class for handling search functionality in a [TextInputEditText].
 *
 * @constructor Creates a [SearchHandler] instance.
 */
class SearchHandler() {

    /**
     * Sets up the provided [searchView] for handling search actions.
     *
     * @param searchView The [TextInputEditText] to set up for search.
     * @param onSearch Callback function to be invoked when a search action is performed.
     */
    fun setupSearchView(searchView: TextInputEditText, onSearch: (String) -> Unit) {
        searchView.addTextChangedListener(createTextWatcher(onSearch))
        searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                actionId == EditorInfo.IME_ACTION_NEXT
            ) {
                handleSearchAction(searchView.text.toString().trim(), onSearch)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    /**
     * Creates a [TextWatcher] for the [TextInputEditText] to respond to text changes.
     *
     * @param onSearch Callback function to be invoked when text changes occur.
     * @return A [TextWatcher] instance.
     */
    private fun createTextWatcher(onSearch: (String) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                onSearch(s.toString().trim())
            }
        }
    }

    /**
     * Handles the search action by invoking the [onSearch] callback if the [query] is not empty.
     *
     * @param query The search query entered by the user.
     * @param onSearch Callback function to be invoked for search actions.
     */
    private fun handleSearchAction(query: String, onSearch: (String) -> Unit) {
        if (query.isNotEmpty()) {
            onSearch(query)
        }
    }
}
