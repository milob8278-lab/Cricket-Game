// server/config.js
// Supabase Configuration loader for Real Cricket Pro

import dotenv from 'dotenv';
dotenv.config();

// Read server-side environment secrets: SUPABASE_URL and SUPABASE_PUBLISHABLE_KEY
export const SUPABASE_URL = process.env.SUPABASE_URL || '';
export const SUPABASE_PUBLISHABLE_KEY = process.env.SUPABASE_PUBLISHABLE_KEY || process.env.SUPABASE_ANON_KEY || '';

// Security Verification: Ensure NO secret or service_role key is exposed to clients
export function validateSecrets() {
  const issues = [];
  if (!SUPABASE_URL) {
    issues.push('SUPABASE_URL is missing in environment variables.');
  }
  if (!SUPABASE_PUBLISHABLE_KEY) {
    issues.push('SUPABASE_PUBLISHABLE_KEY is missing in environment variables.');
  }

  // Safety check: Never allow service_role keys to be treated as publishable keys
  if (SUPABASE_PUBLISHABLE_KEY && SUPABASE_PUBLISHABLE_KEY.includes('service_role')) {
    console.warn('[SECURITY WARNING] Detected service_role key pattern. Secret keys must NEVER be exposed!');
  }

  return {
    isValid: issues.length === 0,
    issues,
    url: SUPABASE_URL,
    hasPublishableKey: !!SUPABASE_PUBLISHABLE_KEY,
    keyMasked: SUPABASE_PUBLISHABLE_KEY ? `${SUPABASE_PUBLISHABLE_KEY.substring(0, 6)}...${SUPABASE_PUBLISHABLE_KEY.slice(-4)}` : 'none'
  };
}
