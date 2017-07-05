package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Stéf on 04/08/2016.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.agence_creation_sc.tools.sitespareupdatev2.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}