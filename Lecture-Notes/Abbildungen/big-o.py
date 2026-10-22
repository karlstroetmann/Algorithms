#!/usr/bin/env python3
"""Draw Abbildungen/big-o.pdf, the example for f in O(g) in complexity.tex.

The curves follow the figure of the Wikipedia article on big O notation: f(x) (red) and
c*g(x) (blue) cross several times, but f(x) <= c*g(x) holds for all x >= k = 5.
Needs matplotlib and a LaTeX installation (the labels are set in Computer Modern Sans).
"""
from pathlib import Path

import matplotlib
matplotlib.use('pdf')
import matplotlib.pyplot as plt
import numpy as np

plt.rcParams.update({
    'text.usetex': True,
    'text.latex.preamble': r'\usepackage{sansmath}\sansmath\renewcommand{\familydefault}{\sfdefault}',
    'font.family': 'sans-serif',
    'font.size': 10,
})

def pchip(xs, ys, x):
    """Monotone cubic interpolation (Fritsch-Carlson): no overshoot, so the local extrema
    of the curve are exactly the given points."""
    xs, ys = np.asarray(xs, float), np.asarray(ys, float)
    h, delta = np.diff(xs), np.diff(ys) / np.diff(xs)
    d = np.zeros_like(ys)
    for i in range(1, len(xs) - 1):
        if delta[i - 1] * delta[i] > 0:
            w1, w2 = 2 * h[i] + h[i - 1], h[i] + 2 * h[i - 1]
            d[i] = (w1 + w2) / (w1 / delta[i - 1] + w2 / delta[i])
    d[0], d[-1] = delta[0], delta[-1]
    i = np.clip(np.searchsorted(xs, x) - 1, 0, len(xs) - 2)
    t = (x - xs[i]) / h[i]
    h00, h10 = 2*t**3 - 3*t**2 + 1, t**3 - 2*t**2 + t
    h01, h11 = -2*t**3 + 3*t**2, t**3 - t**2
    return h00*ys[i] + h10*h[i]*d[i] + h01*ys[i+1] + h11*h[i]*d[i+1]

x  = np.linspace(0, 8.8, 800)
f  = pchip([0, 0.85, 2.0, 4.1, 5.0, 6.0, 7.0, 8.0, 8.8],
           [3, 4.1,  0.5, 6.05, 5.0, 6.0, 8.0, 9.0, 9.4], x)
xg = np.linspace(0, 6.35, 600)
cg = pchip([0, 0.95, 2.0, 3.1, 3.85, 5.0, 6.0, 6.35],
           [2, 3.0,  1.0, 4.0, 2.75, 10.0, 14.0, 15.5], xg)

fig, ax = plt.subplots(figsize=(4.6, 4.3))
ax.plot(xg, cg, color='blue', linewidth=1.2, label=r'$c \cdot g(x)$')
ax.plot(x,  f,  color='red',  linewidth=1.2, label=r'$f(x)$')
ax.axvline(5, color='gray', linewidth=0.6, linestyle='--')
ax.annotate(r'$k$', xy=(5, 0), xytext=(5, -1.1), ha='center', va='top', annotation_clip=False)

# axes through the origin with arrow heads, no frame
ax.set_xlim(0, 9.2)
ax.set_ylim(0, 16)
for side in ('top', 'right'):
    ax.spines[side].set_visible(False)
ax.spines['left'].set_position('zero')
ax.spines['bottom'].set_position('zero')
ax.plot(1, 0, '>k', transform=ax.get_yaxis_transform(), clip_on=False, markersize=4)
ax.plot(0, 1, '^k', transform=ax.get_xaxis_transform(), clip_on=False, markersize=4)
ax.set_xticks(range(1, 9))
ax.set_yticks(range(2, 16, 2))
ax.text(9.2, -0.6, r'$x$', ha='center', va='top')
ax.text(-0.35, 16, r'$y$', ha='right', va='center')
ax.legend(loc='lower right', bbox_to_anchor=(1.0, 0.2), frameon=True, fancybox=False, edgecolor='black')

fig.tight_layout()
fig.savefig(Path(__file__).with_suffix('.pdf'))
