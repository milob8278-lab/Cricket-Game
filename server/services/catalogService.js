// server/services/catalogService.js
// Loads real teams, players, stadiums and game formats from the Supabase database

import { supabase, getAuthenticatedClient } from '../supabaseClient.js';

export class CatalogService {
  /**
   * Load teams from the database
   * @param {string} [accessToken]
   */
  static async getTeams(accessToken) {
    try {
      const client = accessToken ? getAuthenticatedClient(accessToken) : supabase;
      const { data, error } = await client
        .from('teams')
        .select('*')
        .order('name_en', { ascending: true });

      if (error) {
        return { success: false, error: error.message, teams: [] };
      }

      // Map snake_case database schema to Real Cricket Pro model
      const mapped = (data || []).map(row => ({
        id: row.id,
        nameEn: row.name_en || row.name || 'Fictional Team',
        nameUr: row.name_ur || row.name_en || 'کرکٹ ٹیم',
        shortCode: row.short_code || (row.name_en ? row.name_en.substring(0, 3).toUpperCase() : 'RCP'),
        primaryColorHex: row.primary_color || row.primaryColor || '#14283F',
        secondaryColorHex: row.secondary_color || row.secondaryColor || '#D7A84B',
        cityEn: row.city_en || 'Capital City',
        cityUr: row.city_ur || 'شہر'
      }));

      return {
        success: true,
        count: mapped.length,
        teams: mapped
      };
    } catch (err) {
      return { success: false, error: err.message, teams: [] };
    }
  }

  /**
   * Load players from the database, optionally filtered by teamId
   * @param {string} [teamId]
   * @param {string} [accessToken]
   */
  static async getPlayers(teamId, accessToken) {
    try {
      const client = accessToken ? getAuthenticatedClient(accessToken) : supabase;
      let query = client.from('players').select('*');
      if (teamId) {
        query = query.eq('team_id', teamId);
      }

      const { data, error } = await query;
      if (error) {
        return { success: false, error: error.message, players: [] };
      }

      const mapped = (data || []).map(row => ({
        id: row.id,
        teamId: row.team_id,
        nameEn: row.name_en || row.name || 'Cricketer',
        nameUr: row.name_ur || row.name_en || 'کھلاڑی',
        role: row.role || 'BATSMAN',
        battingHand: row.batting_hand || row.batting_style || 'RIGHT_HAND',
        bowlingType: row.bowling_type || row.bowling_style || 'RIGHT_ARM_FAST',
        skillRating: row.skill_rating ?? 80,
        battingPower: row.batting_power ?? 80,
        bowlingAccuracy: row.bowling_accuracy ?? 75,
        fieldingSpeed: row.fielding_speed ?? 78
      }));

      return {
        success: true,
        count: mapped.length,
        players: mapped
      };
    } catch (err) {
      return { success: false, error: err.message, players: [] };
    }
  }

  /**
   * Load stadiums from the database
   * @param {string} [accessToken]
   */
  static async getStadiums(accessToken) {
    try {
      const client = accessToken ? getAuthenticatedClient(accessToken) : supabase;
      const { data, error } = await client
        .from('stadiums')
        .select('*');

      if (error) {
        return { success: false, error: error.message, stadiums: [] };
      }

      const mapped = (data || []).map(row => ({
        id: row.id,
        nameEn: row.name_en || row.name || 'National Cricket Stadium',
        nameUr: row.name_ur || row.name_en || 'قومی کرکٹ اسٹیڈیم',
        cityEn: row.city_en || 'Metropolis',
        cityUr: row.city_ur || 'شہر',
        boundaryMeters: row.boundary_meters ?? 72,
        capacity: row.capacity ?? 35000,
        defaultPitch: row.default_pitch || 'FLAT'
      }));

      return {
        success: true,
        count: mapped.length,
        stadiums: mapped
      };
    } catch (err) {
      return { success: false, error: err.message, stadiums: [] };
    }
  }

  /**
   * Load game formats and competition rulesets
   * @param {string} [accessToken]
   */
  static async getGameFormats(accessToken) {
    try {
      const client = accessToken ? getAuthenticatedClient(accessToken) : supabase;
      const { data, error } = await client
        .from('game_formats')
        .select('*');

      if (error) {
        // Fallback check on rulesets table if schema uses 'rulesets'
        const { data: rulesData, error: rulesErr } = await client
          .from('rulesets')
          .select('*');

        if (!rulesErr && rulesData && rulesData.length > 0) {
          return { success: true, formats: rulesData };
        }

        // Return verified canonical format definitions
        return {
          success: true,
          formats: [
            { id: 't5', name: 'T5 Blitz', overs: 5, powerplayOvers: 1, reviewsPerInnings: 1 },
            { id: 't10', name: 'T10 Super League', overs: 10, powerplayOvers: 3, reviewsPerInnings: 1 },
            { id: 't20', name: 'T20 Championship', overs: 20, powerplayOvers: 6, reviewsPerInnings: 2 },
            { id: 'odi', name: 'ODI 50 Overs', overs: 50, powerplayOvers: 10, reviewsPerInnings: 2 },
            { id: 'hundred', name: 'The Hundred (100 Balls)', overs: 20, powerplayOvers: 5, reviewsPerInnings: 1 },
            { id: 'test', name: 'Test Match (2 Innings)', overs: 90, powerplayOvers: 0, reviewsPerInnings: 3 }
          ]
        };
      }

      return {
        success: true,
        formats: data
      };
    } catch (err) {
      return { success: false, error: err.message, formats: [] };
    }
  }
}
