# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/6 (16.7%)
- **Function parity:** 1/414 matched (target 1) — 0.2%
- **Class/type parity:** 3/32 matched (target 6) — 9.4%
- **Combined symbol parity:** 4/446 matched (target 7) — 0.9%
- **Average inline-code cosine:** 0.00 (function body across 0 matched files)
- **Average documentation cosine:** 0.00 (doc text across 0 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `socket2.Type [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 212510.0
- **Functions:** 1/16 matched (target 1)
- **Missing functions:** `from`, `is_truncated`, `fmt`, `new`, `deref`, `deref_mut`, `with_time`, `with_interval`, `with_retries`, `with_addr`, `with_buffers`, `with_control`, `with_flags`, `flags`, `control_len`
- **Types:** 3/9 matched (target 6)
- **Missing types:** `RecvFlags`, `MaybeUninitSlice`, `Target`, `TcpKeepalive`, `MsgHdr`, `MsgHdrMut`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Lint issues:** 3

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

