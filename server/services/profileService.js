// server/services/profileService.js
// Secure User Profile management using Supabase RLS and existing database tables

import { supabase, getAuthenticatedClient } from '../supabaseClient.js';
import { AuthService } from './authService.js';

export class ProfileService {
  /**
   * Load a user's profile securely
   * @param {string} userId
   * @param {string} [accessToken]
   */
  static async getProfile(userId, accessToken) {
    if (!userId) {
      return { success: false, error: 'User ID is required' };
    }

    try {
      const client = accessToken ? getAuthenticatedClient(accessToken) : supabase;
      const { data, error } = await client
        .from('profiles')
        .select('*')
        .eq('id', userId)
        .maybeSingle();

      if (error) {
        return { success: false, error: error.message };
      }

      if (!data) {
        return {
          success: true,
          profile: {
            id: userId,
            displayName: 'ProCricketer',
            countryCode: 'PK',
            ratingElo: 1200,
            tier: 'Gold League',
            matchesPlayed: 0,
            matchesWon: 0,
            matchesLost: 0,
            winRatePercent: 0
          }
        };
      }

      return {
        success: true,
        profile: {
          id: data.id,
          displayName: data.display_name || data.displayName || 'ProCricketer',
          countryCode: data.country_code || data.countryCode || 'PK',
          ratingElo: data.rating_elo ?? data.ratingElo ?? 1200,
          tier: data.tier || 'Gold League',
          matchesPlayed: data.matches_played ?? data.matchesPlayed ?? 0,
          matchesWon: data.matches_won ?? data.matchesWon ?? 0,
          matchesLost: data.matches_lost ?? data.matchesLost ?? 0,
          winRatePercent: data.matches_played > 0
            ? Math.round(((data.matches_won || 0) / data.matches_played) * 100)
            : 0
        }
      };
    } catch (err) {
      return { success: false, error: err.message || 'Failed to fetch profile' };
    }
  }

  /**
   * Update user profile securely with RLS protection
   * @param {string} userId
   * @param {string} accessToken
   * @param {object} updates
   */
  static async updateProfile(userId, accessToken, updates = {}) {
    if (!userId || !accessToken) {
      return { success: false, error: 'User ID and access token are required' };
    }

    // Verify authenticated user identity
    const sessionRes = await AuthService.getSessionUser(accessToken);
    if (!sessionRes.success || sessionRes.user.id !== userId) {
      return {
        success: false,
        error: 'Unauthorized: Cannot update another user profile'
      };
    }

    // Sanitize user updates: Prevent clients from artificially changing Elo, tier, or admin flags
    const sanitized = {};
    if (updates.displayName || updates.display_name) {
      sanitized.display_name = String(updates.displayName || updates.display_name).trim().slice(0, 32);
    }
    if (updates.countryCode || updates.country_code) {
      sanitized.country_code = String(updates.countryCode || updates.country_code).trim().slice(0, 4).toUpperCase();
    }
    if (updates.avatarUrl || updates.avatar_url) {
      sanitized.avatar_url = String(updates.avatarUrl || updates.avatar_url).trim();
    }
    if (updates.privacyPreferences || updates.privacy_preferences) {
      sanitized.privacy_preferences = updates.privacyPreferences || updates.privacy_preferences;
    }

    sanitized.updated_at = new Date().toISOString();

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client
        .from('profiles')
        .update(sanitized)
        .eq('id', userId)
        .select()
        .maybeSingle();

      if (error) {
        return { success: false, error: error.message };
      }

      return {
        success: true,
        profile: data || { id: userId, ...sanitized }
      };
    } catch (err) {
      return { success: false, error: err.message || 'Profile update failed' };
    }
  }
}
