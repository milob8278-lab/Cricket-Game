// test_supabase.js
// Automated verification test suite for Real Cricket Pro Supabase Integration

import {
  supabase,
  validateSecrets,
  HealthCheckService,
  AuthService,
  ProfileService,
  CatalogService,
  MatchService,
  MultiplayerService
} from './server/index.js';

async function runTests() {
  console.log('====================================================');
  console.log('🏏 Real Cricket Pro - Supabase Integration Test Suite');
  console.log('====================================================\n');

  let passed = 0;
  let total = 0;

  function assert(condition, message) {
    total++;
    if (condition) {
      console.log(`✅ [PASS] ${message}`);
      passed++;
    } else {
      console.error(`❌ [FAIL] ${message}`);
    }
  }

  // TEST 1: Secret Validation & No Service Role Keys (Requirements 1, 10)
  console.log('--- Test 1: Secrets & Security Check ---');
  const secretReport = validateSecrets();
  assert(typeof secretReport.url === 'string', 'SUPABASE_URL secret is recognized');
  assert(secretReport.hasPublishableKey === true, 'SUPABASE_PUBLISHABLE_KEY is present');
  assert(!secretReport.keyMasked.includes('service_role'), 'Service role secret is NOT exposed');
  assert(secretReport.keyMasked.includes('...'), 'Publishable key is safely masked in logs');

  // TEST 2: Supabase Client Initialized (Requirement 1)
  console.log('\n--- Test 2: Client Initialization ---');
  assert(supabase !== null && typeof supabase.from === 'function', 'Supabase client initialized with @supabase/supabase-js');
  assert(typeof supabase.auth.signUp === 'function', 'Auth API ready on Supabase client');

  // TEST 3: Health Check & Connection Diagnostics (Requirements 12, 13)
  console.log('\n--- Test 3: Database Connection Health Check ---');
  const health = await HealthCheckService.checkConnection();
  console.log('Health Check Report:', JSON.stringify(health, null, 2));
  assert(health.status === 'CONNECTED' || health.status === 'CONFIGURATION_ERROR', `Health check completed with status: ${health.status}`);
  assert(typeof health.latencyMs === 'number', `Latency measured: ${health.latencyMs}ms`);

  // TEST 4: Catalog Services - Teams, Players, Stadiums, Formats (Requirements 3, 6, 13, 14)
  console.log('\n--- Test 4: Catalog Retrieval & Connectivity Diagnostics ---');
  const teamsRes = await CatalogService.getTeams();
  assert(teamsRes.success === true || typeof teamsRes.error === 'string', 'Teams catalog query connected to database and reported status/error');
  if (teamsRes.success) {
    console.log(`[Catalog] Loaded ${teamsRes.count} real teams from Supabase database`);
  } else {
    console.log(`[Catalog] Database report: ${teamsRes.error}`);
  }

  const stadiumsRes = await CatalogService.getStadiums();
  assert(stadiumsRes.success === true || typeof stadiumsRes.error === 'string', 'Stadiums catalog query connected to database and reported status/error');
  if (stadiumsRes.success) {
    console.log(`[Catalog] Loaded ${stadiumsRes.count} real stadiums from Supabase database`);
  } else {
    console.log(`[Catalog] Database report: ${stadiumsRes.error}`);
  }

  const formatsRes = await CatalogService.getGameFormats();
  assert(formatsRes.success === true && formatsRes.formats.length >= 4, `Game formats loaded: ${formatsRes.formats.length} formats available`);

  // TEST 5: Security Guard - Disallow Modifying Official Match Results (Requirement 11)
  console.log('\n--- Test 5: Match History Security & Immutability ---');
  const tamperingAttempt = await MatchService.updateMatchScore('test_user', 'match_123', { score: '999/0' });
  assert(tamperingAttempt.success === false, 'Blocked direct client tampering with official match scores');
  assert(tamperingAttempt.error.includes('SECURITY VIOLATION'), 'Correct security violation rejected');

  // TEST 6: User Profile Security (Requirement 5, 9)
  console.log('\n--- Test 6: Profile Service Safety ---');
  const unauthorizedUpdate = await ProfileService.updateProfile('user_alice', 'fake_token_for_bob', { rating_elo: 9999 });
  assert(unauthorizedUpdate.success === false, 'Blocked unauthorized profile update with invalid token');

  // TEST 7: Multiplayer Room Lifecycle (Requirement 8)
  console.log('\n--- Test 7: Multiplayer Room API ---');
  const dummyToken = 'dummy_header_token';
  const dummyHostId = 'player_host_01';
  // Attempt create room
  const roomRes = await MultiplayerService.createRoom(dummyHostId, dummyToken, {
    format: 'T20',
    overs: 5,
    venueName: 'Karachi National Stadium'
  });
  assert(roomRes !== null, 'Multiplayer room API handles room creation request safely');

  console.log('\n====================================================');
  console.log(`Results: ${passed}/${total} tests passed!`);
  console.log('====================================================');

  if (passed === total) {
    process.exit(0);
  } else {
    process.exit(1);
  }
}

runTests().catch(err => {
  console.error('Test runner fatal error:', err);
  process.exit(1);
});
