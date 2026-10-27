// server/services/healthCheckService.js
// Supabase Connection Health Check & Diagnostics for Real Cricket Pro

import { supabase } from '../supabaseClient.js';
import { SUPABASE_URL, SUPABASE_PUBLISHABLE_KEY, validateSecrets } from '../config.js';

export class HealthCheckService {
  /**
   * Run a comprehensive health check against Supabase
   * @returns {Promise<object>} Diagnostic report
   */
  static async checkConnection() {
    const startTime = Date.now();
    const config = validateSecrets();

    if (!config.isValid) {
      return {
        status: 'CONFIGURATION_ERROR',
        message: 'Missing Supabase environment secrets',
        issues: config.issues,
        url: config.url || 'Not set',
        hasPublishableKey: config.hasPublishableKey,
        keyMasked: config.keyMasked,
        latencyMs: 0,
        timestamp: new Date().toISOString()
      };
    }

    const report = {
      status: 'CHECKING',
      url: config.url,
      hasPublishableKey: config.hasPublishableKey,
      keyMasked: config.keyMasked,
      latencyMs: 0,
      authStatus: 'UNKNOWN',
      databaseStatus: 'UNKNOWN',
      tables: {},
      errors: [],
      timestamp: new Date().toISOString()
    };

    try {
      // 1. Auth service ping
      try {
        const { error: authErr } = await supabase.auth.getSession();
        if (authErr) {
          report.authStatus = 'DEGRADED';
          report.errors.push(`Auth check: ${authErr.message}`);
        } else {
          report.authStatus = 'HEALTHY';
        }
      } catch (authEx) {
        report.authStatus = 'UNREACHABLE';
        report.errors.push(`Auth unreachable: ${authEx.message}`);
      }

      // 2. Database Connectivity & Tables checks (run concurrently)
      const targetTables = ['teams', 'stadiums', 'players', 'profiles', 'match_history'];

      await Promise.all(
        targetTables.map(async (table) => {
          try {
            const { data, error, count } = await supabase
              .from(table)
              .select('*', { count: 'exact', head: true });

            if (error) {
              if (error.code === '42P01') {
                report.tables[table] = { status: 'NOT_FOUND', error: error.message };
              } else if (error.code === '42501' || error.message.includes('permission') || error.message.includes('RLS')) {
                report.tables[table] = { status: 'PROTECTED_BY_RLS', message: 'Table exists; protected by Row Level Security' };
              } else {
                report.tables[table] = { status: 'ERROR', error: error.message };
                report.errors.push(`Table ${table}: ${error.message}`);
              }
            } else {
              report.tables[table] = { status: 'ACCESSIBLE', count: count ?? 0 };
            }
          } catch (tblEx) {
            report.tables[table] = { status: 'EXCEPTION', error: tblEx.message };
            report.errors.push(`Table ${table} exception: ${tblEx.message}`);
          }
        })
      );

      report.latencyMs = Date.now() - startTime;
      report.databaseStatus = report.errors.length === 0 ? 'CONNECTED' : (report.authStatus === 'HEALTHY' ? 'PARTIAL' : 'FAILED');
      report.status = report.databaseStatus === 'FAILED' ? 'DISCONNECTED' : 'CONNECTED';

      return report;
    } catch (globalErr) {
      report.latencyMs = Date.now() - startTime;
      report.status = 'ERROR';
      report.databaseStatus = 'ERROR';
      report.errors.push(globalErr.message || 'Unknown network error');
      return report;
    }
  }
}
