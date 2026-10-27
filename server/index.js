// server/index.js
// Main entrypoint for Real Cricket Pro Supabase Backend & REST API Server

import express from 'express';
import cors from 'cors';
import { supabase, getAuthenticatedClient } from './supabaseClient.js';
import { AuthService } from './services/authService.js';
import { ProfileService } from './services/profileService.js';
import { CatalogService } from './services/catalogService.js';
import { MatchService } from './services/matchService.js';
import { MultiplayerService } from './services/multiplayerService.js';
import { HealthCheckService } from './services/healthCheckService.js';
import { SUPABASE_URL, SUPABASE_PUBLISHABLE_KEY, validateSecrets } from './config.js';

export {
  supabase,
  getAuthenticatedClient,
  AuthService,
  ProfileService,
  CatalogService,
  MatchService,
  MultiplayerService,
  HealthCheckService,
  SUPABASE_URL,
  SUPABASE_PUBLISHABLE_KEY,
  validateSecrets
};

const app = express();
app.use(cors());
app.use(express.json());

// Helper to extract bearer token
function getBearerToken(req) {
  const auth = req.headers.authorization;
  if (auth && auth.startsWith('Bearer ')) {
    return auth.substring(7);
  }
  return null;
}

// 1. Health check & Diagnostics
app.get('/api/health', async (req, res) => {
  const result = await HealthCheckService.checkConnection();
  res.json(result);
});

// 2. Authentication endpoints
app.post('/api/auth/signup', async (req, res) => {
  const { email, password, displayName, countryCode } = req.body;
  const result = await AuthService.signUp(email, password, { displayName, countryCode });
  res.status(result.success ? 200 : 400).json(result);
});

app.post('/api/auth/login', async (req, res) => {
  const { email, password } = req.body;
  const result = await AuthService.login(email, password);
  res.status(result.success ? 200 : 401).json(result);
});

app.post('/api/auth/logout', async (req, res) => {
  const token = getBearerToken(req);
  const result = await AuthService.logout(token);
  res.json(result);
});

app.get('/api/auth/session', async (req, res) => {
  const token = getBearerToken(req);
  const result = await AuthService.getSessionUser(token);
  res.status(result.success ? 200 : 401).json(result);
});

// 3. User Profile endpoints
app.get('/api/profile/:userId', async (req, res) => {
  const token = getBearerToken(req);
  const result = await ProfileService.getProfile(req.params.userId, token);
  res.status(result.success ? 200 : 404).json(result);
});

app.put('/api/profile/:userId', async (req, res) => {
  const token = getBearerToken(req);
  const result = await ProfileService.updateProfile(req.params.userId, token, req.body);
  res.status(result.success ? 200 : 400).json(result);
});

// 4. Catalog endpoints (Teams, Players, Stadiums, Game Formats)
app.get('/api/teams', async (req, res) => {
  const token = getBearerToken(req);
  const result = await CatalogService.getTeams(token);
  res.json(result);
});

app.get('/api/players', async (req, res) => {
  const token = getBearerToken(req);
  const result = await CatalogService.getPlayers(req.query.teamId, token);
  res.json(result);
});

app.get('/api/stadiums', async (req, res) => {
  const token = getBearerToken(req);
  const result = await CatalogService.getStadiums(token);
  res.json(result);
});

app.get('/api/formats', async (req, res) => {
  const token = getBearerToken(req);
  const result = await CatalogService.getGameFormats(token);
  res.json(result);
});

// 5. Match History & Cloud Saves
app.post('/api/match/save', async (req, res) => {
  const token = getBearerToken(req);
  const { userId, matchData } = req.body;
  const result = await MatchService.saveMatchHistory(userId, token, matchData);
  res.status(result.success ? 200 : 400).json(result);
});

app.get('/api/match/history/:userId', async (req, res) => {
  const token = getBearerToken(req);
  const result = await MatchService.getMatchHistory(req.params.userId, token);
  res.json(result);
});

app.post('/api/cloud/save', async (req, res) => {
  const token = getBearerToken(req);
  const { userId, saveData } = req.body;
  const result = await MatchService.saveCloudSave(userId, token, saveData);
  res.status(result.success ? 200 : 400).json(result);
});

app.get('/api/cloud/save/:userId', async (req, res) => {
  const token = getBearerToken(req);
  const result = await MatchService.getCloudSave(req.params.userId, token);
  res.json(result);
});

// 6. Online Multiplayer Room APIs
app.post('/api/rooms/create', async (req, res) => {
  const token = getBearerToken(req);
  const { hostUserId, config } = req.body;
  const result = await MultiplayerService.createRoom(hostUserId, token, config);
  res.status(result.success ? 200 : 400).json(result);
});

app.post('/api/rooms/join', async (req, res) => {
  const token = getBearerToken(req);
  const { roomCode, guestUserId } = req.body;
  const result = await MultiplayerService.joinRoom(roomCode, guestUserId, token);
  res.status(result.success ? 200 : 400).json(result);
});

app.post('/api/rooms/emote', async (req, res) => {
  const token = getBearerToken(req);
  const { roomCode, senderId, emote } = req.body;
  const result = await MultiplayerService.sendEmote(roomCode, senderId, token, emote);
  res.json(result);
});

app.post('/api/rooms/toss', async (req, res) => {
  const token = getBearerToken(req);
  const { roomCode, tossWinnerId, decision } = req.body;
  const result = await MultiplayerService.updateToss(roomCode, token, tossWinnerId, decision);
  res.json(result);
});

const PORT = process.env.PORT || 3000;
if (process.argv[1] && process.argv[1].endsWith('index.js')) {
  app.listen(PORT, () => {
    console.log(`[Real Cricket Pro Server] Running on http://localhost:${PORT}`);
    console.log(`[Real Cricket Pro Server] Connected to Supabase: ${SUPABASE_URL}`);
  });
}

export default app;
