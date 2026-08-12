package com.example.data.auth.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.auth.model.StoredAuth
import com.example.domain.auth.model.UserRole
import com.example.domain.auth.repository.AuthTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authTokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

class DataStoreAuthTokenRepository(
    context: Context
) : AuthTokenRepository {

    private val dataStore = context.applicationContext.authTokenDataStore
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val sessionRoleKey = stringPreferencesKey("session_role")

    override suspend fun saveSession(accessToken: String, role: UserRole) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
            prefs[sessionRoleKey] = role.name
        }
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(sessionRoleKey)
        }
    }

    override suspend fun getCurrentSession(): StoredAuth? {
        return dataStore.data.map { it.toStoredAuthOrNull() }.first()
    }

    private fun Preferences.toStoredAuthOrNull(): StoredAuth? {
        val token = this[accessTokenKey] ?: return null
        val roleName = this[sessionRoleKey]
        val role = roleName?.let { name ->
            runCatching { UserRole.valueOf(name) }.getOrNull()
        } ?: UserRole.JOB_SEEKER
        return StoredAuth(accessToken = token, role = role)
    }
}
