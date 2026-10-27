package com.example.data.supabase

import com.example.BuildConfig

/**
 * Configuration reader for Supabase in Real Cricket Pro.
 * Injected securely via BuildConfig and .env. Never exposes secret or service_role keys.
 */
object SupabaseConfig {
    val supabaseUrl: String
        get() = BuildConfig.SUPABASE_URL.trimEnd('/')

    val publishableKey: String
        get() = BuildConfig.SUPABASE_PUBLISHABLE_KEY

    val isConfigured: Boolean
        get() = supabaseUrl.isNotBlank() &&
                publishableKey.isNotBlank() &&
                !supabaseUrl.contains("placeholder")

    val maskedPublishableKey: String
        get() = if (publishableKey.length > 10) {
            "${publishableKey.take(6)}...${publishableKey.takeLast(4)}"
        } else if (publishableKey.isNotBlank()) {
            "***"
        } else {
            "Not Set"
        }
}
