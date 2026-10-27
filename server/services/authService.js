// server/services/authService.js
// Supabase Authentication Service (Signup, Login, Logout, Session verification)

import { supabase, getAuthenticatedClient } from '../supabaseClient.js';

export class AuthService {
  /**
   * User Signup
   * @param {string} email
   * @param {string} password
   * @param {object} metadata - e.g. { displayName, countryCode }
   */
  static async signUp(email, password, metadata = {}) {
    if (!email || !password) {
      return { success: false, error: 'Email and password are required' };
    }

    try {
      const { data, error } = await supabase.auth.signUp({
        email,
        password,
        options: {
          data: {
            display_name: metadata.displayName || email.split('@')[0],
            country_code: metadata.countryCode || 'PK',
            tier: 'Bronze Club',
            rating_elo: 1000
          }
        }
      });

      if (error) {
        return { success: false, error: error.message };
      }

      // If user was created, upsert profile entry into existing profiles table if permitted
      if (data?.user) {
        try {
          const client = data.session?.access_token ? getAuthenticatedClient(data.session.access_token) : supabase;
          await client.from('profiles').upsert({
            id: data.user.id,
            display_name: metadata.displayName || email.split('@')[0],
            country_code: metadata.countryCode || 'PK',
            rating_elo: 1000,
            tier: 'Bronze Club',
            matches_played: 0,
            matches_won: 0,
            matches_lost: 0
          }, { onConflict: 'id' });
        } catch (profileErr) {
          // If profile table trigger already handles profile creation, ignore duplicate
          console.log('[Auth] Profile record handled by database trigger or existing schema');
        }
      }

      return {
        success: true,
        user: data.user,
        session: data.session
      };
    } catch (err) {
      return { success: false, error: err.message || 'Signup failed' };
    }
  }

  /**
   * User Login
   * @param {string} email
   * @param {string} password
   */
  static async login(email, password) {
    if (!email || !password) {
      return { success: false, error: 'Email and password are required' };
    }

    try {
      const { data, error } = await supabase.auth.signInWithPassword({
        email,
        password
      });

      if (error) {
        return { success: false, error: error.message };
      }

      return {
        success: true,
        user: data.user,
        session: data.session
      };
    } catch (err) {
      return { success: false, error: err.message || 'Login failed' };
    }
  }

  /**
   * User Logout
   * @param {string} accessToken
   */
  static async logout(accessToken) {
    try {
      const client = getAuthenticatedClient(accessToken);
      const { error } = await client.auth.signOut();
      if (error) {
        return { success: false, error: error.message };
      }
      return { success: true };
    } catch (err) {
      return { success: false, error: err.message || 'Logout failed' };
    }
  }

  /**
   * Verify authenticated user session from token
   * @param {string} accessToken
   */
  static async getSessionUser(accessToken) {
    if (!accessToken) {
      return { success: false, error: 'No access token provided' };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client.auth.getUser(accessToken);
      if (error || !data?.user) {
        return { success: false, error: error?.message || 'Invalid or expired session' };
      }
      return { success: true, user: data.user };
    } catch (err) {
      return { success: false, error: err.message || 'Session verification failed' };
    }
  }
}
