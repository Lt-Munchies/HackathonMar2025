package com.devconnect.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val authorIdKey = stringPreferencesKey("author_id")
    private val authorAliasKey = stringPreferencesKey("author_alias")

    val authorId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[authorIdKey]
    }

    val authorAlias: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[authorAliasKey]
    }

    suspend fun setAuthor(alias: String) {
        context.dataStore.edit { preferences ->
            preferences[authorIdKey] = UUID.randomUUID().toString()
            preferences[authorAliasKey] = alias
        }
    }

    suspend fun clearAuthor() {
        context.dataStore.edit { preferences ->
            preferences.remove(authorIdKey)
            preferences.remove(authorAliasKey)
        }
    }
}
