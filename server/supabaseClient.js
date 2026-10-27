// server/supabaseClient.js
// Secure Supabase client initialization using @supabase/supabase-js

import { createClient } from '@supabase/supabase-js';
import { SUPABASE_URL, SUPABASE_PUBLISHABLE_KEY, validateSecrets } from './config.js';

// Validate configuration
const validation = validateSecrets();
if (!validation.isValid) {
  console.warn('[Supabase Config Warning]', validation.issues.join('; '));
}

/**
 * Primary secure Supabase client initialized with the publishable/anon key.
 * This client respects Row Level Security (RLS) policies and operates securely.
 */
export const supabase = createClient(
  SUPABASE_URL || 'https://placeholder.supabase.co',
  SUPABASE_PUBLISHABLE_KEY || 'placeholder-key',
  {
    auth: {
      persistSession: false,
      autoRefreshToken: true,
      detectSessionInUrl: false
    },
    global: {
      fetch: (url, options = {}) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 4000);
        return fetch(url, { ...options, signal: options.signal || controller.signal })
          .finally(() => clearTimeout(timeoutId));
      }
    }
  }
);

/**
 * Returns an authenticated Supabase client for a specific user session token.
 * This ensures that database operations execute under the authenticated user's RLS context.
 *
 * @param {string} accessToken - The user's JWT access token from Supabase Auth
 * @returns {import('@supabase/supabase-js').SupabaseClient}
 */
export function getAuthenticatedClient(accessToken) {
  if (!accessToken) {
    return supabase;
  }

  return createClient(
    SUPABASE_URL || 'https://placeholder.supabase.co',
    SUPABASE_PUBLISHABLE_KEY || 'placeholder-key',
    {
      global: {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
        fetch: (url, options = {}) => {
          const controller = new AbortController();
          const timeoutId = setTimeout(() => controller.abort(), 4000);
          return fetch(url, { ...options, signal: options.signal || controller.signal })
            .finally(() => clearTimeout(timeoutId));
        }
      },
      auth: {
        persistSession: false,
        autoRefreshToken: false,
        detectSessionInUrl: false
      }
    }
  );
}
