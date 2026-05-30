---
name: Sovereign Terminal
colors:
  surface: '#1d0c26'
  surface-dim: '#1d0c26'
  surface-bright: '#45324e'
  surface-container-lowest: '#170720'
  surface-container-low: '#25152f'
  surface-container: '#2a1933'
  surface-container-high: '#35233e'
  surface-container-highest: '#402e49'
  on-surface: '#f4dafe'
  on-surface-variant: '#b9ccb5'
  inverse-surface: '#f4dafe'
  inverse-on-surface: '#3b2a45'
  outline: '#849581'
  outline-variant: '#3b4b3a'
  surface-tint: '#00e55b'
  primary: '#edffe8'
  on-primary: '#003911'
  primary-container: '#00ff66'
  on-primary-container: '#007128'
  inverse-primary: '#006e27'
  secondary: '#ffaaf8'
  on-secondary: '#5a005e'
  secondary-container: '#ae06b5'
  on-secondary-container: '#ffd3f7'
  tertiary: '#f5fbff'
  on-tertiary: '#003545'
  tertiary-container: '#aee6ff'
  on-tertiary-container: '#006a86'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#6bff83'
  primary-fixed-dim: '#00e55b'
  on-primary-fixed: '#002107'
  on-primary-fixed-variant: '#00531b'
  secondary-fixed: '#ffd6f7'
  secondary-fixed-dim: '#ffaaf8'
  on-secondary-fixed: '#37003a'
  on-secondary-fixed-variant: '#800084'
  tertiary-fixed: '#bbe9ff'
  tertiary-fixed-dim: '#5ed4ff'
  on-tertiary-fixed: '#001f29'
  on-tertiary-fixed-variant: '#004d63'
  background: '#1d0c26'
  on-background: '#f4dafe'
  surface-variant: '#402e49'
typography:
  headline-lg:
    fontFamily: Hanken Grotesk
    fontSize: 40px
    fontWeight: '800'
    lineHeight: '1.1'
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Hanken Grotesk
    fontSize: 24px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: 0.05em
  code-display:
    fontFamily: JetBrains Mono
    fontSize: 18px
    fontWeight: '500'
    lineHeight: '1.5'
  body-main:
    fontFamily: JetBrains Mono
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.6'
  label-caps:
    fontFamily: JetBrains Mono
    fontSize: 11px
    fontWeight: '700'
    lineHeight: '1'
    letterSpacing: 0.15em
  status-code:
    fontFamily: JetBrains Mono
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
spacing:
  unit: 4px
  gutter: 1px
  margin-sm: 8px
  margin-md: 16px
  margin-lg: 32px
  panel-padding: 12px
---

## Brand & Style
The design system is engineered for the "Sovereign Digital Command Center," a high-utility environment for power users who view their OS as an extension of their cognitive architecture. The aesthetic is rooted in **Cyberpunk Brutalism**—a rigid, modular framework that prioritizes data density and technical precision over decorative softness.

The UI evokes a sense of absolute control and futuristic authority. It utilizes a "Terminal-First" philosophy, where every pixel serves a functional purpose. Visual interest is derived not from imagery, but from the rhythm of information, sharp geometric dividers, and high-frequency data streams. The experience should feel like stepping into a secure, encrypted node where the user is the ultimate authority.

## Colors
The palette is built on a "Total Dark" foundation to minimize eye strain during long-duration technical sessions.

*   **Background Base (#000000):** Used for the primary canvas to provide infinite depth and maximum contrast.
*   **Surface Neutral (#0a0014):** A deep purple-black used for container backgrounds and subtle sectioning to differentiate from the void.
*   **Primary Neon Green (#00FF66):** Reserved for system-critical success states, active terminal cursors, and primary "Go" actions.
*   **Secondary Vibrant Pink (#FF66FF):** Used for alerts, high-priority warnings, and accenting structural hierarchy.
*   **Tertiary Cyan (#00CCFF):** Used for data visualization, informational tags, and secondary navigation elements.

Color should be used sparingly as a "data signal" rather than a decorative element. Functional areas should remain monochromatic until interaction occurs.

## Typography
The typography strategy employs a dual-font system to balance legibility and technical aesthetic.

*   **Primary Sans (Hanken Grotesk):** Used for large-scale headers and organizational labels. It provides a clean, professional "SaaS" structure to the otherwise chaotic data. Use uppercase for a more authoritative, architectural feel.
*   **Monospace (JetBrains Mono):** The workhorse of the design system. Used for all data, terminal output, body text, and labels. The monospaced nature ensures that columns of numbers and code align perfectly, reinforcing the modular grid.

All text should feel like it was "rendered" rather than "published." High letter-spacing on small labels mimics the look of vintage avionics and hardware displays.

## Layout & Spacing
This design system utilizes a **Fixed Modular Grid**. The layout is treated like a hardware motherboard, where components are locked into a strict x/y coordinate system.

*   **The 1px Gutter:** Use 1px primary-colored or neutral-colored borders as the primary separator between modules instead of whitespace. This creates a "tiled" look.
*   **Data Density:** Padding within components is kept to a minimum (12px) to maximize the amount of information visible on a single screen.
*   **Breakpoints:** 
    *   **Desktop (1440px+):** Full 12-column multi-pane layout with persistent sidebars.
    *   **Tablet/Small Screen:** Collapsible sidebars, switching to a 2-column stack.
    *   **Reflow:** Elements do not "flow" like a document; they "snap" into modular blocks.

## Elevation & Depth
In this system, depth is achieved through **Luminance and Borders** rather than shadows.

*   **Zero Shadows:** Do not use drop shadows. They suggest a light source that doesn't exist in a terminal.
*   **Tonal Stacking:** The base layer is #000000. Active panels use #0a0014. Overlays use the same #0a0014 but with a 1px vibrant border.
*   **Neon Glow:** Active or "focused" states utilize an `outer-glow` effect (a high-spread, low-opacity drop shadow in the primary color) to simulate a cathode-ray tube (CRT) or holograph emission.
*   **Scanlines:** A subtle, 2px repeat linear gradient overlay can be applied to the entire UI at 3% opacity to reinforce the technical screen aesthetic.

## Shapes
The shape language is strictly **Rectilinear**. 

*   **Corner Radius:** 0px across all elements (Buttons, Cards, Inputs, Modals).
*   **Beveled Accents:** Use 45-degree clipped corners (dog-ear snips) on primary headers or buttons to reinforce the "military-grade" hardware aesthetic.
*   **Geometric Dividers:** Use vertical and horizontal lines of 1px thickness to define all spatial relationships.

## Components
Consistent styling across the command center ensures rapid recognition of interactive zones.

*   **Buttons:** Rectangular with 1px borders. Default state is a Cyan border with Cyan text. Active/Hover state is a full Pink fill with Black text and a Pink outer glow.
*   **Input Fields:** Single-line bottom border (1px) in Neutral. When focused, the border turns Green and a solid block cursor (non-blinking or fast-blink) appears.
*   **Terminal Lists:** Monospaced text with a 1px vertical line on the left side of the "Active" item. Use alternating row backgrounds (0% vs 5% opacity White) for high-density tables.
*   **Data Chips:** Small, rectangular blocks with solid background fills and contrasting monospaced text (e.g., Green background with Black text). Used for status codes or tags.
*   **Gauges & Meters:** Minimalist horizontal bars. Use the primary green for "within threshold" and pink for "critical overflow." No gradients; use segmented blocks for the bar fill.
*   **Command Palette:** A centered modal with a 1px Neon Green border and a backdrop blur of the underlying modules.

I like duolingo and strava style.