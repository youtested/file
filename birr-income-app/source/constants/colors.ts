/**
 * Semantic design tokens for the mobile app.
 *
 * Mirrors the "Espresso & Terracotta" palette used by the sibling
 * birr-income web artifact (src/index.css), converted from HSL to hex, so
 * both artifacts share the same warm, tactile identity.
 */

const colors = {
  light: {
    // Legacy aliases (kept for backward compatibility)
    text: '#30251d',
    tint: '#bd4d28',

    // Core surfaces
    background: '#f9f6f1',
    foreground: '#30251d',

    // Cards / elevated surfaces
    card: '#fcfbf8',
    cardForeground: '#30251d',

    // Primary action color (buttons, links, active states) — terracotta/brick
    primary: '#bd4d28',
    primaryForeground: '#f9f6f1',

    // Secondary / less-emphasis interactive surfaces
    secondary: '#eae2d7',
    secondaryForeground: '#30251d',

    // Muted / subdued elements (dividers, timestamps, placeholders)
    muted: '#ede7de',
    mutedForeground: '#847062',

    // Accent highlights (badges, selected items, focus rings) — warm gold
    accent: '#d19f47',
    accentForeground: '#30251d',

    // Destructive actions (delete, error states)
    destructive: '#d22d2d',
    destructiveForeground: '#f9f6f1',

    // Borders and input outlines
    border: '#e0d9d1',
    input: '#e0d9d1',
  },

  dark: {
    text: '#f9f6f1',
    tint: '#cc5933',

    background: '#281d15',
    foreground: '#f9f6f1',

    card: '#30251d',
    cardForeground: '#f9f6f1',

    primary: '#cc5933',
    primaryForeground: '#f9f6f1',

    secondary: '#4d3e33',
    secondaryForeground: '#f9f6f1',

    muted: '#3d3129',
    mutedForeground: '#b8a994',

    accent: '#cc9433',
    accentForeground: '#281d15',

    destructive: '#d22d2d',
    destructiveForeground: '#f9f6f1',

    border: '#4d3e33',
    input: '#4d3e33',
  },

  // Border radius (in px), synced from the web artifact's --radius (0.75rem = 12px).
  radius: 12,
};

export default colors;
