// server/services/multiplayerService.js
// Secure Online Multiplayer Room APIs for Real Cricket Pro

import { supabase, getAuthenticatedClient } from '../supabaseClient.js';
import { AuthService } from './authService.js';

export class MultiplayerService {
  /**
   * Create an online room
   * @param {string} hostUserId
   * @param {string} accessToken
   * @param {object} config - { format, overs, venueId, teamA, teamB }
   */
  static async createRoom(hostUserId, accessToken, config = {}) {
    if (!hostUserId || !accessToken) {
      return { success: false, error: 'Host user ID and access token required' };
    }

    const sessionRes = await AuthService.getSessionUser(accessToken);
    if (!sessionRes.success || sessionRes.user.id !== hostUserId) {
      return { success: false, error: 'Unauthorized host user session' };
    }

    const roomCode = `RCP-${Math.floor(100 + Math.random() * 900)}`;
    const roomRecord = {
      room_code: roomCode,
      host_user_id: hostUserId,
      guest_user_id: null,
      format: config.format || 'T20',
      overs: config.overs || 5,
      venue_name: config.venueName || 'Karachi International Stadium',
      host_team_id: config.teamAId || 'team_karachi',
      guest_team_id: config.teamBId || 'team_lahore',
      state: 'LOBBY',
      is_host_ready: true,
      is_guest_ready: false,
      active_emotes: [],
      created_at: new Date().toISOString()
    };

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client
        .from('online_rooms')
        .insert([roomRecord])
        .select()
        .maybeSingle();

      if (error) {
        // Return memory-backed room record if table is in provisioning state
        return {
          success: true,
          room: roomRecord,
          isLiveDatabase: false,
          note: error.message
        };
      }

      return {
        success: true,
        room: data || roomRecord,
        isLiveDatabase: true
      };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }

  /**
   * Join an existing room via room code
   * @param {string} roomCode
   * @param {string} guestUserId
   * @param {string} accessToken
   */
  static async joinRoom(roomCode, guestUserId, accessToken) {
    if (!roomCode || !guestUserId || !accessToken) {
      return { success: false, error: 'Room code, guest ID, and token required' };
    }

    const sessionRes = await AuthService.getSessionUser(accessToken);
    if (!sessionRes.success || sessionRes.user.id !== guestUserId) {
      return { success: false, error: 'Unauthorized guest user session' };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data: existing, error: fetchErr } = await client
        .from('online_rooms')
        .select('*')
        .eq('room_code', roomCode.toUpperCase())
        .maybeSingle();

      if (fetchErr) {
        return { success: false, error: fetchErr.message };
      }
      if (!existing) {
        return { success: false, error: `Room ${roomCode} not found` };
      }
      if (existing.guest_user_id && existing.guest_user_id !== guestUserId) {
        return { success: false, error: 'Room is already full' };
      }

      const { data: updated, error: updateErr } = await client
        .from('online_rooms')
        .update({
          guest_user_id: guestUserId,
          is_guest_ready: true,
          state: 'READY',
          updated_at: new Date().toISOString()
        })
        .eq('room_code', roomCode.toUpperCase())
        .select()
        .maybeSingle();

      if (updateErr) {
        return { success: false, error: updateErr.message };
      }

      return {
        success: true,
        room: updated || existing
      };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }

  /**
   * Send a quick emote/reaction in an active room
   * @param {string} roomCode
   * @param {string} senderId
   * @param {string} accessToken
   * @param {object} emote - { emoji, textEn, textUr }
   */
  static async sendEmote(roomCode, senderId, accessToken, emote) {
    if (!roomCode || !senderId || !accessToken) {
      return { success: false, error: 'Parameters missing' };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data: room } = await client
        .from('online_rooms')
        .select('active_emotes')
        .eq('room_code', roomCode.toUpperCase())
        .maybeSingle();

      const list = Array.isArray(room?.active_emotes) ? room.active_emotes : [];
      const newEmote = {
        id: `emote_${Date.now()}`,
        sender_id: senderId,
        emoji: emote.emoji || '🏏',
        text_en: emote.textEn || 'Great Shot!',
        text_ur: emote.textUr || 'شاندار شاٹ!',
        timestamp: new Date().toISOString()
      };

      const updatedList = [newEmote, ...list].slice(0, 5);
      await client
        .from('online_rooms')
        .update({ active_emotes: updatedList })
        .eq('room_code', roomCode.toUpperCase());

      return { success: true, emote: newEmote };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }

  /**
   * Update Room Toss outcome (authenticated)
   */
  static async updateToss(roomCode, accessToken, tossWinnerId, decision) {
    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client
        .from('online_rooms')
        .update({
          toss_winner_id: tossWinnerId,
          toss_decision: decision, // 'BAT' or 'BOWL'
          state: 'IN_MATCH',
          updated_at: new Date().toISOString()
        })
        .eq('room_code', roomCode.toUpperCase())
        .select()
        .maybeSingle();

      if (error) {
        return { success: false, error: error.message };
      }
      return { success: true, room: data };
    } catch (err) {
      return { success: false, error: err.message };
    }
  }
}
